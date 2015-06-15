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

package com.centurylink.cloud.sdk.servers.services.servers.operations;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.ServerStubFixture;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;


public class ServerOperationsByGroupsTest extends AbstractServerOperationsStubTest {

    @Inject @Mock
    ServerClient serverClient;

    @Inject @Mock
    QueueClient queueClient;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Override
    protected void powerOnServer() {
        groupService
            .powerOn(group)
            .waitUntilComplete();
    }

    @Override
    protected void powerOffServer() {
        groupService
            .powerOff(group)
            .waitUntilComplete();
    }

    @Override
    protected void pauseServer() {
        groupService
            .pause(group)
            .waitUntilComplete();
    }

    @Override
    protected void shutDownServer() {
        groupService
            .shutDown(group)
            .waitUntilComplete();
    }

    @Override
    protected void stopServerMaintenance() {
        groupService
            .stopMaintenance(group)
            .waitUntilComplete();
    }

    @Override
    protected void startServerMaintenance() {
        groupService
            .startMaintenance(group)
            .waitUntilComplete();
    }

    @Override
    protected void archiveServer() {
        groupService
            .archive(group)
            .waitUntilComplete();
    }

    @Override
    protected void createServerSnapshot() {
        groupService
            .createSnapshot(1, group)
            .waitUntilComplete();
    }

    @Override
    protected void restoreServer(Group group, Server server) {
        serverService
            .restore(server, group)
            .waitUntilComplete();
    }

    @Override
    protected void resetServer() {
        groupService
            .reset(group)
            .waitUntilComplete();
    }

    @Override
    protected void rebootServer() {
        groupService
            .reboot(group)
            .waitUntilComplete();
    }

    @Override
    @Test(groups = {INTEGRATION})
    public void runChainTests() {
        ServerStubFixture fixture = new ServerStubFixture(serverClient, queueClient, dataCentersClient);

        ServerMetadata serverMetadata1 = fixture.getServerMetadata();
        ServerMetadata serverMetadata2 = fixture.getAnotherServerMetadata();

        server1 = serverMetadata1.asRefById();
        server2 = serverMetadata2.asRefById();

        group = Group.refById(serverMetadata1.getGroupId());

        testArchive();
        fixture.activateServers();
    }
}