/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.providers.huawei.models;

import java.util.List;
import lombok.Data;

/**
 * MonitorDataResponse model.
 */
@Data
public class HuaweiMonitorResponse {

    /**
     * The ID of the deployed resource.
     */
    private String resourceId;

    private List<HuaweiMonitorValue> huaweiMonitorValues;

}
