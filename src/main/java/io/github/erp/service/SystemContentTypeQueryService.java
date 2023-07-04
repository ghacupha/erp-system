package io.github.erp.service;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
import io.github.erp.domain.SystemContentType;
import io.github.erp.repository.SystemContentTypeRepository;
import io.github.erp.repository.search.SystemContentTypeSearchRepository;
import io.github.erp.service.criteria.SystemContentTypeCriteria;
import io.github.erp.service.dto.SystemContentTypeDTO;
import io.github.erp.service.mapper.SystemContentTypeMapper;
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
 * Service for executing complex queries for {@link SystemContentType} entities in the database.
 * The main input is a {@link SystemContentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemContentTypeDTO} or a {@link Page} of {@link SystemContentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemContentTypeQueryService extends QueryService<SystemContentType> {

    private final Logger log = LoggerFactory.getLogger(SystemContentTypeQueryService.class);

    private final SystemContentTypeRepository systemContentTypeRepository;

    private final SystemContentTypeMapper systemContentTypeMapper;

    private final SystemContentTypeSearchRepository systemContentTypeSearchRepository;

    public SystemContentTypeQueryService(
        SystemContentTypeRepository systemContentTypeRepository,
        SystemContentTypeMapper systemContentTypeMapper,
        SystemContentTypeSearchRepository systemContentTypeSearchRepository
    ) {
        this.systemContentTypeRepository = systemContentTypeRepository;
        this.systemContentTypeMapper = systemContentTypeMapper;
        this.systemContentTypeSearchRepository = systemContentTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemContentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemContentTypeDTO> findByCriteria(SystemContentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemContentType> specification = createSpecification(criteria);
        return systemContentTypeMapper.toDto(systemContentTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemContentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemContentTypeDTO> findByCriteria(SystemContentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemContentType> specification = createSpecification(criteria);
        return systemContentTypeRepository.findAll(specification, page).map(systemContentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemContentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemContentType> specification = createSpecification(criteria);
        return systemContentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemContentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemContentType> createSpecification(SystemContentTypeCriteria criteria) {
        Specification<SystemContentType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SystemContentType_.id));
            }
            if (criteria.getContentTypeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContentTypeName(), SystemContentType_.contentTypeName));
            }
            if (criteria.getContentTypeHeader() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContentTypeHeader(), SystemContentType_.contentTypeHeader));
            }
            if (criteria.getAvailability() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailability(), SystemContentType_.availability));
            }
            if (criteria.getPlaceholdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholdersId(),
                            root -> root.join(SystemContentType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSysMapsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSysMapsId(),
                            root -> root.join(SystemContentType_.sysMaps, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
