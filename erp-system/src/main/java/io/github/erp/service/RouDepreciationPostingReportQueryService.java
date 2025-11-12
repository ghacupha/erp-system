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
import io.github.erp.domain.RouDepreciationPostingReport;
import io.github.erp.repository.RouDepreciationPostingReportRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportSearchRepository;
import io.github.erp.service.criteria.RouDepreciationPostingReportCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportMapper;
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
 * Service for executing complex queries for {@link RouDepreciationPostingReport} entities in the database.
 * The main input is a {@link RouDepreciationPostingReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationPostingReportDTO} or a {@link Page} of {@link RouDepreciationPostingReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationPostingReportQueryService extends QueryService<RouDepreciationPostingReport> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportQueryService.class);

    private final RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository;

    private final RouDepreciationPostingReportMapper rouDepreciationPostingReportMapper;

    private final RouDepreciationPostingReportSearchRepository rouDepreciationPostingReportSearchRepository;

    public RouDepreciationPostingReportQueryService(
        RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository,
        RouDepreciationPostingReportMapper rouDepreciationPostingReportMapper,
        RouDepreciationPostingReportSearchRepository rouDepreciationPostingReportSearchRepository
    ) {
        this.rouDepreciationPostingReportRepository = rouDepreciationPostingReportRepository;
        this.rouDepreciationPostingReportMapper = rouDepreciationPostingReportMapper;
        this.rouDepreciationPostingReportSearchRepository = rouDepreciationPostingReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationPostingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationPostingReportDTO> findByCriteria(RouDepreciationPostingReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationPostingReport> specification = createSpecification(criteria);
        return rouDepreciationPostingReportMapper.toDto(rouDepreciationPostingReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationPostingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportDTO> findByCriteria(RouDepreciationPostingReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationPostingReport> specification = createSpecification(criteria);
        return rouDepreciationPostingReportRepository.findAll(specification, page).map(rouDepreciationPostingReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationPostingReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationPostingReport> specification = createSpecification(criteria);
        return rouDepreciationPostingReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationPostingReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationPostingReport> createSpecification(RouDepreciationPostingReportCriteria criteria) {
        Specification<RouDepreciationPostingReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationPostingReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouDepreciationPostingReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouDepreciationPostingReport_.timeOfRequest));
            }
            if (criteria.getReportIsCompiled() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getReportIsCompiled(), RouDepreciationPostingReport_.reportIsCompiled));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), RouDepreciationPostingReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouDepreciationPostingReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouDepreciationPostingReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), RouDepreciationPostingReport_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouDepreciationPostingReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLeasePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeasePeriodId(),
                            root -> root.join(RouDepreciationPostingReport_.leasePeriod, JoinType.LEFT).get(LeasePeriod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
