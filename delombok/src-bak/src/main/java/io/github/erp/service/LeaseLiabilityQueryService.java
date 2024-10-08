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
import io.github.erp.domain.LeaseLiability;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityCriteria;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
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
 * Service for executing complex queries for {@link LeaseLiability} entities in the database.
 * The main input is a {@link LeaseLiabilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityDTO} or a {@link Page} of {@link LeaseLiabilityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityQueryService extends QueryService<LeaseLiability> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityQueryService.class);

    private final LeaseLiabilityRepository leaseLiabilityRepository;

    private final LeaseLiabilityMapper leaseLiabilityMapper;

    private final LeaseLiabilitySearchRepository leaseLiabilitySearchRepository;

    public LeaseLiabilityQueryService(
        LeaseLiabilityRepository leaseLiabilityRepository,
        LeaseLiabilityMapper leaseLiabilityMapper,
        LeaseLiabilitySearchRepository leaseLiabilitySearchRepository
    ) {
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseLiabilityMapper = leaseLiabilityMapper;
        this.leaseLiabilitySearchRepository = leaseLiabilitySearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityDTO> findByCriteria(LeaseLiabilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiability> specification = createSpecification(criteria);
        return leaseLiabilityMapper.toDto(leaseLiabilityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityDTO> findByCriteria(LeaseLiabilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiability> specification = createSpecification(criteria);
        return leaseLiabilityRepository.findAll(specification, page).map(leaseLiabilityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiability> specification = createSpecification(criteria);
        return leaseLiabilityRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiability> createSpecification(LeaseLiabilityCriteria criteria) {
        Specification<LeaseLiability> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiability_.id));
            }
            if (criteria.getLeaseId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseId(), LeaseLiability_.leaseId));
            }
            if (criteria.getLiabilityAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLiabilityAmount(), LeaseLiability_.liabilityAmount));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), LeaseLiability_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), LeaseLiability_.endDate));
            }
            if (criteria.getInterestRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInterestRate(), LeaseLiability_.interestRate));
            }
            if (criteria.getLeaseAmortizationCalculationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseAmortizationCalculationId(),
                            root ->
                                root.join(LeaseLiability_.leaseAmortizationCalculation, JoinType.LEFT).get(LeaseAmortizationCalculation_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseLiability_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
