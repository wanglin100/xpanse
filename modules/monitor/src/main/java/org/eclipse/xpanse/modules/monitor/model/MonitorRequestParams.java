/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Parameters of the GET Monitor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorRequestParams {

    private UUID id;
    private String monitorType;
}
