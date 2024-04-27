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
import io.github.erp.domain.DepreciationReport;
import io.github.erp.repository.DepreciationReportRepository;
import io.github.erp.repository.search.DepreciationReportSearchRepository;
import io.github.erp.service.criteria.DepreciationReportCriteria;
import io.github.erp.service.dto.DepreciationReportDTO;
import io.github.erp.service.mapper.DepreciationReportMapper;
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
 * Service for executing complex queries for {@link DepreciationReport} entities in the database.
 * The main input is a {@link DepreciationReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationReportDTO} or a {@link Page} of {@link DepreciationReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationReportQueryService extends QueryService<DepreciationReport> {

    private final Logger log = LoggerFactory.getLogger(DepreciationReportQueryService.class);

    private final DepreciationReportRepository depreciationReportRepository;

    private final DepreciationReportMapper depreciationReportMapper;

    private final DepreciationReportSearchRepository depreciationReportSearchRepository;

    public DepreciationReportQueryService(
        DepreciationReportRepository depreciationReportRepository,
        DepreciationReportMapper depreciationReportMapper,
        DepreciationReportSearchRepository depreciationReportSearchRepository
    ) {
        this.depreciationReportRepository = depreciationReportRepository;
        this.depreciationReportMapper = depreciationReportMapper;
        this.depreciationReportSearchRepository = depreciationReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationReportDTO> findByCriteria(DepreciationReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationReport> specification = createSpecification(criteria);
        return depreciationReportMapper.toDto(depreciationReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationReportDTO> findByCriteria(DepreciationReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationReport> specification = createSpecification(criteria);
        return depreciationReportRepository.findAll(specification, page).map(depreciationReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationReport> specification = createSpecification(criteria);
        return depreciationReportRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationReport> createSpecification(DepreciationReportCriteria criteria) {
        Specification<DepreciationReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationReport_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), DepreciationReport_.reportName));
            }
            if (criteria.getTimeOfReportRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfReportRequest(), DepreciationReport_.timeOfReportRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), DepreciationReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), DepreciationReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), DepreciationReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), DepreciationReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(DepreciationReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(DepreciationReport_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(DepreciationReport_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(DepreciationReport_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
