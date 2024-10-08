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
import io.github.erp.domain.RouAssetListReport;
import io.github.erp.repository.RouAssetListReportRepository;
import io.github.erp.repository.search.RouAssetListReportSearchRepository;
import io.github.erp.service.criteria.RouAssetListReportCriteria;
import io.github.erp.service.dto.RouAssetListReportDTO;
import io.github.erp.service.mapper.RouAssetListReportMapper;
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
 * Service for executing complex queries for {@link RouAssetListReport} entities in the database.
 * The main input is a {@link RouAssetListReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAssetListReportDTO} or a {@link Page} of {@link RouAssetListReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAssetListReportQueryService extends QueryService<RouAssetListReport> {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportQueryService.class);

    private final RouAssetListReportRepository rouAssetListReportRepository;

    private final RouAssetListReportMapper rouAssetListReportMapper;

    private final RouAssetListReportSearchRepository rouAssetListReportSearchRepository;

    public RouAssetListReportQueryService(
        RouAssetListReportRepository rouAssetListReportRepository,
        RouAssetListReportMapper rouAssetListReportMapper,
        RouAssetListReportSearchRepository rouAssetListReportSearchRepository
    ) {
        this.rouAssetListReportRepository = rouAssetListReportRepository;
        this.rouAssetListReportMapper = rouAssetListReportMapper;
        this.rouAssetListReportSearchRepository = rouAssetListReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAssetListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAssetListReportDTO> findByCriteria(RouAssetListReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportMapper.toDto(rouAssetListReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAssetListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAssetListReportDTO> findByCriteria(RouAssetListReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportRepository.findAll(specification, page).map(rouAssetListReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAssetListReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAssetListReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAssetListReport> createSpecification(RouAssetListReportCriteria criteria) {
        Specification<RouAssetListReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAssetListReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouAssetListReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouAssetListReport_.timeOfRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), RouAssetListReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouAssetListReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouAssetListReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), RouAssetListReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouAssetListReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
