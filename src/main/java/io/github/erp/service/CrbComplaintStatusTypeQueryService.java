package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.CrbComplaintStatusType;
import io.github.erp.repository.CrbComplaintStatusTypeRepository;
import io.github.erp.repository.search.CrbComplaintStatusTypeSearchRepository;
import io.github.erp.service.criteria.CrbComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CrbComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CrbComplaintStatusTypeMapper;
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
 * Service for executing complex queries for {@link CrbComplaintStatusType} entities in the database.
 * The main input is a {@link CrbComplaintStatusTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbComplaintStatusTypeDTO} or a {@link Page} of {@link CrbComplaintStatusTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbComplaintStatusTypeQueryService extends QueryService<CrbComplaintStatusType> {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintStatusTypeQueryService.class);

    private final CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository;

    private final CrbComplaintStatusTypeMapper crbComplaintStatusTypeMapper;

    private final CrbComplaintStatusTypeSearchRepository crbComplaintStatusTypeSearchRepository;

    public CrbComplaintStatusTypeQueryService(
        CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository,
        CrbComplaintStatusTypeMapper crbComplaintStatusTypeMapper,
        CrbComplaintStatusTypeSearchRepository crbComplaintStatusTypeSearchRepository
    ) {
        this.crbComplaintStatusTypeRepository = crbComplaintStatusTypeRepository;
        this.crbComplaintStatusTypeMapper = crbComplaintStatusTypeMapper;
        this.crbComplaintStatusTypeSearchRepository = crbComplaintStatusTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbComplaintStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbComplaintStatusTypeDTO> findByCriteria(CrbComplaintStatusTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbComplaintStatusType> specification = createSpecification(criteria);
        return crbComplaintStatusTypeMapper.toDto(crbComplaintStatusTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbComplaintStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbComplaintStatusTypeDTO> findByCriteria(CrbComplaintStatusTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbComplaintStatusType> specification = createSpecification(criteria);
        return crbComplaintStatusTypeRepository.findAll(specification, page).map(crbComplaintStatusTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbComplaintStatusTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbComplaintStatusType> specification = createSpecification(criteria);
        return crbComplaintStatusTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbComplaintStatusTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbComplaintStatusType> createSpecification(CrbComplaintStatusTypeCriteria criteria) {
        Specification<CrbComplaintStatusType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbComplaintStatusType_.id));
            }
            if (criteria.getComplaintStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getComplaintStatusTypeCode(), CrbComplaintStatusType_.complaintStatusTypeCode)
                    );
            }
            if (criteria.getComplaintStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getComplaintStatusType(), CrbComplaintStatusType_.complaintStatusType)
                    );
            }
        }
        return specification;
    }
}
