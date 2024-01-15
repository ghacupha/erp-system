package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.LoanAccountCategory;
import io.github.erp.repository.LoanAccountCategoryRepository;
import io.github.erp.repository.search.LoanAccountCategorySearchRepository;
import io.github.erp.service.LoanAccountCategoryService;
import io.github.erp.service.dto.LoanAccountCategoryDTO;
import io.github.erp.service.mapper.LoanAccountCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanAccountCategory}.
 */
@Service
@Transactional
public class LoanAccountCategoryServiceImpl implements LoanAccountCategoryService {

    private final Logger log = LoggerFactory.getLogger(LoanAccountCategoryServiceImpl.class);

    private final LoanAccountCategoryRepository loanAccountCategoryRepository;

    private final LoanAccountCategoryMapper loanAccountCategoryMapper;

    private final LoanAccountCategorySearchRepository loanAccountCategorySearchRepository;

    public LoanAccountCategoryServiceImpl(
        LoanAccountCategoryRepository loanAccountCategoryRepository,
        LoanAccountCategoryMapper loanAccountCategoryMapper,
        LoanAccountCategorySearchRepository loanAccountCategorySearchRepository
    ) {
        this.loanAccountCategoryRepository = loanAccountCategoryRepository;
        this.loanAccountCategoryMapper = loanAccountCategoryMapper;
        this.loanAccountCategorySearchRepository = loanAccountCategorySearchRepository;
    }

    @Override
    public LoanAccountCategoryDTO save(LoanAccountCategoryDTO loanAccountCategoryDTO) {
        log.debug("Request to save LoanAccountCategory : {}", loanAccountCategoryDTO);
        LoanAccountCategory loanAccountCategory = loanAccountCategoryMapper.toEntity(loanAccountCategoryDTO);
        loanAccountCategory = loanAccountCategoryRepository.save(loanAccountCategory);
        LoanAccountCategoryDTO result = loanAccountCategoryMapper.toDto(loanAccountCategory);
        loanAccountCategorySearchRepository.save(loanAccountCategory);
        return result;
    }

    @Override
    public Optional<LoanAccountCategoryDTO> partialUpdate(LoanAccountCategoryDTO loanAccountCategoryDTO) {
        log.debug("Request to partially update LoanAccountCategory : {}", loanAccountCategoryDTO);

        return loanAccountCategoryRepository
            .findById(loanAccountCategoryDTO.getId())
            .map(existingLoanAccountCategory -> {
                loanAccountCategoryMapper.partialUpdate(existingLoanAccountCategory, loanAccountCategoryDTO);

                return existingLoanAccountCategory;
            })
            .map(loanAccountCategoryRepository::save)
            .map(savedLoanAccountCategory -> {
                loanAccountCategorySearchRepository.save(savedLoanAccountCategory);

                return savedLoanAccountCategory;
            })
            .map(loanAccountCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanAccountCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanAccountCategories");
        return loanAccountCategoryRepository.findAll(pageable).map(loanAccountCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanAccountCategoryDTO> findOne(Long id) {
        log.debug("Request to get LoanAccountCategory : {}", id);
        return loanAccountCategoryRepository.findById(id).map(loanAccountCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanAccountCategory : {}", id);
        loanAccountCategoryRepository.deleteById(id);
        loanAccountCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanAccountCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanAccountCategories for query {}", query);
        return loanAccountCategorySearchRepository.search(query, pageable).map(loanAccountCategoryMapper::toDto);
    }
}
