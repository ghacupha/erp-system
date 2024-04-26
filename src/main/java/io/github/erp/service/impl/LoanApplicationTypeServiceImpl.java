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
