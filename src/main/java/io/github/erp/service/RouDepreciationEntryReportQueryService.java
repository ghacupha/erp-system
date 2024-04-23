package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.RouDepreciationEntryReport;
import io.github.erp.repository.RouDepreciationEntryReportRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportSearchRepository;
import io.github.erp.service.criteria.RouDepreciationEntryReportCriteria;
import io.github.erp.service.dto.RouDepreciationEntryReportDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportMapper;
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
 * Service for executing complex queries for {@link RouDepreciationEntryReport} entities in the database.
 * The main input is a {@link RouDepreciationEntryReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationEntryReportDTO} or a {@link Page} of {@link RouDepreciationEntryReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationEntryReportQueryService extends QueryService<RouDepreciationEntryReport> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportQueryService.class);

    private final RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository;

    private final RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper;

    private final RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository;

    public RouDepreciationEntryReportQueryService(
        RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository,
        RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper,
        RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository
    ) {
        this.rouDepreciationEntryReportRepository = rouDepreciationEntryReportRepository;
        this.rouDepreciationEntryReportMapper = rouDepreciationEntryReportMapper;
        this.rouDepreciationEntryReportSearchRepository = rouDepreciationEntryReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationEntryReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationEntryReportDTO> findByCriteria(RouDepreciationEntryReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationEntryReport> specification = createSpecification(criteria);
        return rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationEntryReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportDTO> findByCriteria(RouDepreciationEntryReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationEntryReport> specification = createSpecification(criteria);
        return rouDepreciationEntryReportRepository.findAll(specification, page).map(rouDepreciationEntryReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationEntryReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationEntryReport> specification = createSpecification(criteria);
        return rouDepreciationEntryReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationEntryReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationEntryReport> createSpecification(RouDepreciationEntryReportCriteria criteria) {
        Specification<RouDepreciationEntryReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationEntryReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouDepreciationEntryReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouDepreciationEntryReport_.timeOfRequest));
            }
            if (criteria.getReportIsCompiled() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getReportIsCompiled(), RouDepreciationEntryReport_.reportIsCompiled));
            }
            if (criteria.getPeriodStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPeriodStartDate(), RouDepreciationEntryReport_.periodStartDate));
            }
            if (criteria.getPeriodEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPeriodEndDate(), RouDepreciationEntryReport_.periodEndDate));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), RouDepreciationEntryReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouDepreciationEntryReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouDepreciationEntryReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), RouDepreciationEntryReport_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouDepreciationEntryReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
