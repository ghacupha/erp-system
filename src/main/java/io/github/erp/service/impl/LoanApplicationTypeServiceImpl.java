package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.LoanApplicationType;
import io.github.erp.repository.LoanApplicationTypeRepository;
import io.github.erp.repository.search.LoanApplicationTypeSearchRepository;
import io.github.erp.service.LoanApplicationTypeService;
import io.github.erp.service.dto.LoanApplicationTypeDTO;
import io.github.erp.service.mapper.LoanApplicationTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanApplicationType}.
 */
@Service
@Transactional
public class LoanApplicationTypeServiceImpl implements LoanApplicationTypeService {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationTypeServiceImpl.class);

    private final LoanApplicationTypeRepository loanApplicationTypeRepository;

    private final LoanApplicationTypeMapper loanApplicationTypeMapper;

    private final LoanApplicationTypeSearchRepository loanApplicationTypeSearchRepository;

    public LoanApplicationTypeServiceImpl(
        LoanApplicationTypeRepository loanApplicationTypeRepository,
        LoanApplicationTypeMapper loanApplicationTypeMapper,
        LoanApplicationTypeSearchRepository loanApplicationTypeSearchRepository
    ) {
        this.loanApplicationTypeRepository = loanApplicationTypeRepository;
        this.loanApplicationTypeMapper = loanApplicationTypeMapper;
        this.loanApplicationTypeSearchRepository = loanApplicationTypeSearchRepository;
    }

    @Override
    public LoanApplicationTypeDTO save(LoanApplicationTypeDTO loanApplicationTypeDTO) {
        log.debug("Request to save LoanApplicationType : {}", loanApplicationTypeDTO);
        LoanApplicationType loanApplicationType = loanApplicationTypeMapper.toEntity(loanApplicationTypeDTO);
        loanApplicationType = loanApplicationTypeRepository.save(loanApplicationType);
        LoanApplicationTypeDTO result = loanApplicationTypeMapper.toDto(loanApplicationType);
        loanApplicationTypeSearchRepository.save(loanApplicationType);
        return result;
    }

    @Override
    public Optional<LoanApplicationTypeDTO> partialUpdate(LoanApplicationTypeDTO loanApplicationTypeDTO) {
        log.debug("Request to partially update LoanApplicationType : {}", loanApplicationTypeDTO);

        return loanApplicationTypeRepository
            .findById(loanApplicationTypeDTO.getId())
            .map(existingLoanApplicationType -> {
                loanApplicationTypeMapper.partialUpdate(existingLoanApplicationType, loanApplicationTypeDTO);

                return existingLoanApplicationType;
            })
            .map(loanApplicationTypeRepository::save)
            .map(savedLoanApplicationType -> {
                loanApplicationTypeSearchRepository.save(savedLoanApplicationType);

                return savedLoanApplicationType;
            })
            .map(loanApplicationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanApplicationTypes");
        return loanApplicationTypeRepository.findAll(pageable).map(loanApplicationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanApplicationTypeDTO> findOne(Long id) {
        log.debug("Request to get LoanApplicationType : {}", id);
        return loanApplicationTypeRepository.findById(id).map(loanApplicationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanApplicationType : {}", id);
        loanApplicationTypeRepository.deleteById(id);
        loanApplicationTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanApplicationTypes for query {}", query);
        return loanApplicationTypeSearchRepository.search(query, pageable).map(loanApplicationTypeMapper::toDto);
    }
}
