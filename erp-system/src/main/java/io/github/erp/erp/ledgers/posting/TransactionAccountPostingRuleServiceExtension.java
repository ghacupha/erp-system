package io.github.erp.erp.ledgers.posting;

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
import io.github.erp.erp.ledgers.posting.queue.PostingRuleIndexOperation;
import io.github.erp.erp.ledgers.posting.queue.TransactionAccountPostingRuleIndexMessage;
import io.github.erp.erp.ledgers.posting.queue.TransactionAccountPostingRuleIndexProducer;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.service.TransactionAccountPostingRuleService;
import io.github.erp.service.dto.TransactionAccountPostingRuleDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRuleMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Primary
@Transactional
public class TransactionAccountPostingRuleServiceExtension implements TransactionAccountPostingRuleService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRuleServiceExtension.class);

    private final TransactionAccountPostingRuleRepository postingRuleRepository;
    private final TransactionAccountPostingRuleMapper postingRuleMapper;
    private final TransactionAccountPostingRuleSearchRepository postingRuleSearchRepository;
    private final TransactionAccountPostingRuleIndexProducer indexProducer;

    public TransactionAccountPostingRuleServiceExtension(
        TransactionAccountPostingRuleRepository postingRuleRepository,
        TransactionAccountPostingRuleMapper postingRuleMapper,
        TransactionAccountPostingRuleSearchRepository postingRuleSearchRepository,
        TransactionAccountPostingRuleIndexProducer indexProducer
    ) {
        this.postingRuleRepository = postingRuleRepository;
        this.postingRuleMapper = postingRuleMapper;
        this.postingRuleSearchRepository = postingRuleSearchRepository;
        this.indexProducer = indexProducer;
    }

    @Override
    public TransactionAccountPostingRuleDTO save(TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO) {
        log.debug("Request to save TransactionAccountPostingRule : {}", transactionAccountPostingRuleDTO);
        TransactionAccountPostingRule transactionAccountPostingRule = postingRuleMapper.toEntity(transactionAccountPostingRuleDTO);
        transactionAccountPostingRule = postingRuleRepository.save(transactionAccountPostingRule);
        TransactionAccountPostingRuleDTO result = postingRuleMapper.toDto(transactionAccountPostingRule);
        dispatchIndexAfterCommit(buildIndexMessage(transactionAccountPostingRule, PostingRuleIndexOperation.UPSERT));
        return result;
    }

    @Override
    public Optional<TransactionAccountPostingRuleDTO> partialUpdate(TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO) {
        log.debug("Request to partially update TransactionAccountPostingRule : {}", transactionAccountPostingRuleDTO);

        return postingRuleRepository
            .findById(transactionAccountPostingRuleDTO.getId())
            .map(existingTransactionAccountPostingRule -> {
                postingRuleMapper.partialUpdate(existingTransactionAccountPostingRule, transactionAccountPostingRuleDTO);
                return existingTransactionAccountPostingRule;
            })
            .map(postingRuleRepository::save)
            .map(savedTransactionAccountPostingRule -> {
                dispatchIndexAfterCommit(buildIndexMessage(savedTransactionAccountPostingRule, PostingRuleIndexOperation.UPSERT));
                return savedTransactionAccountPostingRule;
            })
            .map(postingRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountPostingRules");
        return postingRuleRepository.findAll(pageable).map(postingRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountPostingRuleDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountPostingRule : {}", id);
        return postingRuleRepository.findById(id).map(postingRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountPostingRule : {}", id);
        TransactionAccountPostingRuleIndexMessage indexMessage = null;
        Optional<TransactionAccountPostingRule> postingRule = postingRuleRepository.findById(id);
        if (postingRule.isPresent()) {
            indexMessage = buildIndexMessage(postingRule.get(), PostingRuleIndexOperation.DELETE);
        }
        postingRuleRepository.deleteById(id);
        if (indexMessage != null) {
            dispatchIndexAfterCommit(indexMessage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountPostingRules for query {}", query);
        Page<TransactionAccountPostingRule> searchPage = postingRuleSearchRepository.search(query, pageable);
        List<Long> ids = searchPage
            .stream()
            .map(TransactionAccountPostingRule::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, searchPage.getTotalElements());
        }

        Map<Long, TransactionAccountPostingRule> entitiesById = new HashMap<>();
        postingRuleRepository.findAllById(ids).forEach(entity -> entitiesById.put(entity.getId(), entity));

        List<TransactionAccountPostingRuleDTO> results = new ArrayList<>();
        for (Long id : ids) {
            TransactionAccountPostingRule entity = entitiesById.get(id);
            if (entity != null) {
                results.add(postingRuleMapper.toDto(entity));
            }
        }

        return new PageImpl<>(results, pageable, searchPage.getTotalElements());
    }

    private TransactionAccountPostingRuleIndexMessage buildIndexMessage(
        TransactionAccountPostingRule transactionAccountPostingRule,
        PostingRuleIndexOperation operation
    ) {
        TransactionAccountPostingRuleIndexMessage message = new TransactionAccountPostingRuleIndexMessage();
        message.setPostingRuleId(transactionAccountPostingRule.getId());
        message.setOperation(operation);
        message.setTemplateIds(
            Optional
                .ofNullable(transactionAccountPostingRule.getPostingRuleTemplates())
                .orElse(Collections.emptySet())
                .stream()
                .map(template -> template.getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
        message.setConditionIds(
            Optional
                .ofNullable(transactionAccountPostingRule.getPostingRuleConditions())
                .orElse(Collections.emptySet())
                .stream()
                .map(condition -> condition.getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
        return message;
    }

    private void dispatchIndexAfterCommit(TransactionAccountPostingRuleIndexMessage message) {
        Runnable dispatch = () -> indexProducer.sendIndexMessage(message);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    dispatch.run();
                }
            });
            return;
        }

        dispatch.run();
    }
}
