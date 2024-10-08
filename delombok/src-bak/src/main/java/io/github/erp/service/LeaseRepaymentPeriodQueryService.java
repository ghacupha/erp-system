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
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.repository.search.LeaseRepaymentPeriodSearchRepository;
import io.github.erp.service.criteria.LeaseRepaymentPeriodCriteria;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import io.github.erp.service.mapper.LeaseRepaymentPeriodMapper;
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
 * Service for executing complex queries for {@link LeaseRepaymentPeriod} entities in the database.
 * The main input is a {@link LeaseRepaymentPeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseRepaymentPeriodDTO} or a {@link Page} of {@link LeaseRepaymentPeriodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseRepaymentPeriodQueryService extends QueryService<LeaseRepaymentPeriod> {

    private final Logger log = LoggerFactory.getLogger(LeaseRepaymentPeriodQueryService.class);

    private final LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;

    private final LeaseRepaymentPeriodMapper leaseRepaymentPeriodMapper;

    private final LeaseRepaymentPeriodSearchRepository leaseRepaymentPeriodSearchRepository;

    public LeaseRepaymentPeriodQueryService(
        LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository,
        LeaseRepaymentPeriodMapper leaseRepaymentPeriodMapper,
        LeaseRepaymentPeriodSearchRepository leaseRepaymentPeriodSearchRepository
    ) {
        this.leaseRepaymentPeriodRepository = leaseRepaymentPeriodRepository;
        this.leaseRepaymentPeriodMapper = leaseRepaymentPeriodMapper;
        this.leaseRepaymentPeriodSearchRepository = leaseRepaymentPeriodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseRepaymentPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseRepaymentPeriodDTO> findByCriteria(LeaseRepaymentPeriodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseRepaymentPeriod> specification = createSpecification(criteria);
        return leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseRepaymentPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseRepaymentPeriodDTO> findByCriteria(LeaseRepaymentPeriodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseRepaymentPeriod> specification = createSpecification(criteria);
        return leaseRepaymentPeriodRepository.findAll(specification, page).map(leaseRepaymentPeriodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseRepaymentPeriodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseRepaymentPeriod> specification = createSpecification(criteria);
        return leaseRepaymentPeriodRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseRepaymentPeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseRepaymentPeriod> createSpecification(LeaseRepaymentPeriodCriteria criteria) {
        Specification<LeaseRepaymentPeriod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseRepaymentPeriod_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSequenceNumber(), LeaseRepaymentPeriod_.sequenceNumber));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), LeaseRepaymentPeriod_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), LeaseRepaymentPeriod_.endDate));
            }
            if (criteria.getPeriodCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodCode(), LeaseRepaymentPeriod_.periodCode));
            }
            if (criteria.getFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalMonthId(),
                            root -> root.join(LeaseRepaymentPeriod_.fiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
