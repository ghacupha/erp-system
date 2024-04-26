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

import io.github.erp.domain.LoanRepaymentFrequency;
import io.github.erp.repository.LoanRepaymentFrequencyRepository;
import io.github.erp.repository.search.LoanRepaymentFrequencySearchRepository;
import io.github.erp.service.LoanRepaymentFrequencyService;
import io.github.erp.service.dto.LoanRepaymentFrequencyDTO;
import io.github.erp.service.mapper.LoanRepaymentFrequencyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanRepaymentFrequency}.
 */
@Service
@Transactional
public class LoanRepaymentFrequencyServiceImpl implements LoanRepaymentFrequencyService {

    private final Logger log = LoggerFactory.getLogger(LoanRepaymentFrequencyServiceImpl.class);

    private final LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository;

    private final LoanRepaymentFrequencyMapper loanRepaymentFrequencyMapper;

    private final LoanRepaymentFrequencySearchRepository loanRepaymentFrequencySearchRepository;

    public LoanRepaymentFrequencyServiceImpl(
        LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository,
        LoanRepaymentFrequencyMapper loanRepaymentFrequencyMapper,
        LoanRepaymentFrequencySearchRepository loanRepaymentFrequencySearchRepository
    ) {
        this.loanRepaymentFrequencyRepository = loanRepaymentFrequencyRepository;
        this.loanRepaymentFrequencyMapper = loanRepaymentFrequencyMapper;
        this.loanRepaymentFrequencySearchRepository = loanRepaymentFrequencySearchRepository;
    }

    @Override
    public LoanRepaymentFrequencyDTO save(LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO) {
        log.debug("Request to save LoanRepaymentFrequency : {}", loanRepaymentFrequencyDTO);
        LoanRepaymentFrequency loanRepaymentFrequency = loanRepaymentFrequencyMapper.toEntity(loanRepaymentFrequencyDTO);
        loanRepaymentFrequency = loanRepaymentFrequencyRepository.save(loanRepaymentFrequency);
        LoanRepaymentFrequencyDTO result = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);
        loanRepaymentFrequencySearchRepository.save(loanRepaymentFrequency);
        return result;
    }

    @Override
    public Optional<LoanRepaymentFrequencyDTO> partialUpdate(LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO) {
        log.debug("Request to partially update LoanRepaymentFrequency : {}", loanRepaymentFrequencyDTO);

        return loanRepaymentFrequencyRepository
            .findById(loanRepaymentFrequencyDTO.getId())
            .map(existingLoanRepaymentFrequency -> {
                loanRepaymentFrequencyMapper.partialUpdate(existingLoanRepaymentFrequency, loanRepaymentFrequencyDTO);

                return existingLoanRepaymentFrequency;
            })
            .map(loanRepaymentFrequencyRepository::save)
            .map(savedLoanRepaymentFrequency -> {
                loanRepaymentFrequencySearchRepository.save(savedLoanRepaymentFrequency);

                return savedLoanRepaymentFrequency;
            })
            .map(loanRepaymentFrequencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRepaymentFrequencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanRepaymentFrequencies");
        return loanRepaymentFrequencyRepository.findAll(pageable).map(loanRepaymentFrequencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanRepaymentFrequencyDTO> findOne(Long id) {
        log.debug("Request to get LoanRepaymentFrequency : {}", id);
        return loanRepaymentFrequencyRepository.findById(id).map(loanRepaymentFrequencyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanRepaymentFrequency : {}", id);
        loanRepaymentFrequencyRepository.deleteById(id);
        loanRepaymentFrequencySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanRepaymentFrequencyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanRepaymentFrequencies for query {}", query);
        return loanRepaymentFrequencySearchRepository.search(query, pageable).map(loanRepaymentFrequencyMapper::toDto);
    }
}
