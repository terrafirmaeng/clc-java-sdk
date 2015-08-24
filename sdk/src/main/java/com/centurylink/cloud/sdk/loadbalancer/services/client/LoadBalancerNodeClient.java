/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.loadbalancer.services.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.google.inject.Inject;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class LoadBalancerNodeClient extends AuthenticatedSdkHttpClient {

    @Inject
    public LoadBalancerNodeClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<LoadBalancerNodeMetadata> getLoadBalancerNodes(
            String dataCenterId,
            String loadBalancerId,
            String loadBalancerPoolId
    ) {
        List<LoadBalancerNodeMetadata> metadataList =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools/{loadBalancerPoolId}/nodes")
                .resolveTemplate("dataCenterId", dataCenterId)
                .resolveTemplate("loadBalancerId", loadBalancerId)
                .resolveTemplate("loadBalancerPoolId", loadBalancerPoolId)
                .request()
                .get(new GenericType<List<LoadBalancerNodeMetadata>>(){});

        metadataList.forEach(metadata -> setAdditionalParams(
                metadata, dataCenterId, loadBalancerId, loadBalancerPoolId
        ));

        return metadataList;
    }

    public void update(String dataCenterId, String loadBalancerId, String poolId, List<LoadBalancerNodeMetadata> request) {
        client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools/{poolId}/nodes")
            .resolveTemplate("dataCenterId", dataCenterId)
            .resolveTemplate("loadBalancerId", loadBalancerId)
            .resolveTemplate("poolId", poolId)
            .request()
            .put(entity(request, APPLICATION_JSON_TYPE));
    }

    private void setAdditionalParams(
            LoadBalancerNodeMetadata metadata,
            String dataCenterId,
            String loadBalancerId,
            String loadBalancerPoolId
    ) {
        metadata.setDataCenterId(dataCenterId);
        metadata.setLoadBalancerId(loadBalancerId);
        metadata.setLoadBalancerPoolId(loadBalancerPoolId);
    }

}
