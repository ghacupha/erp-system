package io.github.erp.internal.service.ledgers;

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

import io.github.erp.domain.TransactionAccountCategory;
import io.github.erp.internal.repository.InternalTransactionAccountCategoryRepository;
import io.github.erp.repository.TransactionAccountCategoryRepository;
import io.github.erp.repository.search.TransactionAccountCategorySearchRepository;
import io.github.erp.service.dto.TransactionAccountCategoryDTO;
import io.github.erp.service.mapper.TransactionAccountCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionAccountCategory}.
 */
@Service
@Transactional
public class InternalTransactionAccountCategoryServiceImpl implements InternalTransactionAccountCategoryService {

    private final Logger log = LoggerFactory.getLogger(InternalTransactionAccountCategoryServiceImpl.class);

    private final InternalTransactionAccountCategoryRepository transactionAccountCategoryRepository;

    private final TransactionAccountCategoryMapper transactionAccountCategoryMapper;

    private final TransactionAccountCategorySearchRepository transactionAccountCategorySearchRepository;

    public InternalTransactionAccountCategoryServiceImpl(
        InternalTransactionAccountCategoryRepository transactionAccountCategoryRepository,
        TransactionAccountCategoryMapper transactionAccountCategoryMapper,
        TransactionAccountCategorySearchRepository transactionAccountCategorySearchRepository
    ) {
        this.transactionAccountCategoryRepository = transactionAccountCategoryRepository;
        this.transactionAccountCategoryMapper = transactionAccountCategoryMapper;
        this.transactionAccountCategorySearchRepository = transactionAccountCategorySearchRepository;
    }

    @Override
    public TransactionAccountCategoryDTO save(TransactionAccountCategoryDTO transactionAccountCategoryDTO) {
        log.debug("Request to save TransactionAccountCategory : {}", transactionAccountCategoryDTO);
        TransactionAccountCategory transactionAccountCategory = transactionAccountCategoryMapper.toEntity(transactionAccountCategoryDTO);
        transactionAccountCategory = transactionAccountCategoryRepository.save(transactionAccountCategory);
        TransactionAccountCategoryDTO result = transactionAccountCategoryMapper.toDto(transactionAccountCategory);
        transactionAccountCategorySearchRepository.save(transactionAccountCategory);
        return result;
    }

    @Override
    public Optional<TransactionAccountCategoryDTO> partialUpdate(TransactionAccountCategoryDTO transactionAccountCategoryDTO) {
        log.debug("Request to partially update TransactionAccountCategory : {}", transactionAccountCategoryDTO);

        return transactionAccountCategoryRepository
            .findById(transactionAccountCategoryDTO.getId())
            .map(existingTransactionAccountCategory -> {
                transactionAccountCategoryMapper.partialUpdate(existingTransactionAccountCategory, transactionAccountCategoryDTO);

                return existingTransactionAccountCategory;
            })
            .map(transactionAccountCategoryRepository::save)
            .map(savedTransactionAccountCategory -> {
                transactionAccountCategorySearchRepository.save(savedTransactionAccountCategory);

                return savedTransactionAccountCategory;
            })
            .map(transactionAccountCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountCategories");
        return transactionAccountCategoryRepository.findAll(pageable).map(transactionAccountCategoryMapper::toDto);
    }

    public Page<TransactionAccountCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionAccountCategoryRepository.findAllWithEagerRelationships(pageable).map(transactionAccountCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountCategoryDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountCategory : {}", id);
        return transactionAccountCategoryRepository.findOneWithEagerRelationships(id).map(transactionAccountCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountCategory : {}", id);
        transactionAccountCategoryRepository.deleteById(id);
        transactionAccountCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountCategories for query {}", query);
        return transactionAccountCategorySearchRepository.search(query, pageable).map(transactionAccountCategoryMapper::toDto);
    }
}
