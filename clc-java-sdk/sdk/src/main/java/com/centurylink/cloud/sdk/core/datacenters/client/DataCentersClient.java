package com.centurylink.cloud.sdk.core.datacenters.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.ClcClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.GetDeploymentCapabilitiesResponse;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class DataCentersClient extends ClcClient {

    @Inject
    public DataCentersClient(BearerAuthentication authFilter) {
        super(authFilter);
    }

    public GetDeploymentCapabilitiesResponse getDataCenterDeploymentCapabilities(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDeploymentCapabilitiesResponse.class);
    }

    public GetDataCenterResponse getDataCenter(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDataCenterResponse.class);
    }


    public GetDataCenterListResponse findAllDataCenters() {
        return
            client("/datacenters/{accountAlias}?groupLinks=true")
                .request()
                .get(GetDataCenterListResponse.class);
    }

}
