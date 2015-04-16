package com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "networkId",
    "type",
    "accountID"
})
public class NetworkMetadata {

    @JsonProperty("name")
    private String name;
    @JsonProperty("networkId")
    private String networkId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("accountID")
    private String accountID;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public NetworkMetadata name(String name) {
        setName(name);
        return this;
    }

    /**
     *
     * @return
     * The networkId
     */
    @JsonProperty("networkId")
    public String getNetworkId() {
        return networkId;
    }

    /**
     *
     * @param networkId
     * The networkId
     */
    @JsonProperty("networkId")
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public NetworkMetadata networkId(String networkId) {
        setNetworkId(networkId);
        return this;
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
     * The accountID
     */
    @JsonProperty("accountID")
    public String getAccountID() {
        return accountID;
    }

    /**
     *
     * @param accountID
     * The accountID
     */
    @JsonProperty("accountID")
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}