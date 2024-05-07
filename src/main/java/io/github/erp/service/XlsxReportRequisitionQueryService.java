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
import io.github.erp.domain.XlsxReportRequisition;
import io.github.erp.repository.XlsxReportRequisitionRepository;
import io.github.erp.repository.search.XlsxReportRequisitionSearchRepository;
import io.github.erp.service.criteria.XlsxReportRequisitionCriteria;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import io.github.erp.service.mapper.XlsxReportRequisitionMapper;
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
 * Service for executing complex queries for {@link XlsxReportRequisition} entities in the database.
 * The main input is a {@link XlsxReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link XlsxReportRequisitionDTO} or a {@link Page} of {@link XlsxReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class XlsxReportRequisitionQueryService extends QueryService<XlsxReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(XlsxReportRequisitionQueryService.class);

    private final XlsxReportRequisitionRepository xlsxReportRequisitionRepository;

    private final XlsxReportRequisitionMapper xlsxReportRequisitionMapper;

    private final XlsxReportRequisitionSearchRepository xlsxReportRequisitionSearchRepository;

    public XlsxReportRequisitionQueryService(
        XlsxReportRequisitionRepository xlsxReportRequisitionRepository,
        XlsxReportRequisitionMapper xlsxReportRequisitionMapper,
        XlsxReportRequisitionSearchRepository xlsxReportRequisitionSearchRepository
    ) {
        this.xlsxReportRequisitionRepository = xlsxReportRequisitionRepository;
        this.xlsxReportRequisitionMapper = xlsxReportRequisitionMapper;
        this.xlsxReportRequisitionSearchRepository = xlsxReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link XlsxReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<XlsxReportRequisitionDTO> findByCriteria(XlsxReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<XlsxReportRequisition> specification = createSpecification(criteria);
        return xlsxReportRequisitionMapper.toDto(xlsxReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link XlsxReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<XlsxReportRequisitionDTO> findByCriteria(XlsxReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<XlsxReportRequisition> specification = createSpecification(criteria);
        return xlsxReportRequisitionRepository.findAll(specification, page).map(xlsxReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(XlsxReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<XlsxReportRequisition> specification = createSpecification(criteria);
        return xlsxReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link XlsxReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<XlsxReportRequisition> createSpecification(XlsxReportRequisitionCriteria criteria) {
        Specification<XlsxReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), XlsxReportRequisition_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), XlsxReportRequisition_.reportName));
            }
            if (criteria.getReportDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportDate(), XlsxReportRequisition_.reportDate));
            }
            if (criteria.getUserPassword() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getUserPassword(), XlsxReportRequisition_.userPassword));
            }
            if (criteria.getReportFileChecksum() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportFileChecksum(), XlsxReportRequisition_.reportFileChecksum)
                    );
            }
            if (criteria.getReportStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReportStatus(), XlsxReportRequisition_.reportStatus));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), XlsxReportRequisition_.reportId));
            }
            if (criteria.getReportTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportTemplateId(),
                            root -> root.join(XlsxReportRequisition_.reportTemplate, JoinType.LEFT).get(ReportTemplate_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(XlsxReportRequisition_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(XlsxReportRequisition_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
