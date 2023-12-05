package io.github.erp.service.impl;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.LoanRestructureItem;
import io.github.erp.repository.LoanRestructureItemRepository;
import io.github.erp.repository.search.LoanRestructureItemSearchRepository;
import io.github.erp.service.LoanRestructureItemService;
import io.github.erp.service.dto.LoanRestructureItemDTO;
import io.github.erp.service.mapper.LoanRestructureItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanRestructureItem}.
 */
@Service
@Transactional
public class LoanRestructureItemServiceImpl implements LoanRestructureItemService {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureItemServiceImpl.class);

    private final LoanRestructureItemRepository loanRestructureItemRepository;

    private final LoanRestructureItemMapper loanRestructureItemMapper;

    private final LoanRestructureItemSearchRepository loanRestructureItemSearchRepository;

    public LoanRestructureItemServiceImpl(
        LoanRestructureItemRepository loanRestructureItemRepository,
        LoanRestructureItemMapper loanRestructureItemMapper,
        LoanRestructureItemSearchRepository loanRestructureItemSearchRepository
    ) {
        this.loanRestructureItemRepository = loanRestructureItemRepository;
        this.loanRestructureItemMapper = loanRestructureItemMapper;
        this.loanRestructureItemSearchRepository = loanRestructureItemSearchRepository;
    }

    @Override
    public LoanRestructureItemDTO save(LoanRestructureItemDTO loanRestructureItemDTO) {
        log.debug("Request to save LoanRestructureItem : {}", loanRestructureItemDTO);
        LoanRestructureItem loanRestructureItem = loanRestructureItemMapper.toEntity(loanRestructureItemDTO);
        loanRestructureItem = loanRestructureItemRepository.save(loanRestructureItem);
        LoanRestructureItemDTO result = loanRestructureItemMapper.toDto(loanRestructureItem);
        loanRestructureItemSearchRepository.save(loanRestructureItem);
        return result;
    }

    @Override
    public Optional<LoanRestructureItemDTO> partialUpdate(LoanRestructureItemDTO loanRestructureItemDTO) {
        log.debug("Request to partially update LoanRestructureItem : {}", loanRestructureItemDTO);

        return loanRestructureItemRepository
            .findById(loanRestructureItemDTO.getId())
            .map(existingLoanRestructureItem -> {
                loanRestructureItemMapper.partialUpdate(existingLoanRestructureItem, loanRestructureItemDTO);

                return existingLoanRestructureItem;
            })
            .map(loanRestructureItemRepository::save)
            .map(savedLoanRestructureItem -> {
                loanRestructureItemSearchRepository.save(savedLoanRestructureItem);

                return savedLoanRestructureItem;
            })
            .map(loanRestructureItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRestructureItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanRestructureItems");
        return loanRestructureItemRepository.findAll(pageable).map(loanRestructureItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanRestructureItemDTO> findOne(Long id) {
        log.debug("Request to get LoanRestructureItem : {}", id);
        return loanRestructureItemRepository.findById(id).map(loanRestructureItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanRestructureItem : {}", id);
        loanRestructureItemRepository.deleteById(id);
        loanRestructureItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRestructureItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanRestructureItems for query {}", query);
        return loanRestructureItemSearchRepository.search(query, pageable).map(loanRestructureItemMapper::toDto);
    }
}
