package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
