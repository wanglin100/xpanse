/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.providers.huawei.utils;

import com.huaweicloud.sdk.ces.v1.model.Datapoint;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataRequest;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataRequest.FilterEnum;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.database.service.DeployResourceEntity;
import org.eclipse.xpanse.modules.monitor.MonitorResourceTypeEnum;
import org.eclipse.xpanse.modules.monitor.model.MonitorMetric;
import org.eclipse.xpanse.modules.monitor.model.MonitorMetricResponse;
import org.eclipse.xpanse.modules.monitor.providers.huawei.common.HuaweiMonitorConstant;

/**
 * Resource conversion.
 */
@Slf4j
public class HuaweiMonitorHttpConvert {

    public static final int PERIOD = 3600;

    public static final String UNIT = "%";
    public static final String AVERAGE = "average";
    public static final String DIM0_PREFIX = "instance_id,";

    /**
     * resource conversion. deployResourceEntity, monitorAgentEnabled, resourceType,fromTime,
     * toTime
     *
     * @return HuaweiMonitorReqParams.
     */
    public static ShowMetricDataRequest convertRequest(DeployResourceEntity deployResourceEntity,
            Boolean monitorAgentEnabled, String monitorType, Long fromTime, Long toTime) {

        String nameSpace;
        String metricName = null;
        if (monitorAgentEnabled) {
            nameSpace = HuaweiMonitorConstant.AGT_ECS;
            if (MonitorResourceTypeEnum.CPU.toValue().equals(monitorType)) {
                metricName = HuaweiMonitorConstant.CPU_USAGE;
            } else if (MonitorResourceTypeEnum.MEM.toValue().equals(monitorType)) {
                metricName = HuaweiMonitorConstant.MEM_USED_PERCENT;
            }
        } else {
            nameSpace = HuaweiMonitorConstant.SYS_ECS;
            if (MonitorResourceTypeEnum.CPU.toValue().equals(monitorType)) {
                metricName = HuaweiMonitorConstant.CPU_UTIL;
            } else if (MonitorResourceTypeEnum.MEM.toValue().equals(monitorType)) {
                metricName = HuaweiMonitorConstant.MEM_UTIL;
            }
        }
        /*if (StringUtils.isBlank(toTime) || StringUtils.isBlank(toTime)) {
            fromTime = String.valueOf(new Date().getTime() - TIME_GRANULARITY);
            toTime = String.valueOf(new Date().getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATA_FORMAT);
            try {
                fromTime = String.valueOf(sdf.parse(fromTime).getTime());
                toTime = String.valueOf(sdf.parse(toTime).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(
                        String.format("Exception: %s.", e.getMessage()));
            }
        }*/
        ShowMetricDataRequest request = new ShowMetricDataRequest()
                .withDim0(DIM0_PREFIX + deployResourceEntity.getResourceId())
                .withFilter(FilterEnum.valueOf(AVERAGE))
                .withPeriod(PERIOD)
                .withFrom(fromTime)
                .withTo(toTime)
                .withNamespace(nameSpace).withMetricName(metricName);
        return request;
    }

    /**
     * resource conversion.
     *
     * @return MonitorDataResponse.
     */
    public static MonitorMetricResponse convertResponse(ShowMetricDataResponse response,
            String id, String name, String monitorType) {
        MonitorMetricResponse monitorMetricResponse = new MonitorMetricResponse();
        List<MonitorMetric> monitorMetricList = new ArrayList<>();
        for (Datapoint datapoint : response.getDatapoints()) {
            MonitorMetric monitorMetric = new MonitorMetric();
            monitorMetric.setMetric(datapoint.getAverage() + UNIT);
            monitorMetric.setTimeStamp(DateUtils.format(new Date(datapoint.getTimestamp())));
            monitorMetricList.add(monitorMetric);
        }
        monitorMetricResponse.setVmId(id);
        monitorMetricResponse.setVmName(name);
        monitorMetricResponse.setMonitorType(monitorType);
        monitorMetricResponse.setMonitorMetrics(monitorMetricList);
        return monitorMetricResponse;
    }

}
