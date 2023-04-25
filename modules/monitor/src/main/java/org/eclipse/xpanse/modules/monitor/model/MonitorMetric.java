/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Monitor Metric.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorMetric {

    private String metric;
    private String timeStamp;

}
