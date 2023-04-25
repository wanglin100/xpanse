/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor.providers.huawei.utils;

import com.huaweicloud.sdk.ces.v1.CesClient;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataRequest;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataResponse;
import com.huaweicloud.sdk.ces.v1.region.CesRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import org.springframework.stereotype.Component;

/**
 * HuaweiCloudMonitor Client Util.
 */
@Component
public class HuaweiMonitorClientUtil {

    /**
     * Get Huawei Monitor ICredential.
     *
     * @param ak huawei ak.
     * @param sk huawei sk.
     * @return
     */
    public ICredential getICredential(String ak, String sk) {
        return new BasicCredentials()
                .withAk(ak).withSk(sk);
    }

    /**
     * Get HuaweiCloud Monitor Client.
     *
     * @param iCredential ICredential.
     * @param region      regionName.
     * @return
     */
    public CesClient getCesClient(ICredential iCredential, String region) {
        CesClient client = CesClient.newBuilder()
                .withCredential(iCredential)
                .withRegion(CesRegion.valueOf(region))
                .build();
        return client;
    }


    /**
     * Get Huawei Monitor Data.
     *
     * @param cesClient HuaweiCloud Monitor Client.
     * @param request   ShowMetricDataRequest.
     * @return
     */
    public ShowMetricDataResponse showMetricData(CesClient cesClient,
            ShowMetricDataRequest request) {
        return cesClient.showMetricData(request);
    }
}
