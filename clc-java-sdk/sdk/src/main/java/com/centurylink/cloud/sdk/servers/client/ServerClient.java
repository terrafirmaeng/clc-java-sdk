package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.ClcApiConstants;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.servers.client.domain.GetDataCenterResponse;
import com.centurylink.cloud.sdk.servers.client.domain.GetStatusResponse;
import com.centurylink.cloud.sdk.servers.client.domain.datacenter.deployment.capabilities.GetDeploymentCapabilitiesResponse;
import com.centurylink.cloud.sdk.servers.client.domain.group.GetGroupResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.GetServerResult;
import com.google.inject.Inject;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static com.centurylink.cloud.sdk.core.client.ClcApiConstants.CLC_API_URL;
import static javax.ws.rs.client.Entity.entity;

/**
 * @author ilya.drabenia
 */
public class ServerClient {
    private final BearerAuthentication authentication;

    @Inject
    public ServerClient(BearerAuthentication authFilter) {
        this.authentication = authFilter;
    }

    /**
     * Creates a new server. Calls to this operation must include a token acquired
     * from the authentication endpoint. See the Login API for information on acquiring
     * this token.
     */
    public CreateServerResponse create(CreateServerCommand request) {
        return
            client("/servers/{accountAlias}")
                .request().post(
                    entity(request, MediaType.APPLICATION_JSON_TYPE)
                )
                .readEntity(CreateServerResponse.class);
    }

    public String delete(String serverId) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", serverId)
                .request()
                .delete(String.class);
    }

    public GetServerResult findServerByUuid(String uuid) {
        return
            client("/servers/{accountAlias}/{serverId}?uuid=true")
                .resolveTemplate("serverId", uuid)
                .request()
                .get(GetServerResult.class);
    }

    public GetDataCenterResponse getDataCenter(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDataCenterResponse.class);
    }

    public GetGroupResponse getGroup(String rootGroupId) {
        return
            client("/groups/{accountAlias}/{rootGroupId}")
                .resolveTemplate("rootGroupId", rootGroupId)
                .request().get(GetGroupResponse.class);
    }

    public GetDeploymentCapabilitiesResponse getDataCenterDeploymentCapabilities(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDeploymentCapabilitiesResponse.class);
    }

    public GetStatusResponse getJobStatus(String jobId) {
        return
            client("/operations/{accountAlias}/status/{statusId}")
                .resolveTemplate("statusId", jobId)
                .request()
                .get(GetStatusResponse.class);
    }

    private WebTarget client(String target) {
        return
            ClcApiConstants
                .CLIENT
                    .register(authentication)
                    .target(CLC_API_URL + target)
                    .resolveTemplate("accountAlias", authentication.getAccountAlias());
    }
}