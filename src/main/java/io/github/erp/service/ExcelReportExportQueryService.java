package io.github.erp.service;

/*-
 * Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
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
import io.github.erp.domain.ExcelReportExport;
import io.github.erp.repository.ExcelReportExportRepository;
import io.github.erp.repository.search.ExcelReportExportSearchRepository;
import io.github.erp.service.criteria.ExcelReportExportCriteria;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.mapper.ExcelReportExportMapper;
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
 * Service for executing complex queries for {@link ExcelReportExport} entities in the database.
 * The main input is a {@link ExcelReportExportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExcelReportExportDTO} or a {@link Page} of {@link ExcelReportExportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExcelReportExportQueryService extends QueryService<ExcelReportExport> {

    private final Logger log = LoggerFactory.getLogger(ExcelReportExportQueryService.class);

    private final ExcelReportExportRepository excelReportExportRepository;

    private final ExcelReportExportMapper excelReportExportMapper;

    private final ExcelReportExportSearchRepository excelReportExportSearchRepository;

    public ExcelReportExportQueryService(
        ExcelReportExportRepository excelReportExportRepository,
        ExcelReportExportMapper excelReportExportMapper,
        ExcelReportExportSearchRepository excelReportExportSearchRepository
    ) {
        this.excelReportExportRepository = excelReportExportRepository;
        this.excelReportExportMapper = excelReportExportMapper;
        this.excelReportExportSearchRepository = excelReportExportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ExcelReportExportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExcelReportExportDTO> findByCriteria(ExcelReportExportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExcelReportExport> specification = createSpecification(criteria);
        return excelReportExportMapper.toDto(excelReportExportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExcelReportExportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExcelReportExportDTO> findByCriteria(ExcelReportExportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExcelReportExport> specification = createSpecification(criteria);
        return excelReportExportRepository.findAll(specification, page).map(excelReportExportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExcelReportExportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExcelReportExport> specification = createSpecification(criteria);
        return excelReportExportRepository.count(specification);
    }

    /**
     * Function to convert {@link ExcelReportExportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExcelReportExport> createSpecification(ExcelReportExportCriteria criteria) {
        Specification<ExcelReportExport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExcelReportExport_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), ExcelReportExport_.reportName));
            }
            if (criteria.getReportPassword() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportPassword(), ExcelReportExport_.reportPassword));
            }
            if (criteria.getReportTimeStamp() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportTimeStamp(), ExcelReportExport_.reportTimeStamp));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), ExcelReportExport_.reportId));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ExcelReportExport_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(ExcelReportExport_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getReportStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportStatusId(),
                            root -> root.join(ExcelReportExport_.reportStatus, JoinType.LEFT).get(ReportStatus_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(ExcelReportExport_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getReportCreatorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportCreatorId(),
                            root -> root.join(ExcelReportExport_.reportCreator, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(ExcelReportExport_.organization, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(ExcelReportExport_.department, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSystemModuleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemModuleId(),
                            root -> root.join(ExcelReportExport_.systemModule, JoinType.LEFT).get(SystemModule_.id)
                        )
                    );
            }
            if (criteria.getReportDesignId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportDesignId(),
                            root -> root.join(ExcelReportExport_.reportDesign, JoinType.LEFT).get(ReportDesign_.id)
                        )
                    );
            }
            if (criteria.getFileCheckSumAlgorithmId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFileCheckSumAlgorithmId(),
                            root -> root.join(ExcelReportExport_.fileCheckSumAlgorithm, JoinType.LEFT).get(Algorithm_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
