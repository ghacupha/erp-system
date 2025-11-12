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

import io.github.erp.domain.TransactionAccountPostingRun;
import io.github.erp.repository.TransactionAccountPostingRunRepository;
import io.github.erp.repository.search.TransactionAccountPostingRunSearchRepository;
import io.github.erp.service.TransactionAccountPostingRunService;
import io.github.erp.service.dto.TransactionAccountPostingRunDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRunMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccountPostingRun}.
 */
@Service
@Transactional
public class TransactionAccountPostingRunServiceImpl implements TransactionAccountPostingRunService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRunServiceImpl.class);

    private final TransactionAccountPostingRunRepository transactionAccountPostingRunRepository;

    private final TransactionAccountPostingRunMapper transactionAccountPostingRunMapper;

    private final TransactionAccountPostingRunSearchRepository transactionAccountPostingRunSearchRepository;

    public TransactionAccountPostingRunServiceImpl(
        TransactionAccountPostingRunRepository transactionAccountPostingRunRepository,
        TransactionAccountPostingRunMapper transactionAccountPostingRunMapper,
        TransactionAccountPostingRunSearchRepository transactionAccountPostingRunSearchRepository
    ) {
        this.transactionAccountPostingRunRepository = transactionAccountPostingRunRepository;
        this.transactionAccountPostingRunMapper = transactionAccountPostingRunMapper;
        this.transactionAccountPostingRunSearchRepository = transactionAccountPostingRunSearchRepository;
    }

    @Override
    public TransactionAccountPostingRunDTO save(TransactionAccountPostingRunDTO transactionAccountPostingRunDTO) {
        log.debug("Request to save TransactionAccountPostingRun : {}", transactionAccountPostingRunDTO);
        TransactionAccountPostingRun transactionAccountPostingRun = transactionAccountPostingRunMapper.toEntity(
            transactionAccountPostingRunDTO
        );
        transactionAccountPostingRun = transactionAccountPostingRunRepository.save(transactionAccountPostingRun);
        TransactionAccountPostingRunDTO result = transactionAccountPostingRunMapper.toDto(transactionAccountPostingRun);
        transactionAccountPostingRunSearchRepository.save(transactionAccountPostingRun);
        return result;
    }

    @Override
    public Optional<TransactionAccountPostingRunDTO> partialUpdate(TransactionAccountPostingRunDTO transactionAccountPostingRunDTO) {
        log.debug("Request to partially update TransactionAccountPostingRun : {}", transactionAccountPostingRunDTO);

        return transactionAccountPostingRunRepository
            .findById(transactionAccountPostingRunDTO.getId())
            .map(existingTransactionAccountPostingRun -> {
                transactionAccountPostingRunMapper.partialUpdate(existingTransactionAccountPostingRun, transactionAccountPostingRunDTO);

                return existingTransactionAccountPostingRun;
            })
            .map(transactionAccountPostingRunRepository::save)
            .map(savedTransactionAccountPostingRun -> {
                transactionAccountPostingRunSearchRepository.save(savedTransactionAccountPostingRun);

                return savedTransactionAccountPostingRun;
            })
            .map(transactionAccountPostingRunMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRunDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountPostingRuns");
        return transactionAccountPostingRunRepository.findAll(pageable).map(transactionAccountPostingRunMapper::toDto);
    }

    public Page<TransactionAccountPostingRunDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionAccountPostingRunRepository
            .findAllWithEagerRelationships(pageable)
            .map(transactionAccountPostingRunMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountPostingRunDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountPostingRun : {}", id);
        return transactionAccountPostingRunRepository.findOneWithEagerRelationships(id).map(transactionAccountPostingRunMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountPostingRun : {}", id);
        transactionAccountPostingRunRepository.deleteById(id);
        transactionAccountPostingRunSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRunDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountPostingRuns for query {}", query);
        return transactionAccountPostingRunSearchRepository.search(query, pageable).map(transactionAccountPostingRunMapper::toDto);
    }
}
