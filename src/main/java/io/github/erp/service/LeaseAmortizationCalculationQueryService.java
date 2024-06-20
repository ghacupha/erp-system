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
import io.github.erp.domain.LeaseAmortizationCalculation;
import io.github.erp.repository.LeaseAmortizationCalculationRepository;
import io.github.erp.repository.search.LeaseAmortizationCalculationSearchRepository;
import io.github.erp.service.criteria.LeaseAmortizationCalculationCriteria;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.mapper.LeaseAmortizationCalculationMapper;
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
 * Service for executing complex queries for {@link LeaseAmortizationCalculation} entities in the database.
 * The main input is a {@link LeaseAmortizationCalculationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseAmortizationCalculationDTO} or a {@link Page} of {@link LeaseAmortizationCalculationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseAmortizationCalculationQueryService extends QueryService<LeaseAmortizationCalculation> {

    private final Logger log = LoggerFactory.getLogger(LeaseAmortizationCalculationQueryService.class);

    private final LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository;

    private final LeaseAmortizationCalculationMapper leaseAmortizationCalculationMapper;

    private final LeaseAmortizationCalculationSearchRepository leaseAmortizationCalculationSearchRepository;

    public LeaseAmortizationCalculationQueryService(
        LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository,
        LeaseAmortizationCalculationMapper leaseAmortizationCalculationMapper,
        LeaseAmortizationCalculationSearchRepository leaseAmortizationCalculationSearchRepository
    ) {
        this.leaseAmortizationCalculationRepository = leaseAmortizationCalculationRepository;
        this.leaseAmortizationCalculationMapper = leaseAmortizationCalculationMapper;
        this.leaseAmortizationCalculationSearchRepository = leaseAmortizationCalculationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseAmortizationCalculationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseAmortizationCalculationDTO> findByCriteria(LeaseAmortizationCalculationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseAmortizationCalculation> specification = createSpecification(criteria);
        return leaseAmortizationCalculationMapper.toDto(leaseAmortizationCalculationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseAmortizationCalculationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationCalculationDTO> findByCriteria(LeaseAmortizationCalculationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseAmortizationCalculation> specification = createSpecification(criteria);
        return leaseAmortizationCalculationRepository.findAll(specification, page).map(leaseAmortizationCalculationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseAmortizationCalculationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseAmortizationCalculation> specification = createSpecification(criteria);
        return leaseAmortizationCalculationRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseAmortizationCalculationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseAmortizationCalculation> createSpecification(LeaseAmortizationCalculationCriteria criteria) {
        Specification<LeaseAmortizationCalculation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseAmortizationCalculation_.id));
            }
            if (criteria.getInterestRate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getInterestRate(), LeaseAmortizationCalculation_.interestRate));
            }
            if (criteria.getPeriodicity() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPeriodicity(), LeaseAmortizationCalculation_.periodicity));
            }
            if (criteria.getLeaseAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLeaseAmount(), LeaseAmortizationCalculation_.leaseAmount));
            }
            if (criteria.getNumberOfPeriods() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfPeriods(), LeaseAmortizationCalculation_.numberOfPeriods)
                    );
            }
            if (criteria.getLeaseLiabilityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseLiabilityId(),
                            root -> root.join(LeaseAmortizationCalculation_.leaseLiability, JoinType.LEFT).get(LeaseLiability_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseAmortizationCalculation_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
