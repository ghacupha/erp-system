package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
