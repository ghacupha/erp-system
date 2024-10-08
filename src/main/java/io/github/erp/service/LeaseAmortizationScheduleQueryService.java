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
import io.github.erp.domain.LeaseAmortizationSchedule;
import io.github.erp.repository.LeaseAmortizationScheduleRepository;
import io.github.erp.repository.search.LeaseAmortizationScheduleSearchRepository;
import io.github.erp.service.criteria.LeaseAmortizationScheduleCriteria;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.mapper.LeaseAmortizationScheduleMapper;
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
 * Service for executing complex queries for {@link LeaseAmortizationSchedule} entities in the database.
 * The main input is a {@link LeaseAmortizationScheduleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseAmortizationScheduleDTO} or a {@link Page} of {@link LeaseAmortizationScheduleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseAmortizationScheduleQueryService extends QueryService<LeaseAmortizationSchedule> {

    private final Logger log = LoggerFactory.getLogger(LeaseAmortizationScheduleQueryService.class);

    private final LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository;

    private final LeaseAmortizationScheduleMapper leaseAmortizationScheduleMapper;

    private final LeaseAmortizationScheduleSearchRepository leaseAmortizationScheduleSearchRepository;

    public LeaseAmortizationScheduleQueryService(
        LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository,
        LeaseAmortizationScheduleMapper leaseAmortizationScheduleMapper,
        LeaseAmortizationScheduleSearchRepository leaseAmortizationScheduleSearchRepository
    ) {
        this.leaseAmortizationScheduleRepository = leaseAmortizationScheduleRepository;
        this.leaseAmortizationScheduleMapper = leaseAmortizationScheduleMapper;
        this.leaseAmortizationScheduleSearchRepository = leaseAmortizationScheduleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseAmortizationScheduleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseAmortizationScheduleDTO> findByCriteria(LeaseAmortizationScheduleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseAmortizationSchedule> specification = createSpecification(criteria);
        return leaseAmortizationScheduleMapper.toDto(leaseAmortizationScheduleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseAmortizationScheduleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationScheduleDTO> findByCriteria(LeaseAmortizationScheduleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseAmortizationSchedule> specification = createSpecification(criteria);
        return leaseAmortizationScheduleRepository.findAll(specification, page).map(leaseAmortizationScheduleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseAmortizationScheduleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseAmortizationSchedule> specification = createSpecification(criteria);
        return leaseAmortizationScheduleRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseAmortizationScheduleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseAmortizationSchedule> createSpecification(LeaseAmortizationScheduleCriteria criteria) {
        Specification<LeaseAmortizationSchedule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseAmortizationSchedule_.id));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), LeaseAmortizationSchedule_.identifier));
            }
            if (criteria.getLeaseLiabilityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseLiabilityId(),
                            root -> root.join(LeaseAmortizationSchedule_.leaseLiability, JoinType.LEFT).get(LeaseLiability_.id)
                        )
                    );
            }
            if (criteria.getLeaseLiabilityScheduleItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseLiabilityScheduleItemId(),
                            root ->
                                root
                                    .join(LeaseAmortizationSchedule_.leaseLiabilityScheduleItems, JoinType.LEFT)
                                    .get(LeaseLiabilityScheduleItem_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseAmortizationSchedule_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
