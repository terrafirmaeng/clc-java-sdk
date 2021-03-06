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

package com.centurylink.cloud.sdk.server.services.dsl.groups.stats;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.GroupByIdRef;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class GetServerMonitoringStatsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    GroupByIdRef group;

    @BeforeMethod
    public void setUp() {
        List<GroupMetadata> metadata = groupService.find(new GroupFilter()
            .dataCenters(DataCenter.DE_FRANKFURT)
            .nameContains(Group.DEFAULT_GROUP));

        assertTrue(metadata.size() > 0);
        group = metadata.get(0).asRefById();
    }

    @Test
    public void testServerStats() {
        Duration sampleInterval = Duration.ofHours(1);
        GroupFilter groupFilter = new GroupFilter().groups(group);
        List<ServerMonitoringStatistics> result = groupService.getMonitoringStats(
            groupFilter,
            new ServerMonitoringFilter()
                .from(OffsetDateTime.now().minusDays(2))
        );

        assertNotNull(result);

        result.stream()
            .forEach(metadata -> {
                for (int i = 0; i < metadata.getStats().size() - 1; i++) {
                    SamplingEntry curStats = metadata.getStats().get(i);
                    SamplingEntry nextStats = metadata.getStats().get(i + 1);
                    assertEquals(Duration.between(curStats.getTimestamp(), nextStats.getTimestamp()), sampleInterval);

                    checkStats(curStats);
                }
            });
    }

    private void checkStats(SamplingEntry stats) {
        boolean isEmpty = stats.getCpu().equals(0);

        checkStat(stats.getCpuPercent(), isEmpty);
        checkStat(stats.getMemoryPercent(), isEmpty);
        checkStat(stats.getNetworkReceivedKbps(), isEmpty);
        checkStat(stats.getNetworkTransmittedKbps(), isEmpty);
        checkStat(stats.getDiskUsageTotalCapacityMB(), isEmpty);
        checkStat(stats.getMemoryMB(), isEmpty);
        checkStat(stats.getDiskUsage().size(), isEmpty);
        checkStat(stats.getGuestDiskUsage().size(), isEmpty);
    }

    private void checkStat(Double value, boolean isEmpty) {
        Double zero = 0d;
        if (isEmpty) {
            assertTrue(value == zero);
        }
    }

    private void checkStat(Integer value, boolean isEmpty) {
        Integer zero = 0;
        assertTrue(isEmpty ? value == zero : value >= zero);
    }
}
