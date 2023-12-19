package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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

import io.github.erp.domain.LoanApplicationStatus;
import io.github.erp.repository.LoanApplicationStatusRepository;
import io.github.erp.repository.search.LoanApplicationStatusSearchRepository;
import io.github.erp.service.LoanApplicationStatusService;
import io.github.erp.service.dto.LoanApplicationStatusDTO;
import io.github.erp.service.mapper.LoanApplicationStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanApplicationStatus}.
 */
@Service
@Transactional
public class LoanApplicationStatusServiceImpl implements LoanApplicationStatusService {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationStatusServiceImpl.class);

    private final LoanApplicationStatusRepository loanApplicationStatusRepository;

    private final LoanApplicationStatusMapper loanApplicationStatusMapper;

    private final LoanApplicationStatusSearchRepository loanApplicationStatusSearchRepository;

    public LoanApplicationStatusServiceImpl(
        LoanApplicationStatusRepository loanApplicationStatusRepository,
        LoanApplicationStatusMapper loanApplicationStatusMapper,
        LoanApplicationStatusSearchRepository loanApplicationStatusSearchRepository
    ) {
        this.loanApplicationStatusRepository = loanApplicationStatusRepository;
        this.loanApplicationStatusMapper = loanApplicationStatusMapper;
        this.loanApplicationStatusSearchRepository = loanApplicationStatusSearchRepository;
    }

    @Override
    public LoanApplicationStatusDTO save(LoanApplicationStatusDTO loanApplicationStatusDTO) {
        log.debug("Request to save LoanApplicationStatus : {}", loanApplicationStatusDTO);
        LoanApplicationStatus loanApplicationStatus = loanApplicationStatusMapper.toEntity(loanApplicationStatusDTO);
        loanApplicationStatus = loanApplicationStatusRepository.save(loanApplicationStatus);
        LoanApplicationStatusDTO result = loanApplicationStatusMapper.toDto(loanApplicationStatus);
        loanApplicationStatusSearchRepository.save(loanApplicationStatus);
        return result;
    }

    @Override
    public Optional<LoanApplicationStatusDTO> partialUpdate(LoanApplicationStatusDTO loanApplicationStatusDTO) {
        log.debug("Request to partially update LoanApplicationStatus : {}", loanApplicationStatusDTO);

        return loanApplicationStatusRepository
            .findById(loanApplicationStatusDTO.getId())
            .map(existingLoanApplicationStatus -> {
                loanApplicationStatusMapper.partialUpdate(existingLoanApplicationStatus, loanApplicationStatusDTO);

                return existingLoanApplicationStatus;
            })
            .map(loanApplicationStatusRepository::save)
            .map(savedLoanApplicationStatus -> {
                loanApplicationStatusSearchRepository.save(savedLoanApplicationStatus);

                return savedLoanApplicationStatus;
            })
            .map(loanApplicationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanApplicationStatuses");
        return loanApplicationStatusRepository.findAll(pageable).map(loanApplicationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanApplicationStatusDTO> findOne(Long id) {
        log.debug("Request to get LoanApplicationStatus : {}", id);
        return loanApplicationStatusRepository.findById(id).map(loanApplicationStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanApplicationStatus : {}", id);
        loanApplicationStatusRepository.deleteById(id);
        loanApplicationStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanApplicationStatuses for query {}", query);
        return loanApplicationStatusSearchRepository.search(query, pageable).map(loanApplicationStatusMapper::toDto);
    }
}
