package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityScheduleItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityScheduleItem} entities in the database.
 * The main input is a {@link LeaseLiabilityScheduleItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityScheduleItemDTO} or a {@link Page} of {@link LeaseLiabilityScheduleItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityScheduleItemQueryService extends QueryService<LeaseLiabilityScheduleItem> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemQueryService.class);

    private final LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    private final LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    private final LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository;

    public LeaseLiabilityScheduleItemQueryService(
        LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper,
        LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository
    ) {
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityScheduleItemMapper = leaseLiabilityScheduleItemMapper;
        this.leaseLiabilityScheduleItemSearchRepository = leaseLiabilityScheduleItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityScheduleItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityScheduleItemDTO> findByCriteria(LeaseLiabilityScheduleItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityScheduleItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleItemDTO> findByCriteria(LeaseLiabilityScheduleItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityScheduleItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleItemRepository.findAll(specification, page).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityScheduleItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleItem> specification = createSpecification(criteria);
        return leaseLiabilityScheduleItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityScheduleItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityScheduleItem> createSpecification(LeaseLiabilityScheduleItemCriteria criteria) {
        Specification<LeaseLiabilityScheduleItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityScheduleItem_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSequenceNumber(), LeaseLiabilityScheduleItem_.sequenceNumber));
            }
            if (criteria.getPeriodIncluded() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPeriodIncluded(), LeaseLiabilityScheduleItem_.periodIncluded));
            }
            if (criteria.getPeriodStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPeriodStartDate(), LeaseLiabilityScheduleItem_.periodStartDate));
            }
            if (criteria.getPeriodEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPeriodEndDate(), LeaseLiabilityScheduleItem_.periodEndDate));
            }
            if (criteria.getOpeningBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOpeningBalance(), LeaseLiabilityScheduleItem_.openingBalance));
            }
            if (criteria.getCashPayment() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCashPayment(), LeaseLiabilityScheduleItem_.cashPayment));
            }
            if (criteria.getPrincipalPayment() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPrincipalPayment(), LeaseLiabilityScheduleItem_.principalPayment)
                    );
            }
            if (criteria.getInterestPayment() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getInterestPayment(), LeaseLiabilityScheduleItem_.interestPayment));
            }
            if (criteria.getOutstandingBalance() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOutstandingBalance(), LeaseLiabilityScheduleItem_.outstandingBalance)
                    );
            }
            if (criteria.getInterestPayableOpening() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestPayableOpening(), LeaseLiabilityScheduleItem_.interestPayableOpening)
                    );
            }
            if (criteria.getInterestExpenseAccrued() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestExpenseAccrued(), LeaseLiabilityScheduleItem_.interestExpenseAccrued)
                    );
            }
            if (criteria.getInterestPayableBalance() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInterestPayableBalance(), LeaseLiabilityScheduleItem_.interestPayableBalance)
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(LeaseLiabilityScheduleItem_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseLiabilityScheduleItem_.leaseContract, JoinType.LEFT).get(LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getLeaseModelMetadataId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseModelMetadataId(),
                            root -> root.join(LeaseLiabilityScheduleItem_.leaseModelMetadata, JoinType.LEFT).get(LeaseModelMetadata_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root ->
                                root
                                    .join(LeaseLiabilityScheduleItem_.universallyUniqueMappings, JoinType.LEFT)
                                    .get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
