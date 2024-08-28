package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.LeaseLiabilityScheduleReportItem;
import io.github.erp.repository.LeaseLiabilityScheduleReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityScheduleReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportItemMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LeaseLiabilityScheduleReportItem} entities in the database.
 * The main input is a {@link LeaseLiabilityScheduleReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityScheduleReportItemDTO} or a {@link Page} of {@link LeaseLiabilityScheduleReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityScheduleReportItemQueryService extends QueryService<LeaseLiabilityScheduleReportItem> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportItemQueryService.class);

    private final LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository;

    private final LeaseLiabilityScheduleReportItemMapper leaseLiabilityScheduleReportItemMapper;

    private final LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository;

    public LeaseLiabilityScheduleReportItemQueryService(
        LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository,
        LeaseLiabilityScheduleReportItemMapper leaseLiabilityScheduleReportItemMapper,
        LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository
    ) {
        this.leaseLiabilityScheduleReportItemRepository = leaseLiabilityScheduleReportItemRepository;
        this.leaseLiabilityScheduleReportItemMapper = leaseLiabilityScheduleReportItemMapper;
        this.leaseLiabilityScheduleReportItemSearchRepository = leaseLiabilityScheduleReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityScheduleReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityScheduleReportItemDTO> findByCriteria(LeaseLiabilityScheduleReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleReportItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportItemMapper.toDto(leaseLiabilityScheduleReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityScheduleReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportItemDTO> findByCriteria(LeaseLiabilityScheduleReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityScheduleReportItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportItemRepository.findAll(specification, page).map(leaseLiabilityScheduleReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityScheduleReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleReportItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityScheduleReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityScheduleReportItem> createSpecification(LeaseLiabilityScheduleReportItemCriteria criteria) {
        Specification<LeaseLiabilityScheduleReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityScheduleReportItem_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSequenceNumber(), LeaseLiabilityScheduleReportItem_.sequenceNumber)
                    );
            }
            if (criteria.getOpeningBalance() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOpeningBalance(), LeaseLiabilityScheduleReportItem_.openingBalance)
                    );
            }
            if (criteria.getCashPayment() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCashPayment(), LeaseLiabilityScheduleReportItem_.cashPayment));
            }
            if (criteria.getPrincipalPayment() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPrincipalPayment(), LeaseLiabilityScheduleReportItem_.principalPayment)
                    );
            }
            if (criteria.getInterestPayment() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestPayment(), LeaseLiabilityScheduleReportItem_.interestPayment)
                    );
            }
            if (criteria.getOutstandingBalance() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOutstandingBalance(), LeaseLiabilityScheduleReportItem_.outstandingBalance)
                    );
            }
            if (criteria.getInterestPayableOpening() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getInterestPayableOpening(),
                            LeaseLiabilityScheduleReportItem_.interestPayableOpening
                        )
                    );
            }
            if (criteria.getInterestAccrued() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestAccrued(), LeaseLiabilityScheduleReportItem_.interestAccrued)
                    );
            }
            if (criteria.getInterestPayableClosing() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getInterestPayableClosing(),
                            LeaseLiabilityScheduleReportItem_.interestPayableClosing
                        )
                    );
            }
            if (criteria.getAmortizationScheduleId() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getAmortizationScheduleId(),
                            LeaseLiabilityScheduleReportItem_.amortizationScheduleId
                        )
                    );
            }
        }
        return specification;
    }
}
