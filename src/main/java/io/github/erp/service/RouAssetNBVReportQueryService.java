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
import io.github.erp.domain.RouAssetNBVReport;
import io.github.erp.repository.RouAssetNBVReportRepository;
import io.github.erp.repository.search.RouAssetNBVReportSearchRepository;
import io.github.erp.service.criteria.RouAssetNBVReportCriteria;
import io.github.erp.service.dto.RouAssetNBVReportDTO;
import io.github.erp.service.mapper.RouAssetNBVReportMapper;
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
 * Service for executing complex queries for {@link RouAssetNBVReport} entities in the database.
 * The main input is a {@link RouAssetNBVReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAssetNBVReportDTO} or a {@link Page} of {@link RouAssetNBVReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAssetNBVReportQueryService extends QueryService<RouAssetNBVReport> {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportQueryService.class);

    private final RouAssetNBVReportRepository rouAssetNBVReportRepository;

    private final RouAssetNBVReportMapper rouAssetNBVReportMapper;

    private final RouAssetNBVReportSearchRepository rouAssetNBVReportSearchRepository;

    public RouAssetNBVReportQueryService(
        RouAssetNBVReportRepository rouAssetNBVReportRepository,
        RouAssetNBVReportMapper rouAssetNBVReportMapper,
        RouAssetNBVReportSearchRepository rouAssetNBVReportSearchRepository
    ) {
        this.rouAssetNBVReportRepository = rouAssetNBVReportRepository;
        this.rouAssetNBVReportMapper = rouAssetNBVReportMapper;
        this.rouAssetNBVReportSearchRepository = rouAssetNBVReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAssetNBVReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAssetNBVReportDTO> findByCriteria(RouAssetNBVReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAssetNBVReport> specification = createSpecification(criteria);
        return rouAssetNBVReportMapper.toDto(rouAssetNBVReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAssetNBVReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportDTO> findByCriteria(RouAssetNBVReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAssetNBVReport> specification = createSpecification(criteria);
        return rouAssetNBVReportRepository.findAll(specification, page).map(rouAssetNBVReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAssetNBVReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAssetNBVReport> specification = createSpecification(criteria);
        return rouAssetNBVReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAssetNBVReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAssetNBVReport> createSpecification(RouAssetNBVReportCriteria criteria) {
        Specification<RouAssetNBVReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAssetNBVReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouAssetNBVReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouAssetNBVReport_.timeOfRequest));
            }
            if (criteria.getReportIsCompiled() != null) {
                specification = specification.and(buildSpecification(criteria.getReportIsCompiled(), RouAssetNBVReport_.reportIsCompiled));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), RouAssetNBVReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouAssetNBVReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouAssetNBVReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), RouAssetNBVReport_.reportParameters));
            }
            if (criteria.getFiscalReportingMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalReportingMonthId(),
                            root -> root.join(RouAssetNBVReport_.fiscalReportingMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouAssetNBVReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
