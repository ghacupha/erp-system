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

import io.github.erp.domain.TransactionDetails;
import io.github.erp.repository.TransactionDetailsRepository;
import io.github.erp.repository.search.TransactionDetailsSearchRepository;
import io.github.erp.service.TransactionDetailsService;
import io.github.erp.service.dto.TransactionDetailsDTO;
import io.github.erp.service.mapper.TransactionDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionDetails}.
 */
@Service
@Transactional
public class TransactionDetailsServiceImpl implements TransactionDetailsService {

    private final Logger log = LoggerFactory.getLogger(TransactionDetailsServiceImpl.class);

    private final TransactionDetailsRepository transactionDetailsRepository;

    private final TransactionDetailsMapper transactionDetailsMapper;

    private final TransactionDetailsSearchRepository transactionDetailsSearchRepository;

    public TransactionDetailsServiceImpl(
        TransactionDetailsRepository transactionDetailsRepository,
        TransactionDetailsMapper transactionDetailsMapper,
        TransactionDetailsSearchRepository transactionDetailsSearchRepository
    ) {
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.transactionDetailsMapper = transactionDetailsMapper;
        this.transactionDetailsSearchRepository = transactionDetailsSearchRepository;
    }

    @Override
    public TransactionDetailsDTO save(TransactionDetailsDTO transactionDetailsDTO) {
        log.debug("Request to save TransactionDetails : {}", transactionDetailsDTO);
        TransactionDetails transactionDetails = transactionDetailsMapper.toEntity(transactionDetailsDTO);
        transactionDetails = transactionDetailsRepository.save(transactionDetails);
        TransactionDetailsDTO result = transactionDetailsMapper.toDto(transactionDetails);
        transactionDetailsSearchRepository.save(transactionDetails);
        return result;
    }

    @Override
    public Optional<TransactionDetailsDTO> partialUpdate(TransactionDetailsDTO transactionDetailsDTO) {
        log.debug("Request to partially update TransactionDetails : {}", transactionDetailsDTO);

        return transactionDetailsRepository
            .findById(transactionDetailsDTO.getId())
            .map(existingTransactionDetails -> {
                transactionDetailsMapper.partialUpdate(existingTransactionDetails, transactionDetailsDTO);

                return existingTransactionDetails;
            })
            .map(transactionDetailsRepository::save)
            .map(savedTransactionDetails -> {
                transactionDetailsSearchRepository.save(savedTransactionDetails);

                return savedTransactionDetails;
            })
            .map(transactionDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionDetails");
        return transactionDetailsRepository.findAll(pageable).map(transactionDetailsMapper::toDto);
    }

    public Page<TransactionDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionDetailsRepository.findAllWithEagerRelationships(pageable).map(transactionDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDetailsDTO> findOne(Long id) {
        log.debug("Request to get TransactionDetails : {}", id);
        return transactionDetailsRepository.findOneWithEagerRelationships(id).map(transactionDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionDetails : {}", id);
        transactionDetailsRepository.deleteById(id);
        transactionDetailsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionDetails for query {}", query);
        return transactionDetailsSearchRepository.search(query, pageable).map(transactionDetailsMapper::toDto);
    }
}
