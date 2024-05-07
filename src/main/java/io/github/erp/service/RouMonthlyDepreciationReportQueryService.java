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
import io.github.erp.domain.RouMonthlyDepreciationReport;
import io.github.erp.repository.RouMonthlyDepreciationReportRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepository;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportMapper;
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
 * Service for executing complex queries for {@link RouMonthlyDepreciationReport} entities in the database.
 * The main input is a {@link RouMonthlyDepreciationReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouMonthlyDepreciationReportDTO} or a {@link Page} of {@link RouMonthlyDepreciationReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouMonthlyDepreciationReportQueryService extends QueryService<RouMonthlyDepreciationReport> {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportQueryService.class);

    private final RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository;

    private final RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper;

    private final RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository;

    public RouMonthlyDepreciationReportQueryService(
        RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository,
        RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper,
        RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository
    ) {
        this.rouMonthlyDepreciationReportRepository = rouMonthlyDepreciationReportRepository;
        this.rouMonthlyDepreciationReportMapper = rouMonthlyDepreciationReportMapper;
        this.rouMonthlyDepreciationReportSearchRepository = rouMonthlyDepreciationReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouMonthlyDepreciationReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouMonthlyDepreciationReportDTO> findByCriteria(RouMonthlyDepreciationReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouMonthlyDepreciationReport> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportMapper.toDto(rouMonthlyDepreciationReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouMonthlyDepreciationReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportDTO> findByCriteria(RouMonthlyDepreciationReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouMonthlyDepreciationReport> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportRepository.findAll(specification, page).map(rouMonthlyDepreciationReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouMonthlyDepreciationReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouMonthlyDepreciationReport> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouMonthlyDepreciationReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouMonthlyDepreciationReport> createSpecification(RouMonthlyDepreciationReportCriteria criteria) {
        Specification<RouMonthlyDepreciationReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouMonthlyDepreciationReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouMonthlyDepreciationReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouMonthlyDepreciationReport_.timeOfRequest));
            }
            if (criteria.getReportIsCompiled() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getReportIsCompiled(), RouMonthlyDepreciationReport_.reportIsCompiled));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), RouMonthlyDepreciationReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouMonthlyDepreciationReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouMonthlyDepreciationReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), RouMonthlyDepreciationReport_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouMonthlyDepreciationReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getReportingYearId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportingYearId(),
                            root -> root.join(RouMonthlyDepreciationReport_.reportingYear, JoinType.LEFT).get(FiscalYear_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
