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

package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.SampleServerConfigs;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.CA_TORONTO_1;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.apacheHttpServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.mysqlServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.nginxServer;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class InfrastructureTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Inject
    ServerService serverService;

    int testId = generateTestId();

    private static int generateTestId() {
        return new Random(seed()).nextInt() % 1_000;
    }

    private static long seed() {
        return new Date().getTime();
    }

    private String name(String value) {
        return String.format("%s_%s", value, testId);
    }

    @Test
    public void testInfrastructure() throws Exception {
        checkInfrastructure(
            initConfig(
                CA_TORONTO_1,
                US_CENTRAL_SALT_LAKE_CITY
            )
        );
    }

    private List<GroupMetadata> checkInfrastructure(InfrastructureConfig... configs) {
        List<GroupMetadata> groups = defineInfrastructure(configs);

        Arrays.asList(configs).stream()
            .forEach(config ->
                groups.stream()
                    .forEach(group ->
                        config.getSubitems().stream()
                            .forEach(cfg -> checkGroup(group, cfg))
                    )
            );
        return groups;
    }

    private InfrastructureConfig initConfig(DataCenter... dataCenters) {
        return new InfrastructureConfig()
            .dataCenters(dataCenters)
            .subitems(new GroupHierarchyConfig()
                .name(name("Parent Group"))
                .subitems(
                    group(name("Group1-1")).subitems(
                        group(name("Group1-1-1")).subitems(
                            mysqlServer().count(2)
                        ),
                        group(name("Group1-1-2")).subitems(
                            group(name("Group1-1-2-1")),
                            apacheHttpServer()
                        )
                    ),
                    group(name("Group1-2"))
                ));
    }

    private List<GroupMetadata> defineInfrastructure(InfrastructureConfig... config) {
        List<Group> groups = groupService.defineInfrastructure(config).waitUntilComplete().getResult();

        return groupService.find(new GroupFilter().groups(groups.toArray(new Group[groups.size()])));
    }

    private void checkGroup(GroupMetadata groupMetadata, GroupHierarchyConfig hierarchyConfig) {
        if (hierarchyConfig == null) {
            return;
        }
        List<String> metadataNames = groupMetadata.getGroups().stream()
            .map(GroupMetadata::getName)
            .collect(toList());

        List<String> configNames = hierarchyConfig.getSubitems().stream()
            .filter(cfg -> cfg instanceof GroupHierarchyConfig)
            .map(cfg -> ((GroupHierarchyConfig) cfg).getName())
            .filter(notNull())
            .collect(toList());

        assertTrue(metadataNames.containsAll(configNames), "group must have all groups from config");

        groupMetadata.getGroups().stream()
            .forEach(group -> {
                GroupHierarchyConfig nextConfig = (GroupHierarchyConfig)hierarchyConfig.getSubitems().stream()
                    .filter(cfg -> cfg instanceof GroupHierarchyConfig)
                    .filter(subgroup -> ((GroupHierarchyConfig)subgroup).getName().equals(group.getName()))
                    .findFirst()
                    .orElse(null);
                checkGroup(group, nextConfig);
            });
    }

    @AfterClass
    private void deleteGroups() throws Exception {
        serverService
            .delete(new ServerFilter()
                .dataCenters(CA_TORONTO_1, US_CENTRAL_SALT_LAKE_CITY)
                .groupNames(name("Group1-1-1"), name("Group1-1-2"))
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter()
                .dataCenters(CA_TORONTO_1, US_CENTRAL_SALT_LAKE_CITY)
                .names(name("Parent Group"))
            )
            .waitUntilComplete();
    }
}