package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.NbvReport;
import io.github.erp.repository.NbvReportRepository;
import io.github.erp.repository.search.NbvReportSearchRepository;
import io.github.erp.service.criteria.NbvReportCriteria;
import io.github.erp.service.dto.NbvReportDTO;
import io.github.erp.service.mapper.NbvReportMapper;
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
 * Service for executing complex queries for {@link NbvReport} entities in the database.
 * The main input is a {@link NbvReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NbvReportDTO} or a {@link Page} of {@link NbvReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NbvReportQueryService extends QueryService<NbvReport> {

    private final Logger log = LoggerFactory.getLogger(NbvReportQueryService.class);

    private final NbvReportRepository nbvReportRepository;

    private final NbvReportMapper nbvReportMapper;

    private final NbvReportSearchRepository nbvReportSearchRepository;

    public NbvReportQueryService(
        NbvReportRepository nbvReportRepository,
        NbvReportMapper nbvReportMapper,
        NbvReportSearchRepository nbvReportSearchRepository
    ) {
        this.nbvReportRepository = nbvReportRepository;
        this.nbvReportMapper = nbvReportMapper;
        this.nbvReportSearchRepository = nbvReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NbvReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NbvReportDTO> findByCriteria(NbvReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NbvReport> specification = createSpecification(criteria);
        return nbvReportMapper.toDto(nbvReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NbvReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NbvReportDTO> findByCriteria(NbvReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NbvReport> specification = createSpecification(criteria);
        return nbvReportRepository.findAll(specification, page).map(nbvReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NbvReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NbvReport> specification = createSpecification(criteria);
        return nbvReportRepository.count(specification);
    }

    /**
     * Function to convert {@link NbvReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NbvReport> createSpecification(NbvReportCriteria criteria) {
        Specification<NbvReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NbvReport_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), NbvReport_.reportName));
            }
            if (criteria.getTimeOfReportRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfReportRequest(), NbvReport_.timeOfReportRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), NbvReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), NbvReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), NbvReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportParameters(), NbvReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(NbvReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(NbvReport_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(NbvReport_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(NbvReport_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
