package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TransactionAccountRepository;
import io.github.erp.repository.search.TransactionAccountSearchRepository;
import io.github.erp.service.TransactionAccountService;
import io.github.erp.service.dto.TransactionAccountDTO;
import io.github.erp.service.mapper.TransactionAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccount}.
 */
@Service
@Transactional
public class TransactionAccountServiceImpl implements TransactionAccountService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountServiceImpl.class);

    private final TransactionAccountRepository transactionAccountRepository;

    private final TransactionAccountMapper transactionAccountMapper;

    private final TransactionAccountSearchRepository transactionAccountSearchRepository;

    public TransactionAccountServiceImpl(
        TransactionAccountRepository transactionAccountRepository,
        TransactionAccountMapper transactionAccountMapper,
        TransactionAccountSearchRepository transactionAccountSearchRepository
    ) {
        this.transactionAccountRepository = transactionAccountRepository;
        this.transactionAccountMapper = transactionAccountMapper;
        this.transactionAccountSearchRepository = transactionAccountSearchRepository;
    }

    @Override
    public TransactionAccountDTO save(TransactionAccountDTO transactionAccountDTO) {
        log.debug("Request to save TransactionAccount : {}", transactionAccountDTO);
        TransactionAccount transactionAccount = transactionAccountMapper.toEntity(transactionAccountDTO);
        transactionAccount = transactionAccountRepository.save(transactionAccount);
        TransactionAccountDTO result = transactionAccountMapper.toDto(transactionAccount);
        transactionAccountSearchRepository.save(transactionAccount);
        return result;
    }

    @Override
    public Optional<TransactionAccountDTO> partialUpdate(TransactionAccountDTO transactionAccountDTO) {
        log.debug("Request to partially update TransactionAccount : {}", transactionAccountDTO);

        return transactionAccountRepository
            .findById(transactionAccountDTO.getId())
            .map(existingTransactionAccount -> {
                transactionAccountMapper.partialUpdate(existingTransactionAccount, transactionAccountDTO);

                return existingTransactionAccount;
            })
            .map(transactionAccountRepository::save)
            .map(savedTransactionAccount -> {
                transactionAccountSearchRepository.save(savedTransactionAccount);

                return savedTransactionAccount;
            })
            .map(transactionAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccounts");
        return transactionAccountRepository.findAll(pageable).map(transactionAccountMapper::toDto);
    }

    public Page<TransactionAccountDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionAccountRepository.findAllWithEagerRelationships(pageable).map(transactionAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccount : {}", id);
        return transactionAccountRepository.findOneWithEagerRelationships(id).map(transactionAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccount : {}", id);
        transactionAccountRepository.deleteById(id);
        transactionAccountSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccounts for query {}", query);
        return transactionAccountSearchRepository.search(query, pageable).map(transactionAccountMapper::toDto);
    }
}
