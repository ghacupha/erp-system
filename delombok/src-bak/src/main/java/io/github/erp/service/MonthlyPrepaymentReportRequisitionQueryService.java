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
import io.github.erp.domain.MonthlyPrepaymentReportRequisition;
import io.github.erp.repository.MonthlyPrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.MonthlyPrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.criteria.MonthlyPrepaymentReportRequisitionCriteria;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.MonthlyPrepaymentReportRequisitionMapper;
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
 * Service for executing complex queries for {@link MonthlyPrepaymentReportRequisition} entities in the database.
 * The main input is a {@link MonthlyPrepaymentReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MonthlyPrepaymentReportRequisitionDTO} or a {@link Page} of {@link MonthlyPrepaymentReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonthlyPrepaymentReportRequisitionQueryService extends QueryService<MonthlyPrepaymentReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(MonthlyPrepaymentReportRequisitionQueryService.class);

    private final MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository;

    private final MonthlyPrepaymentReportRequisitionMapper monthlyPrepaymentReportRequisitionMapper;

    private final MonthlyPrepaymentReportRequisitionSearchRepository monthlyPrepaymentReportRequisitionSearchRepository;

    public MonthlyPrepaymentReportRequisitionQueryService(
        MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository,
        MonthlyPrepaymentReportRequisitionMapper monthlyPrepaymentReportRequisitionMapper,
        MonthlyPrepaymentReportRequisitionSearchRepository monthlyPrepaymentReportRequisitionSearchRepository
    ) {
        this.monthlyPrepaymentReportRequisitionRepository = monthlyPrepaymentReportRequisitionRepository;
        this.monthlyPrepaymentReportRequisitionMapper = monthlyPrepaymentReportRequisitionMapper;
        this.monthlyPrepaymentReportRequisitionSearchRepository = monthlyPrepaymentReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MonthlyPrepaymentReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MonthlyPrepaymentReportRequisitionDTO> findByCriteria(MonthlyPrepaymentReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MonthlyPrepaymentReportRequisition> specification = createSpecification(criteria);
        return monthlyPrepaymentReportRequisitionMapper.toDto(monthlyPrepaymentReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MonthlyPrepaymentReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentReportRequisitionDTO> findByCriteria(MonthlyPrepaymentReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonthlyPrepaymentReportRequisition> specification = createSpecification(criteria);
        return monthlyPrepaymentReportRequisitionRepository
            .findAll(specification, page)
            .map(monthlyPrepaymentReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonthlyPrepaymentReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MonthlyPrepaymentReportRequisition> specification = createSpecification(criteria);
        return monthlyPrepaymentReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link MonthlyPrepaymentReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonthlyPrepaymentReportRequisition> createSpecification(MonthlyPrepaymentReportRequisitionCriteria criteria) {
        Specification<MonthlyPrepaymentReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MonthlyPrepaymentReportRequisition_.id));
            }
            if (criteria.getRequestId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRequestId(), MonthlyPrepaymentReportRequisition_.requestId));
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTimeOfRequisition(), MonthlyPrepaymentReportRequisition_.timeOfRequisition)
                    );
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFileChecksum(), MonthlyPrepaymentReportRequisition_.fileChecksum)
                    );
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), MonthlyPrepaymentReportRequisition_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), MonthlyPrepaymentReportRequisition_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), MonthlyPrepaymentReportRequisition_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(MonthlyPrepaymentReportRequisition_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(MonthlyPrepaymentReportRequisition_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getFiscalYearId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalYearId(),
                            root -> root.join(MonthlyPrepaymentReportRequisition_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
