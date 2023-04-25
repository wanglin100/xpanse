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
 * Service Info of the Xpanse Deployed Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorServiceInfo {

    private String name;
    private List<ServiceInfo> serviceInfoList;
}


