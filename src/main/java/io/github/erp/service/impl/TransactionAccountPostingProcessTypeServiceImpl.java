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

import io.github.erp.domain.TransactionAccountPostingProcessType;
import io.github.erp.repository.TransactionAccountPostingProcessTypeRepository;
import io.github.erp.repository.search.TransactionAccountPostingProcessTypeSearchRepository;
import io.github.erp.service.TransactionAccountPostingProcessTypeService;
import io.github.erp.service.dto.TransactionAccountPostingProcessTypeDTO;
import io.github.erp.service.mapper.TransactionAccountPostingProcessTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccountPostingProcessType}.
 */
@Service
@Transactional
public class TransactionAccountPostingProcessTypeServiceImpl implements TransactionAccountPostingProcessTypeService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingProcessTypeServiceImpl.class);

    private final TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository;

    private final TransactionAccountPostingProcessTypeMapper transactionAccountPostingProcessTypeMapper;

    private final TransactionAccountPostingProcessTypeSearchRepository transactionAccountPostingProcessTypeSearchRepository;

    public TransactionAccountPostingProcessTypeServiceImpl(
        TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository,
        TransactionAccountPostingProcessTypeMapper transactionAccountPostingProcessTypeMapper,
        TransactionAccountPostingProcessTypeSearchRepository transactionAccountPostingProcessTypeSearchRepository
    ) {
        this.transactionAccountPostingProcessTypeRepository = transactionAccountPostingProcessTypeRepository;
        this.transactionAccountPostingProcessTypeMapper = transactionAccountPostingProcessTypeMapper;
        this.transactionAccountPostingProcessTypeSearchRepository = transactionAccountPostingProcessTypeSearchRepository;
    }

    @Override
    public TransactionAccountPostingProcessTypeDTO save(TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO) {
        log.debug("Request to save TransactionAccountPostingProcessType : {}", transactionAccountPostingProcessTypeDTO);
        TransactionAccountPostingProcessType transactionAccountPostingProcessType = transactionAccountPostingProcessTypeMapper.toEntity(
            transactionAccountPostingProcessTypeDTO
        );
        transactionAccountPostingProcessType = transactionAccountPostingProcessTypeRepository.save(transactionAccountPostingProcessType);
        TransactionAccountPostingProcessTypeDTO result = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );
        transactionAccountPostingProcessTypeSearchRepository.save(transactionAccountPostingProcessType);
        return result;
    }

    @Override
    public Optional<TransactionAccountPostingProcessTypeDTO> partialUpdate(
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO
    ) {
        log.debug("Request to partially update TransactionAccountPostingProcessType : {}", transactionAccountPostingProcessTypeDTO);

        return transactionAccountPostingProcessTypeRepository
            .findById(transactionAccountPostingProcessTypeDTO.getId())
            .map(existingTransactionAccountPostingProcessType -> {
                transactionAccountPostingProcessTypeMapper.partialUpdate(
                    existingTransactionAccountPostingProcessType,
                    transactionAccountPostingProcessTypeDTO
                );

                return existingTransactionAccountPostingProcessType;
            })
            .map(transactionAccountPostingProcessTypeRepository::save)
            .map(savedTransactionAccountPostingProcessType -> {
                transactionAccountPostingProcessTypeSearchRepository.save(savedTransactionAccountPostingProcessType);

                return savedTransactionAccountPostingProcessType;
            })
            .map(transactionAccountPostingProcessTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingProcessTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountPostingProcessTypes");
        return transactionAccountPostingProcessTypeRepository.findAll(pageable).map(transactionAccountPostingProcessTypeMapper::toDto);
    }

    public Page<TransactionAccountPostingProcessTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionAccountPostingProcessTypeRepository
            .findAllWithEagerRelationships(pageable)
            .map(transactionAccountPostingProcessTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountPostingProcessTypeDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountPostingProcessType : {}", id);
        return transactionAccountPostingProcessTypeRepository
            .findOneWithEagerRelationships(id)
            .map(transactionAccountPostingProcessTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountPostingProcessType : {}", id);
        transactionAccountPostingProcessTypeRepository.deleteById(id);
        transactionAccountPostingProcessTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingProcessTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountPostingProcessTypes for query {}", query);
        return transactionAccountPostingProcessTypeSearchRepository
            .search(query, pageable)
            .map(transactionAccountPostingProcessTypeMapper::toDto);
    }
}
