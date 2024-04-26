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

import io.github.erp.domain.LoanProductType;
import io.github.erp.repository.LoanProductTypeRepository;
import io.github.erp.repository.search.LoanProductTypeSearchRepository;
import io.github.erp.service.LoanProductTypeService;
import io.github.erp.service.dto.LoanProductTypeDTO;
import io.github.erp.service.mapper.LoanProductTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanProductType}.
 */
@Service
@Transactional
public class LoanProductTypeServiceImpl implements LoanProductTypeService {

    private final Logger log = LoggerFactory.getLogger(LoanProductTypeServiceImpl.class);

    private final LoanProductTypeRepository loanProductTypeRepository;

    private final LoanProductTypeMapper loanProductTypeMapper;

    private final LoanProductTypeSearchRepository loanProductTypeSearchRepository;

    public LoanProductTypeServiceImpl(
        LoanProductTypeRepository loanProductTypeRepository,
        LoanProductTypeMapper loanProductTypeMapper,
        LoanProductTypeSearchRepository loanProductTypeSearchRepository
    ) {
        this.loanProductTypeRepository = loanProductTypeRepository;
        this.loanProductTypeMapper = loanProductTypeMapper;
        this.loanProductTypeSearchRepository = loanProductTypeSearchRepository;
    }

    @Override
    public LoanProductTypeDTO save(LoanProductTypeDTO loanProductTypeDTO) {
        log.debug("Request to save LoanProductType : {}", loanProductTypeDTO);
        LoanProductType loanProductType = loanProductTypeMapper.toEntity(loanProductTypeDTO);
        loanProductType = loanProductTypeRepository.save(loanProductType);
        LoanProductTypeDTO result = loanProductTypeMapper.toDto(loanProductType);
        loanProductTypeSearchRepository.save(loanProductType);
        return result;
    }

    @Override
    public Optional<LoanProductTypeDTO> partialUpdate(LoanProductTypeDTO loanProductTypeDTO) {
        log.debug("Request to partially update LoanProductType : {}", loanProductTypeDTO);

        return loanProductTypeRepository
            .findById(loanProductTypeDTO.getId())
            .map(existingLoanProductType -> {
                loanProductTypeMapper.partialUpdate(existingLoanProductType, loanProductTypeDTO);

                return existingLoanProductType;
            })
            .map(loanProductTypeRepository::save)
            .map(savedLoanProductType -> {
                loanProductTypeSearchRepository.save(savedLoanProductType);

                return savedLoanProductType;
            })
            .map(loanProductTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanProductTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanProductTypes");
        return loanProductTypeRepository.findAll(pageable).map(loanProductTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanProductTypeDTO> findOne(Long id) {
        log.debug("Request to get LoanProductType : {}", id);
        return loanProductTypeRepository.findById(id).map(loanProductTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanProductType : {}", id);
        loanProductTypeRepository.deleteById(id);
        loanProductTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanProductTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanProductTypes for query {}", query);
        return loanProductTypeSearchRepository.search(query, pageable).map(loanProductTypeMapper::toDto);
    }
}
