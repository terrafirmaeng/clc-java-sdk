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

package com.centurylink.cloud.sdk.server.services.dsl.domain.group;

import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupConfig {
    private String name;
    private String description;
    private Group parentGroup;
    private List<CustomField> customFields = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupConfig name(String name) {
        setName(name);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupConfig description(String description) {
        setDescription(description);
        return this;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public GroupConfig parentGroup(Group parentGroup) {
        setParentGroup(parentGroup);
        return this;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        checkNotNull(customFields, "List of custom fields must be not a null");
        this.customFields = customFields;
    }

    public GroupConfig customFields(List<CustomField> customFields) {
        setCustomFields(customFields);
        return this;
    }

    public GroupConfig customFields(CustomField... customFields) {
        checkNotNull(customFields, "List of custom fields must be not a null");
        this.customFields.addAll(Arrays.asList(customFields));
        return this;
    }
}
