package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.service.TransactionAccountPostingRuleService;
import io.github.erp.service.dto.TransactionAccountPostingRuleDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccountPostingRule}.
 */
@Service
@Transactional
public class TransactionAccountPostingRuleServiceImpl implements TransactionAccountPostingRuleService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRuleServiceImpl.class);

    private final TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository;

    private final TransactionAccountPostingRuleMapper transactionAccountPostingRuleMapper;

    private final TransactionAccountPostingRuleSearchRepository transactionAccountPostingRuleSearchRepository;

    public TransactionAccountPostingRuleServiceImpl(
        TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository,
        TransactionAccountPostingRuleMapper transactionAccountPostingRuleMapper,
        TransactionAccountPostingRuleSearchRepository transactionAccountPostingRuleSearchRepository
    ) {
        this.transactionAccountPostingRuleRepository = transactionAccountPostingRuleRepository;
        this.transactionAccountPostingRuleMapper = transactionAccountPostingRuleMapper;
        this.transactionAccountPostingRuleSearchRepository = transactionAccountPostingRuleSearchRepository;
    }

    @Override
    public TransactionAccountPostingRuleDTO save(TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO) {
        log.debug("Request to save TransactionAccountPostingRule : {}", transactionAccountPostingRuleDTO);
        TransactionAccountPostingRule transactionAccountPostingRule = transactionAccountPostingRuleMapper.toEntity(
            transactionAccountPostingRuleDTO
        );
        transactionAccountPostingRule = transactionAccountPostingRuleRepository.save(transactionAccountPostingRule);
        TransactionAccountPostingRuleDTO result = transactionAccountPostingRuleMapper.toDto(transactionAccountPostingRule);
        transactionAccountPostingRuleSearchRepository.save(transactionAccountPostingRule);
        return result;
    }

    @Override
    public Optional<TransactionAccountPostingRuleDTO> partialUpdate(TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO) {
        log.debug("Request to partially update TransactionAccountPostingRule : {}", transactionAccountPostingRuleDTO);

        return transactionAccountPostingRuleRepository
            .findById(transactionAccountPostingRuleDTO.getId())
            .map(existingTransactionAccountPostingRule -> {
                transactionAccountPostingRuleMapper.partialUpdate(existingTransactionAccountPostingRule, transactionAccountPostingRuleDTO);

                return existingTransactionAccountPostingRule;
            })
            .map(transactionAccountPostingRuleRepository::save)
            .map(savedTransactionAccountPostingRule -> {
                transactionAccountPostingRuleSearchRepository.save(savedTransactionAccountPostingRule);

                return savedTransactionAccountPostingRule;
            })
            .map(transactionAccountPostingRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountPostingRules");
        return transactionAccountPostingRuleRepository.findAll(pageable).map(transactionAccountPostingRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountPostingRuleDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountPostingRule : {}", id);
        return transactionAccountPostingRuleRepository.findById(id).map(transactionAccountPostingRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountPostingRule : {}", id);
        transactionAccountPostingRuleRepository.deleteById(id);
        transactionAccountPostingRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountPostingRules for query {}", query);
        return transactionAccountPostingRuleSearchRepository.search(query, pageable).map(transactionAccountPostingRuleMapper::toDto);
    }
}
