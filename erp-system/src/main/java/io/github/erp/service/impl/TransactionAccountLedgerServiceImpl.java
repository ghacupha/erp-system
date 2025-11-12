package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.TransactionAccountLedger;
import io.github.erp.repository.TransactionAccountLedgerRepository;
import io.github.erp.repository.search.TransactionAccountLedgerSearchRepository;
import io.github.erp.service.TransactionAccountLedgerService;
import io.github.erp.service.dto.TransactionAccountLedgerDTO;
import io.github.erp.service.mapper.TransactionAccountLedgerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccountLedger}.
 */
@Service
@Transactional
public class TransactionAccountLedgerServiceImpl implements TransactionAccountLedgerService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountLedgerServiceImpl.class);

    private final TransactionAccountLedgerRepository transactionAccountLedgerRepository;

    private final TransactionAccountLedgerMapper transactionAccountLedgerMapper;

    private final TransactionAccountLedgerSearchRepository transactionAccountLedgerSearchRepository;

    public TransactionAccountLedgerServiceImpl(
        TransactionAccountLedgerRepository transactionAccountLedgerRepository,
        TransactionAccountLedgerMapper transactionAccountLedgerMapper,
        TransactionAccountLedgerSearchRepository transactionAccountLedgerSearchRepository
    ) {
        this.transactionAccountLedgerRepository = transactionAccountLedgerRepository;
        this.transactionAccountLedgerMapper = transactionAccountLedgerMapper;
        this.transactionAccountLedgerSearchRepository = transactionAccountLedgerSearchRepository;
    }

    @Override
    public TransactionAccountLedgerDTO save(TransactionAccountLedgerDTO transactionAccountLedgerDTO) {
        log.debug("Request to save TransactionAccountLedger : {}", transactionAccountLedgerDTO);
        TransactionAccountLedger transactionAccountLedger = transactionAccountLedgerMapper.toEntity(transactionAccountLedgerDTO);
        transactionAccountLedger = transactionAccountLedgerRepository.save(transactionAccountLedger);
        TransactionAccountLedgerDTO result = transactionAccountLedgerMapper.toDto(transactionAccountLedger);
        transactionAccountLedgerSearchRepository.save(transactionAccountLedger);
        return result;
    }

    @Override
    public Optional<TransactionAccountLedgerDTO> partialUpdate(TransactionAccountLedgerDTO transactionAccountLedgerDTO) {
        log.debug("Request to partially update TransactionAccountLedger : {}", transactionAccountLedgerDTO);

        return transactionAccountLedgerRepository
            .findById(transactionAccountLedgerDTO.getId())
            .map(existingTransactionAccountLedger -> {
                transactionAccountLedgerMapper.partialUpdate(existingTransactionAccountLedger, transactionAccountLedgerDTO);

                return existingTransactionAccountLedger;
            })
            .map(transactionAccountLedgerRepository::save)
            .map(savedTransactionAccountLedger -> {
                transactionAccountLedgerSearchRepository.save(savedTransactionAccountLedger);

                return savedTransactionAccountLedger;
            })
            .map(transactionAccountLedgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountLedgerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountLedgers");
        return transactionAccountLedgerRepository.findAll(pageable).map(transactionAccountLedgerMapper::toDto);
    }

    public Page<TransactionAccountLedgerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionAccountLedgerRepository.findAllWithEagerRelationships(pageable).map(transactionAccountLedgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountLedgerDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountLedger : {}", id);
        return transactionAccountLedgerRepository.findOneWithEagerRelationships(id).map(transactionAccountLedgerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountLedger : {}", id);
        transactionAccountLedgerRepository.deleteById(id);
        transactionAccountLedgerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountLedgerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountLedgers for query {}", query);
        return transactionAccountLedgerSearchRepository.search(query, pageable).map(transactionAccountLedgerMapper::toDto);
    }
}
