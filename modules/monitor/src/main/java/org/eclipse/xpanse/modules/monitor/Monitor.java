/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor;

import java.util.List;
import org.eclipse.xpanse.modules.database.service.DeployServiceEntity;
import org.eclipse.xpanse.modules.models.enums.Csp;
import org.eclipse.xpanse.modules.monitor.model.MonitorMetricResponse;

/**
 * This interface describes the cpuUsage and memUsage for querying the server.
 */
public interface Monitor {

    /**
     * get the Csp of the monitor.
     */
    Csp getCsp();

    /**
     * Method to git service cpuUsage.
     *
     * @param deployServiceEntity the resource of the deployment.
     * @param fromTime            the start time of the monitor.
     * @param toTime              the start time of the monitor.
     * @param monitorType         the monitor type.
     */
    List<MonitorMetricResponse> getUsage(DeployServiceEntity deployServiceEntity, Long fromTime
            , Long toTime, String monitorType);

}
