package io.github.erp.service;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.ReportRequisition;
import io.github.erp.repository.ReportRequisitionRepository;
import io.github.erp.repository.search.ReportRequisitionSearchRepository;
import io.github.erp.service.criteria.ReportRequisitionCriteria;
import io.github.erp.service.dto.ReportRequisitionDTO;
import io.github.erp.service.mapper.ReportRequisitionMapper;
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
 * Service for executing complex queries for {@link ReportRequisition} entities in the database.
 * The main input is a {@link ReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportRequisitionDTO} or a {@link Page} of {@link ReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportRequisitionQueryService extends QueryService<ReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(ReportRequisitionQueryService.class);

    private final ReportRequisitionRepository reportRequisitionRepository;

    private final ReportRequisitionMapper reportRequisitionMapper;

    private final ReportRequisitionSearchRepository reportRequisitionSearchRepository;

    public ReportRequisitionQueryService(
        ReportRequisitionRepository reportRequisitionRepository,
        ReportRequisitionMapper reportRequisitionMapper,
        ReportRequisitionSearchRepository reportRequisitionSearchRepository
    ) {
        this.reportRequisitionRepository = reportRequisitionRepository;
        this.reportRequisitionMapper = reportRequisitionMapper;
        this.reportRequisitionSearchRepository = reportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportRequisitionDTO> findByCriteria(ReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportRequisition> specification = createSpecification(criteria);
        return reportRequisitionMapper.toDto(reportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportRequisitionDTO> findByCriteria(ReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportRequisition> specification = createSpecification(criteria);
        return reportRequisitionRepository.findAll(specification, page).map(reportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportRequisition> specification = createSpecification(criteria);
        return reportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportRequisition> createSpecification(ReportRequisitionCriteria criteria) {
        Specification<ReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportRequisition_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), ReportRequisition_.reportName));
            }
            if (criteria.getReportRequestTime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportRequestTime(), ReportRequisition_.reportRequestTime));
            }
            if (criteria.getReportPassword() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportPassword(), ReportRequisition_.reportPassword));
            }
            if (criteria.getReportStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReportStatus(), ReportRequisition_.reportStatus));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), ReportRequisition_.reportId));
            }
            if (criteria.getPlaceholdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholdersId(),
                            root -> root.join(ReportRequisition_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(ReportRequisition_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getReportTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportTemplateId(),
                            root -> root.join(ReportRequisition_.reportTemplate, JoinType.LEFT).get(ReportTemplate_.id)
                        )
                    );
            }
            if (criteria.getReportContentTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportContentTypeId(),
                            root -> root.join(ReportRequisition_.reportContentType, JoinType.LEFT).get(ReportContentType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
