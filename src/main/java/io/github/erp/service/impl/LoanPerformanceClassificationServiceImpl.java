package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
