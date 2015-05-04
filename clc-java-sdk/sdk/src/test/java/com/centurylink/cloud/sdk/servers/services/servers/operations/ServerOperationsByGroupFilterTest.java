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


public class ServerOperationsByGroupFilterTest extends AbstractServerOperationsStubTest {

    @Inject @Mock
    ServerClient serverClient;

    @Inject @Mock
    QueueClient queueClient;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Override
    protected void powerOnServer() {
        groupService
            .powerOn(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void powerOffServer() {
        groupService
            .powerOff(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void pauseServer() {
        groupService
            .pause(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void shutDownServer() {
        groupService
            .shutDown(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void stopServerMaintenance() {
        groupService
            .stopMaintenance(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void startServerMaintenance() {
        groupService
            .startMaintenance(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void archiveServer() {
        groupService
            .archive(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void createServerSnapshot() {
        groupService
            .createSnapshot(1, groupFilter)
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
            .reset(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void rebootServer() {
        groupService
            .reboot(groupFilter)
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
        groupFilter = fixture.getGroupFilterById();

        testArchive();
        fixture.activateServers();
    }
}