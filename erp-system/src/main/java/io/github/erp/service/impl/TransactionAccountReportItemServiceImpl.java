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

import io.github.erp.domain.TransactionAccountReportItem;
import io.github.erp.repository.TransactionAccountReportItemRepository;
import io.github.erp.repository.search.TransactionAccountReportItemSearchRepository;
import io.github.erp.service.TransactionAccountReportItemService;
import io.github.erp.service.dto.TransactionAccountReportItemDTO;
import io.github.erp.service.mapper.TransactionAccountReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionAccountReportItem}.
 */
@Service
@Transactional
public class TransactionAccountReportItemServiceImpl implements TransactionAccountReportItemService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountReportItemServiceImpl.class);

    private final TransactionAccountReportItemRepository transactionAccountReportItemRepository;

    private final TransactionAccountReportItemMapper transactionAccountReportItemMapper;

    private final TransactionAccountReportItemSearchRepository transactionAccountReportItemSearchRepository;

    public TransactionAccountReportItemServiceImpl(
        TransactionAccountReportItemRepository transactionAccountReportItemRepository,
        TransactionAccountReportItemMapper transactionAccountReportItemMapper,
        TransactionAccountReportItemSearchRepository transactionAccountReportItemSearchRepository
    ) {
        this.transactionAccountReportItemRepository = transactionAccountReportItemRepository;
        this.transactionAccountReportItemMapper = transactionAccountReportItemMapper;
        this.transactionAccountReportItemSearchRepository = transactionAccountReportItemSearchRepository;
    }

    @Override
    public TransactionAccountReportItemDTO save(TransactionAccountReportItemDTO transactionAccountReportItemDTO) {
        log.debug("Request to save TransactionAccountReportItem : {}", transactionAccountReportItemDTO);
        TransactionAccountReportItem transactionAccountReportItem = transactionAccountReportItemMapper.toEntity(
            transactionAccountReportItemDTO
        );
        transactionAccountReportItem = transactionAccountReportItemRepository.save(transactionAccountReportItem);
        TransactionAccountReportItemDTO result = transactionAccountReportItemMapper.toDto(transactionAccountReportItem);
        transactionAccountReportItemSearchRepository.save(transactionAccountReportItem);
        return result;
    }

    @Override
    public Optional<TransactionAccountReportItemDTO> partialUpdate(TransactionAccountReportItemDTO transactionAccountReportItemDTO) {
        log.debug("Request to partially update TransactionAccountReportItem : {}", transactionAccountReportItemDTO);

        return transactionAccountReportItemRepository
            .findById(transactionAccountReportItemDTO.getId())
            .map(existingTransactionAccountReportItem -> {
                transactionAccountReportItemMapper.partialUpdate(existingTransactionAccountReportItem, transactionAccountReportItemDTO);

                return existingTransactionAccountReportItem;
            })
            .map(transactionAccountReportItemRepository::save)
            .map(savedTransactionAccountReportItem -> {
                transactionAccountReportItemSearchRepository.save(savedTransactionAccountReportItem);

                return savedTransactionAccountReportItem;
            })
            .map(transactionAccountReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccountReportItems");
        return transactionAccountReportItemRepository.findAll(pageable).map(transactionAccountReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccountReportItemDTO> findOne(Long id) {
        log.debug("Request to get TransactionAccountReportItem : {}", id);
        return transactionAccountReportItemRepository.findById(id).map(transactionAccountReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccountReportItem : {}", id);
        transactionAccountReportItemRepository.deleteById(id);
        transactionAccountReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccountReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccountReportItems for query {}", query);
        return transactionAccountReportItemSearchRepository.search(query, pageable).map(transactionAccountReportItemMapper::toDto);
    }
}
