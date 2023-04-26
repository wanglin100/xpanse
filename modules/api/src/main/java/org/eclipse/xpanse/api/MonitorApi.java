/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */
package org.eclipse.xpanse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.models.enums.ServiceState;
import org.eclipse.xpanse.modules.models.view.ServiceVo;
import org.eclipse.xpanse.modules.monitor.model.MonitorMetricResponse;
import org.eclipse.xpanse.modules.monitor.model.MonitorRequestParams;
import org.eclipse.xpanse.modules.monitor.model.MonitorServiceInfo;
import org.eclipse.xpanse.modules.monitor.model.ServiceInfo;
import org.eclipse.xpanse.orchestrator.OrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Monitor Api
 */
@Slf4j
@RestController
@RequestMapping("/xpanse")
@CrossOrigin
public class MonitorApi {

    private final OrchestratorService orchestratorService;

    @Autowired
    public MonitorApi(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @Tag(name = "Monitor",
            description = "APIs to Monitor ServiceInfo.")
    @Operation(description = "Get Monitor ServiceInfo.")
    @GetMapping(value = "/monitor/serviceInfo",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MonitorServiceInfo> getMonitorServiceInfo() {
        Map<String, List<ServiceVo>> nameServiceInfoMap = getServiceInfoMapGroupByName();
        List<MonitorServiceInfo> monitorServiceInfoList = new ArrayList<>();
        nameServiceInfoMap.forEach((nameKey, nameValue) -> {
            Map<String, List<ServiceVo>> versionServiceInfoMap = nameValue.stream()
                    .collect(Collectors.groupingBy(ServiceVo::getVersion));
            versionServiceInfoMap.forEach((versionKey, versionValue) -> {
                String monitorServiceName = new StringBuilder().append(nameKey).append("(")
                        .append(versionKey).append(")").toString();
                List<ServiceInfo> serviceInfoList = new ArrayList<>();
                for (ServiceVo serviceVo : versionValue) {
                    serviceInfoList.add(
                            new ServiceInfo(serviceVo.getId().toString(),
                                    serviceVo.getCustomerServiceName()));
                }
                monitorServiceInfoList.add(
                        new MonitorServiceInfo(monitorServiceName, serviceInfoList));
            });
        });
        return monitorServiceInfoList;
    }


    @Tag(name = "Monitor",
            description = "APIs to Monitor ServiceInfo.")
    @Operation(description = "Get Monitor ServiceInfo.")
    @GetMapping(value = "/monitor/metric",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MonitorMetricResponse> getMonitorMetric(
            @Parameter(name = "id", description = "Id of the deploy service")
            @RequestParam(name = "id", required = true) String id,
            @Parameter(name = "monitorType", description = "Monitor resource type")
            @RequestParam(name = "monitorType", required = true) String monitorType) {
        if (Objects.isNull(id) || StringUtils.isBlank(monitorType)) {
            log.error("Parameters Invalid,id: {},getMonitorType: {}", id, monitorType);
            throw new IllegalArgumentException("Parameters Invalid");
        }
        MonitorRequestParams monitorRequestParams = new MonitorRequestParams(UUID.fromString(id),
                monitorType);
        List<MonitorMetricResponse> monitorMetricResponseList =
                orchestratorService.getMonitorMetric(monitorRequestParams);
        log.info(String.format("Get Monitor metric with params %s "
                + "success.", monitorRequestParams));
        return monitorMetricResponseList;
    }

    private Map<String, List<ServiceVo>> getServiceInfoMapGroupByName() {
        List<ServiceVo> serviceVos = orchestratorService.listDeployServices().stream()
                .filter(serviceVo -> ServiceState.DEPLOY_SUCCESS.name()
                        .equalsIgnoreCase(serviceVo.getServiceState().name())
                ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(serviceVos)) {
            log.warn("The list of successfully deployed services is empty");
            return null;
        }
        return serviceVos.stream().collect(Collectors.groupingBy(ServiceVo::getName));
    }


}
