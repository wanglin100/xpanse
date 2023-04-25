/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.model;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response of the Get Monitor Metric.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorMetricResponse {

    private String vmId;
    private String vmName;
    private String monitorType;
    private List<MonitorMetric> monitorMetrics;

}
