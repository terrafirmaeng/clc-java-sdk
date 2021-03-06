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

package com.centurylink.cloud.sdk.server.services.client.domain.group;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.server.services.client.domain.ChangeInfo;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.GroupByIdRef;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "name",
    "description",
    "locationId",
    "type",
    "status",
    "serversCount",
    "groups",
    "links",
    "changeInfo",
    "customFields"
})
public class GroupMetadata {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("locationId")
    private String locationId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("serversCount")
    private Integer serversCount;
    @JsonProperty("groups")
    private List<GroupMetadata> groups = new ArrayList<GroupMetadata>();
    @JsonProperty("servers")
    private List<ServerMetadata> servers = new ArrayList<>();
    @JsonProperty("links")
    private List<Link> links = new ArrayList<Link>();
    @JsonProperty("changeInfo")
    private ChangeInfo changeInfo;
    @JsonProperty("customFields")
    private List<CustomField> customFields = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public GroupMetadata id(String id) {
        setId(id);
        return this;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The locationId
     */
    @JsonProperty("locationId")
    public String getLocationId() {
        return locationId;
    }

    /**
     *
     * @param locationId
     * The locationId
     */
    @JsonProperty("locationId")
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The serversCount
     */
    @JsonProperty("serversCount")
    public Integer getServersCount() {
        return serversCount;
    }

    /**
     *
     * @param serversCount
     * The serversCount
     */
    @JsonProperty("serversCount")
    public void setServersCount(Integer serversCount) {
        this.serversCount = serversCount;
    }

    /**
     *
     * @return
     * The groups
     */
    @JsonProperty("groups")
    public List<GroupMetadata> getGroups() {
        return groups;
    }

    /**
     *
     * @param groups
     * The groups
     */
    @JsonProperty("groups")
    public void setGroups(List<GroupMetadata> groups) {
        this.groups = groups;
    }

    @JsonProperty("servers")
    public List<ServerMetadata> getServers() {
        return servers;
    }

    @JsonProperty("servers")
    public void setServers(List<ServerMetadata> servers) {
        this.servers = servers;
    }

    /**
     *
     * @return
     * The links
     */
    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    /**
     *
     * @param links
     * The links
     */
    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     *
     * @return
     * The changeInfo
     */
    @JsonProperty("changeInfo")
    public ChangeInfo getChangeInfo() {
        return changeInfo;
    }

    /**
     *
     * @param changeInfo
     * The changeInfo
     */
    @JsonProperty("changeInfo")
    public void setChangeInfo(ChangeInfo changeInfo) {
        this.changeInfo = changeInfo;
    }

    /**
     *
     * @return
     * The customFields
     */
    @JsonProperty("customFields")
    public List<CustomField> getCustomFields() {
        return customFields;
    }

    /**
     *
     * @param customFields
     * The customFields
     */
    @JsonProperty("customFields")
    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public GroupMetadata findGroupByName(String name) {
        return findGroupByName(groups, name);
    }

    public GroupMetadata findGroupByName(List<GroupMetadata> groups, String name) {
        if (getName().equals(name)) {
            return this;
        }

        for (GroupMetadata curGroup : groups) {
            if (curGroup.getName().equals(name)) {
                return curGroup;
            } else if (curGroup.getGroups().size() > 0
                    && findGroupByName(curGroup.getGroups(), name) != null) {
                return findGroupByName(curGroup.getGroups(), name);
            }
        }

        return null;
    }

    @JsonIgnore
    public List<GroupMetadata> getAllGroups() {
        return
            concat(
                Stream
                    .of(this),

                getGroups()
                    .stream()
                    .flatMap(group ->
                        group.getAllGroups().stream()
                    )
            )
            .collect(toList());
    }

    @JsonIgnore
    public List<ServerMetadata> getAllServers() {
        List<ServerMetadata> allServers = concat(
            getServers()
                .stream(),

            getGroups()
                .stream()
                .flatMap(group ->
                        group.getAllServers().stream()
                )
        )
        .collect(toList());

        List<String> serverIds = allServers.stream()
            .map(ServerMetadata::getId)
            .distinct()
            .collect(toList());

        return allServers.stream()
            .filter(srv -> serverIds.contains(srv.getId()))
            .collect(toList());
    }

    @JsonIgnore
    public String getParentGroupId() {
        return this.getLinks().stream()
                .filter(link -> "parentGroup".equals(link.getRel()))
                .map(Link::getId)
                .findFirst().orElse(null);
    }

    public GroupByIdRef asRefById() {
        return Group.refById(id);
    }

}
