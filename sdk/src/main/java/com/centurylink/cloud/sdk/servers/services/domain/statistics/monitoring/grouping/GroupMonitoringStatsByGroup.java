package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMonitoringStatsByGroup extends GroupMonitoringStatsBy {

    private GroupService groupService;
    private ServerService serverService;
    private final boolean aggregateSubItems;

    public GroupMonitoringStatsByGroup(GroupService groupService,
                                       ServerService serverService,
                                       MonitoringStatsFilter statsFilter,
                                       boolean aggregateSubItems) {
        super(statsFilter);
        this.groupService = groupService;
        this.serverService = serverService;
        this.aggregateSubItems = aggregateSubItems;
    }

    @Override
    public List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> stats) {
        Map<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        stats.forEach((group, statsList) ->
            statsList.stream()
                .forEach(stat -> {
                    String key = serverService.findByRef(Server.refById(stat.getName())).getGroupId();
                    if (group.asFilter().getIds().contains(key) || aggregateSubItems)
                        collectStats(plainGroupMap,
                            key,
                            stat.getStats(),
                            false
                        );
                })
        );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(Map<String, List<MonitoringEntry>> plainGroupMap) {

        List<MonitoringStatsEntry> result = new ArrayList<>();
        plainGroupMap.forEach(
            (key, statistics) -> result.add(
                createMonitoringStatsEntry(groupService.findByRef(Group.refById(key)), statistics)
            )
        );

        return result;
    }
}