package io.github.erp.erp.ledgers.posting.queue;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.domain.TransactionAccountPostingRuleCondition;
import io.github.erp.domain.TransactionAccountPostingRuleTemplate;
import io.github.erp.erp.ledgers.posting.TransactionAccountPostingRuleIndexingSupport;
import io.github.erp.repository.TransactionAccountPostingRuleConditionRepository;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.repository.TransactionAccountPostingRuleTemplateRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleConditionSearchRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleTemplateSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionAccountPostingRuleIndexConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRuleIndexConsumer.class);
    private static final String POSTING_RULE_INDEX_TOPIC = "spring.kafka.topics.posting-rule-index.topic.name:posting-rule-index";

    private final TransactionAccountPostingRuleRepository postingRuleRepository;
    private final TransactionAccountPostingRuleTemplateRepository templateRepository;
    private final TransactionAccountPostingRuleConditionRepository conditionRepository;
    private final TransactionAccountPostingRuleSearchRepository postingRuleSearchRepository;
    private final TransactionAccountPostingRuleTemplateSearchRepository templateSearchRepository;
    private final TransactionAccountPostingRuleConditionSearchRepository conditionSearchRepository;
    private final TransactionAccountPostingRuleIndexingSupport indexingSupport;
    private final String topicName;

    public TransactionAccountPostingRuleIndexConsumer(
        TransactionAccountPostingRuleRepository postingRuleRepository,
        TransactionAccountPostingRuleTemplateRepository templateRepository,
        TransactionAccountPostingRuleConditionRepository conditionRepository,
        TransactionAccountPostingRuleSearchRepository postingRuleSearchRepository,
        TransactionAccountPostingRuleTemplateSearchRepository templateSearchRepository,
        TransactionAccountPostingRuleConditionSearchRepository conditionSearchRepository,
        TransactionAccountPostingRuleIndexingSupport indexingSupport,
        @Value("${" + POSTING_RULE_INDEX_TOPIC + "}") String topicName
    ) {
        this.postingRuleRepository = postingRuleRepository;
        this.templateRepository = templateRepository;
        this.conditionRepository = conditionRepository;
        this.postingRuleSearchRepository = postingRuleSearchRepository;
        this.templateSearchRepository = templateSearchRepository;
        this.conditionSearchRepository = conditionSearchRepository;
        this.indexingSupport = indexingSupport;
        this.topicName = topicName;
    }

    @KafkaListener(
        topics = "${" + POSTING_RULE_INDEX_TOPIC + "}",
        containerFactory = "postingRuleIndexKafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(TransactionAccountPostingRuleIndexMessage message) {
        if (message == null || message.getOperation() == null) {
            return;
        }

        log.debug(
            "Processing posting rule index message for rule {} on topic {} with operation {}",
            message.getPostingRuleId(),
            topicName,
            message.getOperation()
        );

        if (message.getOperation() == PostingRuleIndexOperation.DELETE) {
            deleteFromIndex(message);
            return;
        }

        upsertIndex(message);
    }

    private void upsertIndex(TransactionAccountPostingRuleIndexMessage message) {
        if (message.getPostingRuleId() != null) {
            Optional<TransactionAccountPostingRule> postingRule = postingRuleRepository.findById(message.getPostingRuleId());
            postingRule.map(indexingSupport::sanitizePostingRule).ifPresent(postingRuleSearchRepository::save);
        }

        if (message.getTemplateIds() != null && !message.getTemplateIds().isEmpty()) {
            List<TransactionAccountPostingRuleTemplate> templates = templateRepository
                .findAllById(message.getTemplateIds())
                .stream()
                .map(indexingSupport::sanitizeTemplate)
                .collect(Collectors.toList());
            if (!templates.isEmpty()) {
                templateSearchRepository.saveAll(templates);
            }
        }

        if (message.getConditionIds() != null && !message.getConditionIds().isEmpty()) {
            List<TransactionAccountPostingRuleCondition> conditions = conditionRepository
                .findAllById(message.getConditionIds())
                .stream()
                .map(indexingSupport::sanitizeCondition)
                .collect(Collectors.toList());
            if (!conditions.isEmpty()) {
                conditionSearchRepository.saveAll(conditions);
            }
        }
    }

    private void deleteFromIndex(TransactionAccountPostingRuleIndexMessage message) {
        if (message.getPostingRuleId() != null) {
            postingRuleSearchRepository.deleteById(message.getPostingRuleId());
        }

        if (message.getTemplateIds() != null && !message.getTemplateIds().isEmpty()) {
            templateSearchRepository.deleteAllById(message.getTemplateIds());
        }

        if (message.getConditionIds() != null && !message.getConditionIds().isEmpty()) {
            conditionSearchRepository.deleteAllById(message.getConditionIds());
        }
    }
}
