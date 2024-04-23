package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.ReportTemplate;
import io.github.erp.repository.ReportTemplateRepository;
import io.github.erp.repository.search.ReportTemplateSearchRepository;
import io.github.erp.service.criteria.ReportTemplateCriteria;
import io.github.erp.service.dto.ReportTemplateDTO;
import io.github.erp.service.mapper.ReportTemplateMapper;
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
 * Service for executing complex queries for {@link ReportTemplate} entities in the database.
 * The main input is a {@link ReportTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportTemplateDTO} or a {@link Page} of {@link ReportTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportTemplateQueryService extends QueryService<ReportTemplate> {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateQueryService.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    private final ReportTemplateSearchRepository reportTemplateSearchRepository;

    public ReportTemplateQueryService(
        ReportTemplateRepository reportTemplateRepository,
        ReportTemplateMapper reportTemplateMapper,
        ReportTemplateSearchRepository reportTemplateSearchRepository
    ) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
        this.reportTemplateSearchRepository = reportTemplateSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportTemplateDTO> findByCriteria(ReportTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateMapper.toDto(reportTemplateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportTemplateDTO> findByCriteria(ReportTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.findAll(specification, page).map(reportTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportTemplate> createSpecification(ReportTemplateCriteria criteria) {
        Specification<ReportTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportTemplate_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCatalogueNumber(), ReportTemplate_.catalogueNumber));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ReportTemplate_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
