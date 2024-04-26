package io.github.erp.service;

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
