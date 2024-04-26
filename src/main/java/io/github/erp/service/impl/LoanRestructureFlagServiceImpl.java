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

import io.github.erp.domain.LoanRestructureFlag;
import io.github.erp.repository.LoanRestructureFlagRepository;
import io.github.erp.repository.search.LoanRestructureFlagSearchRepository;
import io.github.erp.service.LoanRestructureFlagService;
import io.github.erp.service.dto.LoanRestructureFlagDTO;
import io.github.erp.service.mapper.LoanRestructureFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanRestructureFlag}.
 */
@Service
@Transactional
public class LoanRestructureFlagServiceImpl implements LoanRestructureFlagService {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureFlagServiceImpl.class);

    private final LoanRestructureFlagRepository loanRestructureFlagRepository;

    private final LoanRestructureFlagMapper loanRestructureFlagMapper;

    private final LoanRestructureFlagSearchRepository loanRestructureFlagSearchRepository;

    public LoanRestructureFlagServiceImpl(
        LoanRestructureFlagRepository loanRestructureFlagRepository,
        LoanRestructureFlagMapper loanRestructureFlagMapper,
        LoanRestructureFlagSearchRepository loanRestructureFlagSearchRepository
    ) {
        this.loanRestructureFlagRepository = loanRestructureFlagRepository;
        this.loanRestructureFlagMapper = loanRestructureFlagMapper;
        this.loanRestructureFlagSearchRepository = loanRestructureFlagSearchRepository;
    }

    @Override
    public LoanRestructureFlagDTO save(LoanRestructureFlagDTO loanRestructureFlagDTO) {
        log.debug("Request to save LoanRestructureFlag : {}", loanRestructureFlagDTO);
        LoanRestructureFlag loanRestructureFlag = loanRestructureFlagMapper.toEntity(loanRestructureFlagDTO);
        loanRestructureFlag = loanRestructureFlagRepository.save(loanRestructureFlag);
        LoanRestructureFlagDTO result = loanRestructureFlagMapper.toDto(loanRestructureFlag);
        loanRestructureFlagSearchRepository.save(loanRestructureFlag);
        return result;
    }

    @Override
    public Optional<LoanRestructureFlagDTO> partialUpdate(LoanRestructureFlagDTO loanRestructureFlagDTO) {
        log.debug("Request to partially update LoanRestructureFlag : {}", loanRestructureFlagDTO);

        return loanRestructureFlagRepository
            .findById(loanRestructureFlagDTO.getId())
            .map(existingLoanRestructureFlag -> {
                loanRestructureFlagMapper.partialUpdate(existingLoanRestructureFlag, loanRestructureFlagDTO);

                return existingLoanRestructureFlag;
            })
            .map(loanRestructureFlagRepository::save)
            .map(savedLoanRestructureFlag -> {
                loanRestructureFlagSearchRepository.save(savedLoanRestructureFlag);

                return savedLoanRestructureFlag;
            })
            .map(loanRestructureFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRestructureFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanRestructureFlags");
        return loanRestructureFlagRepository.findAll(pageable).map(loanRestructureFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanRestructureFlagDTO> findOne(Long id) {
        log.debug("Request to get LoanRestructureFlag : {}", id);
        return loanRestructureFlagRepository.findById(id).map(loanRestructureFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanRestructureFlag : {}", id);
        loanRestructureFlagRepository.deleteById(id);
        loanRestructureFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRestructureFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanRestructureFlags for query {}", query);
        return loanRestructureFlagSearchRepository.search(query, pageable).map(loanRestructureFlagMapper::toDto);
    }
}
