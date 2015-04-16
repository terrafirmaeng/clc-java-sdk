package com.centurylink.cloud.sdk.core.commons.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.BaseSdkClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.GetDeploymentCapabilitiesResponse;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class DataCentersClient extends BaseSdkClient {

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

    public DataCenterMetadata getDataCenter(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(DataCenterMetadata.class);
    }

    // TODO: need to implement memoization of this method with acceptable expiration time
    public GetDataCenterListResponse findAllDataCenters() {
        return
            client("/datacenters/{accountAlias}?groupLinks=true")
                .request()
                .get(GetDataCenterListResponse.class);
    }

}