package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeaseLiabilityReportItem;
import io.github.erp.repository.LeaseLiabilityReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityReportItemMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityReportItem} entities in the database.
 * The main input is a {@link LeaseLiabilityReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityReportItemDTO} or a {@link Page} of {@link LeaseLiabilityReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityReportItemQueryService extends QueryService<LeaseLiabilityReportItem> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityReportItemQueryService.class);

    private final LeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository;

    private final LeaseLiabilityReportItemMapper leaseLiabilityReportItemMapper;

    private final LeaseLiabilityReportItemSearchRepository leaseLiabilityReportItemSearchRepository;

    public LeaseLiabilityReportItemQueryService(
        LeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository,
        LeaseLiabilityReportItemMapper leaseLiabilityReportItemMapper,
        LeaseLiabilityReportItemSearchRepository leaseLiabilityReportItemSearchRepository
    ) {
        this.leaseLiabilityReportItemRepository = leaseLiabilityReportItemRepository;
        this.leaseLiabilityReportItemMapper = leaseLiabilityReportItemMapper;
        this.leaseLiabilityReportItemSearchRepository = leaseLiabilityReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityReportItemDTO> findByCriteria(LeaseLiabilityReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityReportItem> specification = createSpecification(criteria);
        return leaseLiabilityReportItemMapper.toDto(leaseLiabilityReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityReportItemDTO> findByCriteria(LeaseLiabilityReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityReportItem> specification = createSpecification(criteria);
        return leaseLiabilityReportItemRepository.findAll(specification, page).map(leaseLiabilityReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityReportItem> specification = createSpecification(criteria);
        return leaseLiabilityReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityReportItem> createSpecification(LeaseLiabilityReportItemCriteria criteria) {
        Specification<LeaseLiabilityReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityReportItem_.id));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingId(), LeaseLiabilityReportItem_.bookingId));
            }
            if (criteria.getLeaseTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseTitle(), LeaseLiabilityReportItem_.leaseTitle));
            }
            if (criteria.getLiabilityAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLiabilityAccountNumber(), LeaseLiabilityReportItem_.liabilityAccountNumber)
                    );
            }
            if (criteria.getLiabilityAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLiabilityAmount(), LeaseLiabilityReportItem_.liabilityAmount));
            }
            if (criteria.getInterestPayableAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getInterestPayableAccountNumber(),
                            LeaseLiabilityReportItem_.interestPayableAccountNumber
                        )
                    );
            }
            if (criteria.getInterestPayableAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestPayableAmount(), LeaseLiabilityReportItem_.interestPayableAmount)
                    );
            }
        }
        return specification;
    }
}
