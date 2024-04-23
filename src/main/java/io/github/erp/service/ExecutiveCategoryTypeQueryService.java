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
import io.github.erp.domain.ExecutiveCategoryType;
import io.github.erp.repository.ExecutiveCategoryTypeRepository;
import io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepository;
import io.github.erp.service.criteria.ExecutiveCategoryTypeCriteria;
import io.github.erp.service.dto.ExecutiveCategoryTypeDTO;
import io.github.erp.service.mapper.ExecutiveCategoryTypeMapper;
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
 * Service for executing complex queries for {@link ExecutiveCategoryType} entities in the database.
 * The main input is a {@link ExecutiveCategoryTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExecutiveCategoryTypeDTO} or a {@link Page} of {@link ExecutiveCategoryTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExecutiveCategoryTypeQueryService extends QueryService<ExecutiveCategoryType> {

    private final Logger log = LoggerFactory.getLogger(ExecutiveCategoryTypeQueryService.class);

    private final ExecutiveCategoryTypeRepository executiveCategoryTypeRepository;

    private final ExecutiveCategoryTypeMapper executiveCategoryTypeMapper;

    private final ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository;

    public ExecutiveCategoryTypeQueryService(
        ExecutiveCategoryTypeRepository executiveCategoryTypeRepository,
        ExecutiveCategoryTypeMapper executiveCategoryTypeMapper,
        ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository
    ) {
        this.executiveCategoryTypeRepository = executiveCategoryTypeRepository;
        this.executiveCategoryTypeMapper = executiveCategoryTypeMapper;
        this.executiveCategoryTypeSearchRepository = executiveCategoryTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ExecutiveCategoryTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExecutiveCategoryTypeDTO> findByCriteria(ExecutiveCategoryTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExecutiveCategoryType> specification = createSpecification(criteria);
        return executiveCategoryTypeMapper.toDto(executiveCategoryTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExecutiveCategoryTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExecutiveCategoryTypeDTO> findByCriteria(ExecutiveCategoryTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExecutiveCategoryType> specification = createSpecification(criteria);
        return executiveCategoryTypeRepository.findAll(specification, page).map(executiveCategoryTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExecutiveCategoryTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExecutiveCategoryType> specification = createSpecification(criteria);
        return executiveCategoryTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ExecutiveCategoryTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExecutiveCategoryType> createSpecification(ExecutiveCategoryTypeCriteria criteria) {
        Specification<ExecutiveCategoryType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExecutiveCategoryType_.id));
            }
            if (criteria.getDirectorCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDirectorCategoryTypeCode(), ExecutiveCategoryType_.directorCategoryTypeCode)
                    );
            }
            if (criteria.getDirectorCategoryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDirectorCategoryType(), ExecutiveCategoryType_.directorCategoryType)
                    );
            }
        }
        return specification;
    }
}
