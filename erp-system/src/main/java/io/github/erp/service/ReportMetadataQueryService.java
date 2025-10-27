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

import io.github.erp.domain.ReportMetadata;
import io.github.erp.domain.ReportMetadata_;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.repository.search.ReportMetadataSearchRepository;
import io.github.erp.service.criteria.ReportMetadataCriteria;
import io.github.erp.service.dto.ReportMetadataDTO;
import io.github.erp.service.mapper.ReportMetadataMapper;
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
 * Service for executing complex queries for {@link ReportMetadata} entities in the database.
 * The main input is a {@link ReportMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportMetadataDTO} or a {@link Page} of {@link ReportMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportMetadataQueryService extends QueryService<ReportMetadata> {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataQueryService.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataMapper reportMetadataMapper;

    private final ReportMetadataSearchRepository reportMetadataSearchRepository;

    public ReportMetadataQueryService(
        ReportMetadataRepository reportMetadataRepository,
        ReportMetadataMapper reportMetadataMapper,
        ReportMetadataSearchRepository reportMetadataSearchRepository
    ) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataMapper = reportMetadataMapper;
        this.reportMetadataSearchRepository = reportMetadataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportMetadataDTO> findByCriteria(ReportMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportMetadata> specification = createSpecification(criteria);
        return reportMetadataMapper.toDto(reportMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportMetadataDTO> findByCriteria(ReportMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportMetadata> specification = createSpecification(criteria);
        return reportMetadataRepository.findAll(specification, page).map(reportMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportMetadata> specification = createSpecification(criteria);
        return reportMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportMetadataCriteria} to a {@link Specification}.
     */
    protected Specification<ReportMetadata> createSpecification(ReportMetadataCriteria criteria) {
        Specification<ReportMetadata> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportMetadata_.id));
            }
            if (criteria.getReportTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportTitle(), ReportMetadata_.reportTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ReportMetadata_.description));
            }
            if (criteria.getModule() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModule(), ReportMetadata_.module));
            }
            if (criteria.getPagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPagePath(), ReportMetadata_.pagePath));
            }
            if (criteria.getBackendApi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBackendApi(), ReportMetadata_.backendApi));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), ReportMetadata_.active));
            }
            if (criteria.getFilterLabel() != null) {
                specification = specification.and(
                    buildStringSpecification(
                        criteria.getFilterLabel(),
                        root -> root.join(ReportMetadata_.filters, JoinType.LEFT).get("label")
                    )
                );
            }
            if (criteria.getFilterQueryParameterKey() != null) {
                specification = specification.and(
                    buildStringSpecification(
                        criteria.getFilterQueryParameterKey(),
                        root -> root.join(ReportMetadata_.filters, JoinType.LEFT).get("queryParameterKey")
                    )
                );
            }
            if (criteria.getFilterValueSource() != null) {
                specification = specification.and(
                    buildStringSpecification(
                        criteria.getFilterValueSource(),
                        root -> root.join(ReportMetadata_.filters, JoinType.LEFT).get("valueSource")
                    )
                );
            }
            if (criteria.getFilterUiHint() != null) {
                specification = specification.and(
                    buildStringSpecification(
                        criteria.getFilterUiHint(),
                        root -> root.join(ReportMetadata_.filters, JoinType.LEFT).get("uiHint")
                    )
                );
            }
        }
        return specification;
    }
}
