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

package com.centurylink.cloud.sdk.policy.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.policy.services.client.PolicyClient;
import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsEmailMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertActionMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertTriggerMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.ActionSettings;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.ActionSettingsEmail;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertAction;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTrigger;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class AlertService implements
    QueryService<AlertPolicy, AlertPolicyFilter, AlertPolicyMetadata> {

    private PolicyClient client;

    public AlertService(PolicyClient client) {
        this.client = client;
    }

    public OperationFuture<AlertPolicy> create(AlertPolicyConfig createConfig) {
        AlertPolicyMetadata policy = client.createAlertPolicy(makeRequest(createConfig));

        return new OperationFuture<>(
            AlertPolicy.refById(policy.getId()),
            new NoWaitingJobFuture()
        );
    }

    private AlertPolicyRequest makeRequest(AlertPolicyConfig config) {
        return new AlertPolicyRequest()
            .name(config.getName())
            .actions(convertActions(config.getActions()))
            .triggers(convertTriggers(config.getTriggers()));
    }

    private List<AlertActionMetadata> convertActions(List<AlertAction> actions) {
        return actions.stream()
            .map(action -> new AlertActionMetadata()
                    .action(action.getAction())
                    .settings(convertSettings(action.getSettings()))
            )
            .collect(toList());
    }

    private List<AlertTriggerMetadata> convertTriggers(List<AlertTrigger> triggers) {
        return triggers.stream()
            .map(trigger -> new AlertTriggerMetadata()
                    .metric(trigger.getMetric().name().toLowerCase())
                    .duration(trigger.getDuration().format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .threshold(trigger.getThreshold())
            )
            .collect(toList());
    }

    private ActionSettingsMetadata convertSettings(ActionSettings settings) {
        if (settings instanceof ActionSettingsEmail) {
            return new ActionSettingsEmailMetadata()
                .recipients(((ActionSettingsEmail) settings).getRecipients());
        }

        return null;
    }

    public OperationFuture<AlertPolicy> modify(AlertPolicy policyRef,
        AlertPolicyConfig modifyConfig) {

        AlertPolicyMetadata policyToUpdate = findByRef(policyRef);

        AlertPolicyRequest modifyRequest = new AlertPolicyRequest();

        modifyRequest
            .name(
                (modifyConfig.getName() == null) ?
                    policyToUpdate.getName() : modifyConfig.getName()
        ).actions(
            (modifyConfig.getActions().size() == 0) ?
                policyToUpdate.getActions() : convertActions(modifyConfig.getActions())
        ).triggers(
            (modifyConfig.getTriggers().size() == 0) ?
                policyToUpdate.getTriggers() : convertTriggers(modifyConfig.getTriggers())
        );

        client.modifyAlertPolicy(
            policyToUpdate.getId(),
            modifyRequest
        );

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<AlertPolicy>> modify(List<AlertPolicy> policyRefs,
        AlertPolicyConfig modifyConfig) {

        policyRefs.forEach(ref -> modify(ref, modifyConfig));

        return new OperationFuture<>(
            policyRefs,
            new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<AlertPolicy>> modify(AlertPolicyFilter filter,
        AlertPolicyConfig modifyConfig) {

        return modify(getRefsFromFilter(filter), modifyConfig);
    }

    public OperationFuture<AlertPolicy> delete(AlertPolicy policyRef) {
        client.deleteAlertPolicy(findByRef(policyRef).getId());

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<AlertPolicy>> delete(AlertPolicy... policyRefs) {
        List<AlertPolicy> policiesList = Arrays.asList(policyRefs);

        List<JobFuture> jobs = policiesList.stream()
            .map(ref -> delete(ref).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            policiesList,
            new ParallelJobsFuture(jobs)
        );
    }

    public OperationFuture<List<AlertPolicy>> delete(AlertPolicyFilter filter) {
        List<AlertPolicy> policyRefs = getRefsFromFilter(filter);
        return delete(policyRefs.toArray(new AlertPolicy[policyRefs.size()]));
    }


    private List<AlertPolicy> getRefsFromFilter(AlertPolicyFilter filter) {
        return findLazy(filter)
            .map(metadata -> AlertPolicy.refById(metadata.getId()))
            .collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<AlertPolicyMetadata> findLazy(AlertPolicyFilter filter) {
        checkNotNull(filter, "Criteria must be not null");

        return
            filter.applyFindLazy(criteria -> {
                if (isAlwaysTruePredicate(criteria.getPredicate()) &&
                    criteria.getIds().size() > 0) {
                    return
                        criteria.getIds().stream()
                            .map(nullable(curId -> client.getAlertPolicy(curId)))
                            .filter(notNull());
                } else {
                    return
                        client.getAlertPolicies()
                            .stream()
                            .filter(criteria.getPredicate())
                            .filter((criteria.getIds().size() > 0) ?
                                    combine(AlertPolicyMetadata::getId, in(criteria.getIds())) : alwaysTrue()
                            );
                }
            });
    }
}
