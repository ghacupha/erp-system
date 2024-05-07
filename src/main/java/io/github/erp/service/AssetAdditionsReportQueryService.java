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
import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.repository.AssetAdditionsReportRepository;
import io.github.erp.repository.search.AssetAdditionsReportSearchRepository;
import io.github.erp.service.criteria.AssetAdditionsReportCriteria;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import io.github.erp.service.mapper.AssetAdditionsReportMapper;
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
 * Service for executing complex queries for {@link AssetAdditionsReport} entities in the database.
 * The main input is a {@link AssetAdditionsReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetAdditionsReportDTO} or a {@link Page} of {@link AssetAdditionsReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetAdditionsReportQueryService extends QueryService<AssetAdditionsReport> {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportQueryService.class);

    private final AssetAdditionsReportRepository assetAdditionsReportRepository;

    private final AssetAdditionsReportMapper assetAdditionsReportMapper;

    private final AssetAdditionsReportSearchRepository assetAdditionsReportSearchRepository;

    public AssetAdditionsReportQueryService(
        AssetAdditionsReportRepository assetAdditionsReportRepository,
        AssetAdditionsReportMapper assetAdditionsReportMapper,
        AssetAdditionsReportSearchRepository assetAdditionsReportSearchRepository
    ) {
        this.assetAdditionsReportRepository = assetAdditionsReportRepository;
        this.assetAdditionsReportMapper = assetAdditionsReportMapper;
        this.assetAdditionsReportSearchRepository = assetAdditionsReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetAdditionsReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetAdditionsReportDTO> findByCriteria(AssetAdditionsReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetAdditionsReport> specification = createSpecification(criteria);
        return assetAdditionsReportMapper.toDto(assetAdditionsReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetAdditionsReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportDTO> findByCriteria(AssetAdditionsReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetAdditionsReport> specification = createSpecification(criteria);
        return assetAdditionsReportRepository.findAll(specification, page).map(assetAdditionsReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetAdditionsReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetAdditionsReport> specification = createSpecification(criteria);
        return assetAdditionsReportRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetAdditionsReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetAdditionsReport> createSpecification(AssetAdditionsReportCriteria criteria) {
        Specification<AssetAdditionsReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetAdditionsReport_.id));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), AssetAdditionsReport_.timeOfRequest));
            }
            if (criteria.getReportStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportStartDate(), AssetAdditionsReport_.reportStartDate));
            }
            if (criteria.getReportEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportEndDate(), AssetAdditionsReport_.reportEndDate));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), AssetAdditionsReport_.requestId));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), AssetAdditionsReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), AssetAdditionsReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), AssetAdditionsReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), AssetAdditionsReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(AssetAdditionsReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
