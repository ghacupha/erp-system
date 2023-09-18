package io.github.erp.service;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.ReportDesign;
import io.github.erp.repository.ReportDesignRepository;
import io.github.erp.repository.search.ReportDesignSearchRepository;
import io.github.erp.service.criteria.ReportDesignCriteria;
import io.github.erp.service.dto.ReportDesignDTO;
import io.github.erp.service.mapper.ReportDesignMapper;
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
 * Service for executing complex queries for {@link ReportDesign} entities in the database.
 * The main input is a {@link ReportDesignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportDesignDTO} or a {@link Page} of {@link ReportDesignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportDesignQueryService extends QueryService<ReportDesign> {

    private final Logger log = LoggerFactory.getLogger(ReportDesignQueryService.class);

    private final ReportDesignRepository reportDesignRepository;

    private final ReportDesignMapper reportDesignMapper;

    private final ReportDesignSearchRepository reportDesignSearchRepository;

    public ReportDesignQueryService(
        ReportDesignRepository reportDesignRepository,
        ReportDesignMapper reportDesignMapper,
        ReportDesignSearchRepository reportDesignSearchRepository
    ) {
        this.reportDesignRepository = reportDesignRepository;
        this.reportDesignMapper = reportDesignMapper;
        this.reportDesignSearchRepository = reportDesignSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportDesignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDesignDTO> findByCriteria(ReportDesignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportDesign> specification = createSpecification(criteria);
        return reportDesignMapper.toDto(reportDesignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportDesignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportDesignDTO> findByCriteria(ReportDesignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportDesign> specification = createSpecification(criteria);
        return reportDesignRepository.findAll(specification, page).map(reportDesignMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportDesignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportDesign> specification = createSpecification(criteria);
        return reportDesignRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportDesignCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportDesign> createSpecification(ReportDesignCriteria criteria) {
        Specification<ReportDesign> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportDesign_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getCatalogueNumber(), ReportDesign_.catalogueNumber));
            }
            if (criteria.getDesignation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignation(), ReportDesign_.designation));
            }
            if (criteria.getReportFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportFileChecksum(), ReportDesign_.reportFileChecksum));
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(ReportDesign_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(ReportDesign_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getReportDesignerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportDesignerId(),
                            root -> root.join(ReportDesign_.reportDesigner, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(ReportDesign_.organization, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(ReportDesign_.department, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ReportDesign_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSystemModuleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemModuleId(),
                            root -> root.join(ReportDesign_.systemModule, JoinType.LEFT).get(SystemModule_.id)
                        )
                    );
            }
            if (criteria.getFileCheckSumAlgorithmId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFileCheckSumAlgorithmId(),
                            root -> root.join(ReportDesign_.fileCheckSumAlgorithm, JoinType.LEFT).get(Algorithm_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
