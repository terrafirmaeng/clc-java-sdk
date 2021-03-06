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

package com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "description",
        "labProductCode",
        "premiumProductCode",
        "type"
})
public class ImportableOSType {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("labProductCode")
    private String labProductCode;
    @JsonProperty("premiumProductCode")
    private String premiumProductCode;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
     * The labProductCode
     */
    @JsonProperty("labProductCode")
    public String getLabProductCode() {
        return labProductCode;
    }

    /**
     *
     * @param labProductCode
     * The labProductCode
     */
    @JsonProperty("labProductCode")
    public void setLabProductCode(String labProductCode) {
        this.labProductCode = labProductCode;
    }

    /**
     *
     * @return
     * The premiumProductCode
     */
    @JsonProperty("premiumProductCode")
    public String getPremiumProductCode() {
        return premiumProductCode;
    }

    /**
     *
     * @param premiumProductCode
     * The premiumProductCode
     */
    @JsonProperty("premiumProductCode")
    public void setPremiumProductCode(String premiumProductCode) {
        this.premiumProductCode = premiumProductCode;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}