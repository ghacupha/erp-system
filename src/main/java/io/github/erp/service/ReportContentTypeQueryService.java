package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.ReportContentType;
import io.github.erp.repository.ReportContentTypeRepository;
import io.github.erp.repository.search.ReportContentTypeSearchRepository;
import io.github.erp.service.criteria.ReportContentTypeCriteria;
import io.github.erp.service.dto.ReportContentTypeDTO;
import io.github.erp.service.mapper.ReportContentTypeMapper;
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
 * Service for executing complex queries for {@link ReportContentType} entities in the database.
 * The main input is a {@link ReportContentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportContentTypeDTO} or a {@link Page} of {@link ReportContentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportContentTypeQueryService extends QueryService<ReportContentType> {

    private final Logger log = LoggerFactory.getLogger(ReportContentTypeQueryService.class);

    private final ReportContentTypeRepository reportContentTypeRepository;

    private final ReportContentTypeMapper reportContentTypeMapper;

    private final ReportContentTypeSearchRepository reportContentTypeSearchRepository;

    public ReportContentTypeQueryService(
        ReportContentTypeRepository reportContentTypeRepository,
        ReportContentTypeMapper reportContentTypeMapper,
        ReportContentTypeSearchRepository reportContentTypeSearchRepository
    ) {
        this.reportContentTypeRepository = reportContentTypeRepository;
        this.reportContentTypeMapper = reportContentTypeMapper;
        this.reportContentTypeSearchRepository = reportContentTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportContentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportContentTypeDTO> findByCriteria(ReportContentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportContentType> specification = createSpecification(criteria);
        return reportContentTypeMapper.toDto(reportContentTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportContentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportContentTypeDTO> findByCriteria(ReportContentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportContentType> specification = createSpecification(criteria);
        return reportContentTypeRepository.findAll(specification, page).map(reportContentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportContentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportContentType> specification = createSpecification(criteria);
        return reportContentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportContentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportContentType> createSpecification(ReportContentTypeCriteria criteria) {
        Specification<ReportContentType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportContentType_.id));
            }
            if (criteria.getReportTypeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportTypeName(), ReportContentType_.reportTypeName));
            }
            if (criteria.getReportFileExtension() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportFileExtension(), ReportContentType_.reportFileExtension));
            }
            if (criteria.getSystemContentTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemContentTypeId(),
                            root -> root.join(ReportContentType_.systemContentType, JoinType.LEFT).get(SystemContentType_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ReportContentType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
