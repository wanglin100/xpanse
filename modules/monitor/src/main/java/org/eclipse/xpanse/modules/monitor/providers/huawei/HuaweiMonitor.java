/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.providers.huawei;

import com.huaweicloud.sdk.ces.v1.CesClient;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataRequest;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataResponse;
import com.huaweicloud.sdk.core.auth.ICredential;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.database.service.DeployResourceEntity;
import org.eclipse.xpanse.modules.database.service.DeployServiceEntity;
import org.eclipse.xpanse.modules.models.enums.Csp;
import org.eclipse.xpanse.modules.models.enums.DeployResourceKind;
import org.eclipse.xpanse.modules.models.enums.DeployVariableKind;
import org.eclipse.xpanse.modules.models.resource.DeployVariable;
import org.eclipse.xpanse.modules.monitor.providers.huawei.models.HuaweiMonitorResponse;
import org.eclipse.xpanse.modules.monitor.Monitor;
import org.eclipse.xpanse.modules.monitor.providers.huawei.common.HuaweiMonitorConstant;
import org.eclipse.xpanse.modules.monitor.providers.huawei.utils.HuaweiMonitorClientUtil;
import org.eclipse.xpanse.modules.monitor.providers.huawei.utils.HuaweiMonitorHttpConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Plugin to get monitor data on Huawei cloud.
 */
@Component
@Slf4j
public class HuaweiMonitor implements Monitor {

    private final HuaweiMonitorClientUtil client;

    @Autowired
    public HuaweiMonitor(
            HuaweiMonitorClientUtil client) {
        this.client = client;
    }

    /**
     * Get CSP.
     *
     * @param deployServiceEntity DeployServiceEntity.
     * @return
     */
    @Override
    public Csp getCsp(DeployServiceEntity deployServiceEntity) {
        return deployServiceEntity.getCsp();
    }

    /**
     * @param serviceEntity the resource of the deployment.
     * @param agentEnabled  the agent state.
     * @param fromTime      the start time of the monitor.
     * @param toTime        the end time of the monitor.
     * @param resourceType  the type of the monitor resource.
     * @return
     */
    @Override
    public List<HuaweiMonitorResponse> getUsage(DeployServiceEntity serviceEntity,
            Boolean agentEnabled, String fromTime, String toTime, String resourceType) {

        List<HuaweiMonitorResponse> huaweiMonitorResponseList = new ArrayList<>();
        if (Objects.isNull(serviceEntity)) {
            return huaweiMonitorResponseList;
        }

        Map<String, String> variable = getVariable(serviceEntity);
        // TODO 权限校验.
        ICredential iCredential = client.getICredential(
                variable.get(HuaweiMonitorConstant.HW_ACCESS_KEY),
                variable.get(HuaweiMonitorConstant.HW_SECRET_KEY));
        CesClient cesClient = client.getCesClient(iCredential,
                serviceEntity.getCreateRequest().getRegion());
        for (DeployResourceEntity resourceEntity : serviceEntity.getDeployResourceList()) {
            if (resourceEntity.getKind().equals(DeployResourceKind.VM)) {
                ShowMetricDataRequest request =
                        HuaweiMonitorHttpConvert.convertRequest(resourceEntity, agentEnabled,
                                resourceType, fromTime, toTime);
                ShowMetricDataResponse response = client.showMetricData(cesClient, request);
                HuaweiMonitorResponse huaweiMonitorResponse = HuaweiMonitorHttpConvert.convertResponse(
                        response, resourceEntity.getResourceId());
                huaweiMonitorResponseList.add(huaweiMonitorResponse);
            }
        }
        return huaweiMonitorResponseList;
    }

    private Map<String, String> getVariable(DeployServiceEntity deployServiceEntity) {
        Map<String, String> variables = new HashMap<>();
        Map<String, String> request = deployServiceEntity.getCreateRequest()
                .getServiceRequestProperties();
        for (DeployVariable variable : deployServiceEntity.getCreateRequest().getOcl()
                .getDeployment()
                .getVariables()) {
            if (variable.getKind() == DeployVariableKind.ENV) {
                if (request.containsKey(variable.getName())) {
                    variables.put(variable.getName(), request.get(variable.getName()));
                } else {
                    variables.put(variable.getName(), System.getenv(variable.getName()));
                }
            }
            if (variable.getKind() == DeployVariableKind.FIX_ENV) {
                variables.put(variable.getName(), request.get(variable.getValue()));
            }
        }
        return variables;
    }
}
