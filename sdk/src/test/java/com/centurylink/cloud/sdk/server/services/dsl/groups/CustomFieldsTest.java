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

package com.centurylink.cloud.sdk.server.services.dsl.groups;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class CustomFieldsTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    GroupService groupService;

    GroupMetadata groupMetadata;
    Group groupRef;

    @Test
    @WireMockFileSource("custom-fields/create")
    public void testCreate() {
        groupRef =
            groupService.create(new GroupConfig()
                .parentGroup(Group.refByName(DataCenter.DE_FRANKFURT, Group.DEFAULT_GROUP))
                .name("CSTM")
                .customFields(
                    new CustomField().name("Approved by").value("test user"),
                    new CustomField().name("Type").value("1")
                )
            )
            .waitUntilComplete()
            .getResult();

        groupMetadata = groupService.findByRef(groupRef);

        List<CustomField> customFields = groupMetadata.getCustomFields();
        assert customFields.size() == 2;

        customFields.forEach(field -> {
            if (field.getName().equals("Type")) {
                assert field.getValue().equals("1");
            } else {
                assert field.getValue().equals("test user");
            }
        });

    }

    @Test
    @WireMockFileSource("custom-fields/modify")
    public void testModify() {
        groupService.modify(groupRef, new GroupConfig()
            .customFields(
                new CustomField().name("Approved by").value("user")
            ))
            .waitUntilComplete();

        groupMetadata = groupService.findByRef(groupRef);

        List<CustomField> customFields = groupMetadata.getCustomFields();
        assert customFields.size() == 1;

        assert customFields.get(0).getValue().equals("user");

        deleteGroup();
    }

    private void deleteGroup() {
        groupService.delete(groupRef);
    }

}
