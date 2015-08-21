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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerPoolClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerPoolRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerPoolFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class LoadBalancerPoolService implements QueryService<LoadBalancerPool, LoadBalancerPoolFilter, LoadBalancerPoolMetadata> {

    private final LoadBalancerPoolClient loadBalancerPoolClient;
    private final LoadBalancerService loadBalancerService;

    @Inject
    public LoadBalancerPoolService(
            LoadBalancerPoolClient loadBalancerPoolClient,
            LoadBalancerService loadBalancerService
    ) {
        this.loadBalancerPoolClient = loadBalancerPoolClient;
        this.loadBalancerService = loadBalancerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<LoadBalancerPoolMetadata> findLazy(LoadBalancerPoolFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<LoadBalancerMetadata> loadBalancers = loadBalancerService.findLazy(
                filter.getLoadBalancerFilter()
        );

        return
            loadBalancers
                .flatMap(loadBalancerMetadata -> loadBalancerPoolClient
                    .getLoadBalancerPools(loadBalancerMetadata.getDataCenterId(), loadBalancerMetadata.getId())
                    .stream()
                )
                .filter(filter.getPredicate())
                .filter(
                    filter.getIds().size() > 0 ?
                        combine(LoadBalancerPoolMetadata::getId, in(filter.getIds())) :
                        alwaysTrue()
                );
    }

    public OperationFuture<LoadBalancerPool> create(LoadBalancerPoolConfig config) {
        LoadBalancer loadBalancer = config.getLoadBalancer();
        LoadBalancerMetadata loadBalancerMetadata = loadBalancerService.findByRef(loadBalancer);

        LoadBalancerPoolMetadata metadata = loadBalancerPoolClient.create(
                loadBalancerMetadata.getDataCenterId(),
                loadBalancerMetadata.getId(),
                new LoadBalancerPoolRequest()
                        .port(config.getPort())
                        .method(config.getMethod())
                        .persistence(config.getPersistence())
        );

        return new OperationFuture<>(
                LoadBalancerPool.refById(metadata.getId(), loadBalancer),
                new NoWaitingJobFuture()
        );
    }

    public OperationFuture<LoadBalancerPool> update(LoadBalancerPool loadBalancerPool, LoadBalancerPoolConfig config) {
        LoadBalancerPoolMetadata loadBalancerPoolMetadata = findByRef(loadBalancerPool);

        loadBalancerPoolClient.update(
                loadBalancerPoolMetadata.getDataCenterId(),
                loadBalancerPoolMetadata.getLoadBalancerId(),
                loadBalancerPoolMetadata.getId(),
                new LoadBalancerPoolRequest()
                        .method(config.getMethod())
                        .persistence(config.getPersistence())
        );

        return new OperationFuture<>(
                loadBalancerPool,
                new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<LoadBalancerPool>> update(
            List<LoadBalancerPool> loadBalancerPoolList,
            LoadBalancerPoolConfig config
    ) {
        loadBalancerPoolList.forEach(pool -> update(pool, config));

        return new OperationFuture<>(
                loadBalancerPoolList,
                new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<LoadBalancerPool>> update(
            LoadBalancerPoolFilter loadBalancerPoolFilter,
            LoadBalancerPoolConfig config
    ) {
        checkNotNull(loadBalancerPoolFilter, "Load balancer pool filter must be not null");

        List<LoadBalancerPool> loadBalancerPoolList = findLazy(loadBalancerPoolFilter)
                .map(metadata -> LoadBalancerPool.refById(
                        metadata.getId(),
                        LoadBalancer.refById(
                                metadata.getLoadBalancerId(),
                                DataCenter.refById(metadata.getDataCenterId())
                        )
                ))
                .collect(toList());

        return update(loadBalancerPoolList, config);
    }

    public OperationFuture<LoadBalancerPool> delete(LoadBalancerPool loadBalancerPool) {
        LoadBalancerPoolMetadata loadBalancerPoolMetadata = findByRef(loadBalancerPool);

        loadBalancerPoolClient.delete(
                loadBalancerPoolMetadata.getDataCenterId(),
                loadBalancerPoolMetadata.getLoadBalancerId(),
                loadBalancerPoolMetadata.getId()
        );

        return new OperationFuture<>(
                loadBalancerPool,
                new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<LoadBalancerPool>> delete(LoadBalancerPool... loadBalancerPool) {
        return delete(Arrays.asList(loadBalancerPool));
    }

    public OperationFuture<List<LoadBalancerPool>> delete(LoadBalancerPoolFilter filter) {
        List<LoadBalancerPool> loadBalancerPoolList = findLazy(filter)
                .map(metadata -> LoadBalancerPool.refById(
                        metadata.getId(),
                        LoadBalancer.refById(
                                metadata.getLoadBalancerId(),
                                DataCenter.refById(metadata.getDataCenterId())
                        )
                ))
                .collect(toList());

        return delete(loadBalancerPoolList);
    }

    public OperationFuture<List<LoadBalancerPool>> delete(List<LoadBalancerPool> loadBalancerPoolList) {
        List<JobFuture> jobs = loadBalancerPoolList
                .stream()
                .map(reference -> delete(reference).jobFuture())
                .collect(toList());

        return new OperationFuture<>(
                loadBalancerPoolList,
                new ParallelJobsFuture(jobs)
        );
    }

}
