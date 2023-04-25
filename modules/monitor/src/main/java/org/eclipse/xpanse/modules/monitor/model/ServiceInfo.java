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
 * ServiceId and Service Instance Name Of The Deployed Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfo {

    private String id;
    private String customerServiceName;
}
