/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.providers.huawei.models;

import lombok.Data;

/**
 * Data model.
 */
@Data
public class HuaweiMonitorValue {

    /**
     * The average of the monitor data.
     */
    private Double average;

    /**
     * The unit.
     */
    private String unit;

}
