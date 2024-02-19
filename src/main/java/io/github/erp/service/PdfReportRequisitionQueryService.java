package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.PdfReportRequisition;
import io.github.erp.repository.PdfReportRequisitionRepository;
import io.github.erp.repository.search.PdfReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PdfReportRequisitionCriteria;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.mapper.PdfReportRequisitionMapper;
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
 * Service for executing complex queries for {@link PdfReportRequisition} entities in the database.
 * The main input is a {@link PdfReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PdfReportRequisitionDTO} or a {@link Page} of {@link PdfReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PdfReportRequisitionQueryService extends QueryService<PdfReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(PdfReportRequisitionQueryService.class);

    private final PdfReportRequisitionRepository pdfReportRequisitionRepository;

    private final PdfReportRequisitionMapper pdfReportRequisitionMapper;

    private final PdfReportRequisitionSearchRepository pdfReportRequisitionSearchRepository;

    public PdfReportRequisitionQueryService(
        PdfReportRequisitionRepository pdfReportRequisitionRepository,
        PdfReportRequisitionMapper pdfReportRequisitionMapper,
        PdfReportRequisitionSearchRepository pdfReportRequisitionSearchRepository
    ) {
        this.pdfReportRequisitionRepository = pdfReportRequisitionRepository;
        this.pdfReportRequisitionMapper = pdfReportRequisitionMapper;
        this.pdfReportRequisitionSearchRepository = pdfReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PdfReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PdfReportRequisitionDTO> findByCriteria(PdfReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PdfReportRequisition> specification = createSpecification(criteria);
        return pdfReportRequisitionMapper.toDto(pdfReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PdfReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PdfReportRequisitionDTO> findByCriteria(PdfReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PdfReportRequisition> specification = createSpecification(criteria);
        return pdfReportRequisitionRepository.findAll(specification, page).map(pdfReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PdfReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PdfReportRequisition> specification = createSpecification(criteria);
        return pdfReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link PdfReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PdfReportRequisition> createSpecification(PdfReportRequisitionCriteria criteria) {
        Specification<PdfReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PdfReportRequisition_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), PdfReportRequisition_.reportName));
            }
            if (criteria.getReportDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportDate(), PdfReportRequisition_.reportDate));
            }
            if (criteria.getUserPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserPassword(), PdfReportRequisition_.userPassword));
            }
            if (criteria.getOwnerPassword() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getOwnerPassword(), PdfReportRequisition_.ownerPassword));
            }
            if (criteria.getReportFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportFileChecksum(), PdfReportRequisition_.reportFileChecksum));
            }
            if (criteria.getReportStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReportStatus(), PdfReportRequisition_.reportStatus));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), PdfReportRequisition_.reportId));
            }
            if (criteria.getReportTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportTemplateId(),
                            root -> root.join(PdfReportRequisition_.reportTemplate, JoinType.LEFT).get(ReportTemplate_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PdfReportRequisition_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(PdfReportRequisition_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
