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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.database.service.DeployResourceEntity;
import org.eclipse.xpanse.modules.monitor.providers.huawei.models.HuaweiMonitorValue;
import org.eclipse.xpanse.modules.monitor.providers.huawei.models.HuaweiMonitorResponse;
import org.eclipse.xpanse.modules.monitor.MonitorResourceTypeEnum;
import org.eclipse.xpanse.modules.monitor.providers.huawei.common.HuaweiMonitorConstant;

/**
 * Resource conversion.
 */
@Slf4j
public class HuaweiMonitorHttpConvert {

    public static final int PERIOD = 300;
    public static final int TIME_GRANULARITY = 5 * 60 * 1000;

    public static final String UNIT = "%";
    public static final String REGION = "region";
    public static final String AVERAGE = "average";
    public static final String DIM0_PREFIX = "instance_id,";
    public static final String SIMPLE_DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * resource conversion. deployResourceEntity, monitorAgentEnabled, resourceType,fromTime,
     * toTime
     *
     * @return HuaweiMonitorReqParams.
     */
    public static ShowMetricDataRequest convertRequest(DeployResourceEntity deployResourceEntity,
            Boolean monitorAgentEnabled, String resourceType, String fromTime, String toTime) {

        String nameSpace;
        String metricName = null;
        if (monitorAgentEnabled) {
            nameSpace = HuaweiMonitorConstant.AGT_ECS;
            if (MonitorResourceTypeEnum.CPU.name().equals(resourceType)) {
                metricName = HuaweiMonitorConstant.CPU_USAGE;
            } else if (MonitorResourceTypeEnum.MEM.name().equals(resourceType)) {
                metricName = HuaweiMonitorConstant.MEM_USED_PERCENT;
            }
        } else {
            nameSpace = HuaweiMonitorConstant.SYS_ECS;
            if (MonitorResourceTypeEnum.CPU.name().equals(resourceType)) {
                metricName = HuaweiMonitorConstant.CPU_UTIL;
            } else if (MonitorResourceTypeEnum.MEM.name().equals(resourceType)) {
                metricName = HuaweiMonitorConstant.MEM_UTIL;
            }
        }
        if (StringUtils.isBlank(toTime) || StringUtils.isBlank(toTime)) {
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
        }
        ShowMetricDataRequest request = new ShowMetricDataRequest()
                .withDim0(DIM0_PREFIX + deployResourceEntity.getResourceId())
                .withFilter(FilterEnum.valueOf(AVERAGE))
                .withPeriod(PERIOD)
                .withFrom(Long.valueOf(fromTime))
                .withTo(Long.valueOf(toTime))
                .withNamespace(nameSpace).withMetricName(metricName);
        return request;
    }

    /**
     * resource conversion.
     *
     * @return MonitorDataResponse.
     */
    public static HuaweiMonitorResponse convertResponse(ShowMetricDataResponse response,
            String id) {
        HuaweiMonitorResponse huaweiMonitorResponse = new HuaweiMonitorResponse();
        List<HuaweiMonitorValue> huaweiMonitorValues = new ArrayList<>();
        HuaweiMonitorValue huaweiMonitorValue = new HuaweiMonitorValue();
        for (Datapoint datapoint : response.getDatapoints()) {
            huaweiMonitorValue.setAverage(datapoint.getAverage());
            huaweiMonitorValue.setUnit(UNIT);
            huaweiMonitorValues.add(huaweiMonitorValue);
        }
        huaweiMonitorResponse.setResourceId(id);
        huaweiMonitorResponse.setHuaweiMonitorValues(huaweiMonitorValues);
        return huaweiMonitorResponse;
    }

}
