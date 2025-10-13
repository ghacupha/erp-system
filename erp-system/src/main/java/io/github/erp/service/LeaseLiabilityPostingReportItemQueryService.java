package io.github.erp.service;

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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.LeaseLiabilityPostingReportItem;
import io.github.erp.repository.LeaseLiabilityPostingReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityPostingReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityPostingReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityPostingReportItemMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityPostingReportItem} entities in the database.
 * The main input is a {@link LeaseLiabilityPostingReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityPostingReportItemDTO} or a {@link Page} of {@link LeaseLiabilityPostingReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityPostingReportItemQueryService extends QueryService<LeaseLiabilityPostingReportItem> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityPostingReportItemQueryService.class);

    private final LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository;

    private final LeaseLiabilityPostingReportItemMapper leaseLiabilityPostingReportItemMapper;

    private final LeaseLiabilityPostingReportItemSearchRepository leaseLiabilityPostingReportItemSearchRepository;

    public LeaseLiabilityPostingReportItemQueryService(
        LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository,
        LeaseLiabilityPostingReportItemMapper leaseLiabilityPostingReportItemMapper,
        LeaseLiabilityPostingReportItemSearchRepository leaseLiabilityPostingReportItemSearchRepository
    ) {
        this.leaseLiabilityPostingReportItemRepository = leaseLiabilityPostingReportItemRepository;
        this.leaseLiabilityPostingReportItemMapper = leaseLiabilityPostingReportItemMapper;
        this.leaseLiabilityPostingReportItemSearchRepository = leaseLiabilityPostingReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityPostingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityPostingReportItemDTO> findByCriteria(LeaseLiabilityPostingReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityPostingReportItem> specification = createSpecification(criteria);
        return leaseLiabilityPostingReportItemMapper.toDto(leaseLiabilityPostingReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityPostingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityPostingReportItemDTO> findByCriteria(LeaseLiabilityPostingReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityPostingReportItem> specification = createSpecification(criteria);
        return leaseLiabilityPostingReportItemRepository.findAll(specification, page).map(leaseLiabilityPostingReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityPostingReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityPostingReportItem> specification = createSpecification(criteria);
        return leaseLiabilityPostingReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityPostingReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityPostingReportItem> createSpecification(LeaseLiabilityPostingReportItemCriteria criteria) {
        Specification<LeaseLiabilityPostingReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityPostingReportItem_.id));
            }
            if (criteria.getBookingId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getBookingId(), LeaseLiabilityPostingReportItem_.bookingId));
            }
            if (criteria.getLeaseTitle() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLeaseTitle(), LeaseLiabilityPostingReportItem_.leaseTitle));
            }
            if (criteria.getLeaseDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseDescription(), LeaseLiabilityPostingReportItem_.leaseDescription)
                    );
            }
            if (criteria.getAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountNumber(), LeaseLiabilityPostingReportItem_.accountNumber)
                    );
            }
            if (criteria.getPosting() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPosting(), LeaseLiabilityPostingReportItem_.posting));
            }
            if (criteria.getPostingAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPostingAmount(), LeaseLiabilityPostingReportItem_.postingAmount));
            }
        }
        return specification;
    }
}
