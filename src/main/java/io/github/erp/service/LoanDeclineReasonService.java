package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.domain.LoanDeclineReason;
import io.github.erp.repository.LoanDeclineReasonRepository;
import io.github.erp.repository.search.LoanDeclineReasonSearchRepository;
import io.github.erp.service.dto.LoanDeclineReasonDTO;
import io.github.erp.service.mapper.LoanDeclineReasonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanDeclineReason}.
 */
@Service
@Transactional
public class LoanDeclineReasonService {

    private final Logger log = LoggerFactory.getLogger(LoanDeclineReasonService.class);

    private final LoanDeclineReasonRepository loanDeclineReasonRepository;

    private final LoanDeclineReasonMapper loanDeclineReasonMapper;

    private final LoanDeclineReasonSearchRepository loanDeclineReasonSearchRepository;

    public LoanDeclineReasonService(
        LoanDeclineReasonRepository loanDeclineReasonRepository,
        LoanDeclineReasonMapper loanDeclineReasonMapper,
        LoanDeclineReasonSearchRepository loanDeclineReasonSearchRepository
    ) {
        this.loanDeclineReasonRepository = loanDeclineReasonRepository;
        this.loanDeclineReasonMapper = loanDeclineReasonMapper;
        this.loanDeclineReasonSearchRepository = loanDeclineReasonSearchRepository;
    }

    /**
     * Save a loanDeclineReason.
     *
     * @param loanDeclineReasonDTO the entity to save.
     * @return the persisted entity.
     */
    public LoanDeclineReasonDTO save(LoanDeclineReasonDTO loanDeclineReasonDTO) {
        log.debug("Request to save LoanDeclineReason : {}", loanDeclineReasonDTO);
        LoanDeclineReason loanDeclineReason = loanDeclineReasonMapper.toEntity(loanDeclineReasonDTO);
        loanDeclineReason = loanDeclineReasonRepository.save(loanDeclineReason);
        LoanDeclineReasonDTO result = loanDeclineReasonMapper.toDto(loanDeclineReason);
        loanDeclineReasonSearchRepository.save(loanDeclineReason);
        return result;
    }

    /**
     * Partially update a loanDeclineReason.
     *
     * @param loanDeclineReasonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoanDeclineReasonDTO> partialUpdate(LoanDeclineReasonDTO loanDeclineReasonDTO) {
        log.debug("Request to partially update LoanDeclineReason : {}", loanDeclineReasonDTO);

        return loanDeclineReasonRepository
            .findById(loanDeclineReasonDTO.getId())
            .map(existingLoanDeclineReason -> {
                loanDeclineReasonMapper.partialUpdate(existingLoanDeclineReason, loanDeclineReasonDTO);

                return existingLoanDeclineReason;
            })
            .map(loanDeclineReasonRepository::save)
            .map(savedLoanDeclineReason -> {
                loanDeclineReasonSearchRepository.save(savedLoanDeclineReason);

                return savedLoanDeclineReason;
            })
            .map(loanDeclineReasonMapper::toDto);
    }

    /**
     * Get all the loanDeclineReasons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanDeclineReasonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanDeclineReasons");
        return loanDeclineReasonRepository.findAll(pageable).map(loanDeclineReasonMapper::toDto);
    }

    /**
     * Get one loanDeclineReason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoanDeclineReasonDTO> findOne(Long id) {
        log.debug("Request to get LoanDeclineReason : {}", id);
        return loanDeclineReasonRepository.findById(id).map(loanDeclineReasonMapper::toDto);
    }

    /**
     * Delete the loanDeclineReason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LoanDeclineReason : {}", id);
        loanDeclineReasonRepository.deleteById(id);
        loanDeclineReasonSearchRepository.deleteById(id);
    }

    /**
     * Search for the loanDeclineReason corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanDeclineReasonDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanDeclineReasons for query {}", query);
        return loanDeclineReasonSearchRepository.search(query, pageable).map(loanDeclineReasonMapper::toDto);
    }
}
