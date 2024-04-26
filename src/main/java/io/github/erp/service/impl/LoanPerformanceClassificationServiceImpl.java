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

import io.github.erp.domain.LoanPerformanceClassification;
import io.github.erp.repository.LoanPerformanceClassificationRepository;
import io.github.erp.repository.search.LoanPerformanceClassificationSearchRepository;
import io.github.erp.service.LoanPerformanceClassificationService;
import io.github.erp.service.dto.LoanPerformanceClassificationDTO;
import io.github.erp.service.mapper.LoanPerformanceClassificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanPerformanceClassification}.
 */
@Service
@Transactional
public class LoanPerformanceClassificationServiceImpl implements LoanPerformanceClassificationService {

    private final Logger log = LoggerFactory.getLogger(LoanPerformanceClassificationServiceImpl.class);

    private final LoanPerformanceClassificationRepository loanPerformanceClassificationRepository;

    private final LoanPerformanceClassificationMapper loanPerformanceClassificationMapper;

    private final LoanPerformanceClassificationSearchRepository loanPerformanceClassificationSearchRepository;

    public LoanPerformanceClassificationServiceImpl(
        LoanPerformanceClassificationRepository loanPerformanceClassificationRepository,
        LoanPerformanceClassificationMapper loanPerformanceClassificationMapper,
        LoanPerformanceClassificationSearchRepository loanPerformanceClassificationSearchRepository
    ) {
        this.loanPerformanceClassificationRepository = loanPerformanceClassificationRepository;
        this.loanPerformanceClassificationMapper = loanPerformanceClassificationMapper;
        this.loanPerformanceClassificationSearchRepository = loanPerformanceClassificationSearchRepository;
    }

    @Override
    public LoanPerformanceClassificationDTO save(LoanPerformanceClassificationDTO loanPerformanceClassificationDTO) {
        log.debug("Request to save LoanPerformanceClassification : {}", loanPerformanceClassificationDTO);
        LoanPerformanceClassification loanPerformanceClassification = loanPerformanceClassificationMapper.toEntity(
            loanPerformanceClassificationDTO
        );
        loanPerformanceClassification = loanPerformanceClassificationRepository.save(loanPerformanceClassification);
        LoanPerformanceClassificationDTO result = loanPerformanceClassificationMapper.toDto(loanPerformanceClassification);
        loanPerformanceClassificationSearchRepository.save(loanPerformanceClassification);
        return result;
    }

    @Override
    public Optional<LoanPerformanceClassificationDTO> partialUpdate(LoanPerformanceClassificationDTO loanPerformanceClassificationDTO) {
        log.debug("Request to partially update LoanPerformanceClassification : {}", loanPerformanceClassificationDTO);

        return loanPerformanceClassificationRepository
            .findById(loanPerformanceClassificationDTO.getId())
            .map(existingLoanPerformanceClassification -> {
                loanPerformanceClassificationMapper.partialUpdate(existingLoanPerformanceClassification, loanPerformanceClassificationDTO);

                return existingLoanPerformanceClassification;
            })
            .map(loanPerformanceClassificationRepository::save)
            .map(savedLoanPerformanceClassification -> {
                loanPerformanceClassificationSearchRepository.save(savedLoanPerformanceClassification);

                return savedLoanPerformanceClassification;
            })
            .map(loanPerformanceClassificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanPerformanceClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanPerformanceClassifications");
        return loanPerformanceClassificationRepository.findAll(pageable).map(loanPerformanceClassificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanPerformanceClassificationDTO> findOne(Long id) {
        log.debug("Request to get LoanPerformanceClassification : {}", id);
        return loanPerformanceClassificationRepository.findById(id).map(loanPerformanceClassificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanPerformanceClassification : {}", id);
        loanPerformanceClassificationRepository.deleteById(id);
        loanPerformanceClassificationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanPerformanceClassificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanPerformanceClassifications for query {}", query);
        return loanPerformanceClassificationSearchRepository.search(query, pageable).map(loanPerformanceClassificationMapper::toDto);
    }
}
