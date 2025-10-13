package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.internal.repository.InternalPrepaymentAmortizationRepository;
import io.github.erp.internal.service.cache.ScheduledCacheRefreshService;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.PrepaymentAmortizationQueryService;
import io.github.erp.service.criteria.PrepaymentAmortizationCriteria;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.mapper.PrepaymentAmortizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentAmortization}.
 */
@Service
@Transactional
public class InternalPrepaymentAmortizationServiceImpl implements InternalPrepaymentAmortizationService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentAmortizationServiceImpl.class);

    private final InternalPrepaymentAmortizationRepository prepaymentAmortizationRepository;

    private final PrepaymentAmortizationMapper prepaymentAmortizationMapper;

    private final PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository;

    private final PrepaymentAmortizationQueryService prepaymentAmortizationQueryService;

    private final ScheduledCacheRefreshService scheduledTransactionAccountCacheRefreshService;

    public InternalPrepaymentAmortizationServiceImpl(
        InternalPrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationMapper prepaymentAmortizationMapper,
        PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository,
        PrepaymentAmortizationQueryService prepaymentAmortizationQueryService,
        ScheduledCacheRefreshService scheduledTransactionAccountCacheRefreshService
    ) {
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationMapper = prepaymentAmortizationMapper;
        this.prepaymentAmortizationSearchRepository = prepaymentAmortizationSearchRepository;
        this.prepaymentAmortizationQueryService = prepaymentAmortizationQueryService;
        this.scheduledTransactionAccountCacheRefreshService = scheduledTransactionAccountCacheRefreshService;
    }

    @Override
    public PrepaymentAmortizationDTO save(PrepaymentAmortizationDTO prepaymentAmortizationDTO) {
        log.debug("Request to save PrepaymentAmortization : {}", prepaymentAmortizationDTO);
        PrepaymentAmortization prepaymentAmortization = prepaymentAmortizationMapper.toEntity(prepaymentAmortizationDTO);
        prepaymentAmortization = prepaymentAmortizationRepository.save(prepaymentAmortization);
        PrepaymentAmortizationDTO result = prepaymentAmortizationMapper.toDto(prepaymentAmortization);
        prepaymentAmortizationSearchRepository.save(prepaymentAmortization);
        return result;
    }

    @Override
    public Optional<PrepaymentAmortizationDTO> partialUpdate(PrepaymentAmortizationDTO prepaymentAmortizationDTO) {
        log.debug("Request to partially update PrepaymentAmortization : {}", prepaymentAmortizationDTO);

        return prepaymentAmortizationRepository
            .findById(prepaymentAmortizationDTO.getId())
            .map(existingPrepaymentAmortization -> {
                prepaymentAmortizationMapper.partialUpdate(existingPrepaymentAmortization, prepaymentAmortizationDTO);

                return existingPrepaymentAmortization;
            })
            .map(prepaymentAmortizationRepository::save)
            .map(savedPrepaymentAmortization -> {
                prepaymentAmortizationSearchRepository.save(savedPrepaymentAmortization);

                return savedPrepaymentAmortization;
            })
            .map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAmortizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentAmortizations");
        return prepaymentAmortizationRepository.findAll(pageable).map(prepaymentAmortizationMapper::toDto);
    }

    public Page<PrepaymentAmortizationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentAmortizationRepository.findAllWithEagerRelationships(pageable).map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentAmortizationDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentAmortization : {}", id);
        return prepaymentAmortizationRepository.findOneWithEagerRelationships(id).map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentAmortization : {}", id);
        prepaymentAmortizationRepository.deleteById(id);
        prepaymentAmortizationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAmortizationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentAmortizations for query {}", query);
        return prepaymentAmortizationSearchRepository.search(query, pageable).map(prepaymentAmortizationMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link PrepaymentAmortizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param pageable The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PrepaymentAmortizationDTO> findByCriteria(PrepaymentAmortizationCriteria criteria, Pageable pageable) {

        scheduledTransactionAccountCacheRefreshService.refreshDefinedCacheItems(prepaymentAmortizationRepository.findAdjacentIds());

        return prepaymentAmortizationQueryService.findByCriteria(criteria, pageable);
    }
}
