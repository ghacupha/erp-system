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
import io.github.erp.domain.GlMapping;
import io.github.erp.repository.GlMappingRepository;
import io.github.erp.repository.search.GlMappingSearchRepository;
import io.github.erp.service.criteria.GlMappingCriteria;
import io.github.erp.service.dto.GlMappingDTO;
import io.github.erp.service.mapper.GlMappingMapper;
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
 * Service for executing complex queries for {@link GlMapping} entities in the database.
 * The main input is a {@link GlMappingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GlMappingDTO} or a {@link Page} of {@link GlMappingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GlMappingQueryService extends QueryService<GlMapping> {

    private final Logger log = LoggerFactory.getLogger(GlMappingQueryService.class);

    private final GlMappingRepository glMappingRepository;

    private final GlMappingMapper glMappingMapper;

    private final GlMappingSearchRepository glMappingSearchRepository;

    public GlMappingQueryService(
        GlMappingRepository glMappingRepository,
        GlMappingMapper glMappingMapper,
        GlMappingSearchRepository glMappingSearchRepository
    ) {
        this.glMappingRepository = glMappingRepository;
        this.glMappingMapper = glMappingMapper;
        this.glMappingSearchRepository = glMappingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GlMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GlMappingDTO> findByCriteria(GlMappingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GlMapping> specification = createSpecification(criteria);
        return glMappingMapper.toDto(glMappingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GlMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GlMappingDTO> findByCriteria(GlMappingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GlMapping> specification = createSpecification(criteria);
        return glMappingRepository.findAll(specification, page).map(glMappingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GlMappingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GlMapping> specification = createSpecification(criteria);
        return glMappingRepository.count(specification);
    }

    /**
     * Function to convert {@link GlMappingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GlMapping> createSpecification(GlMappingCriteria criteria) {
        Specification<GlMapping> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GlMapping_.id));
            }
            if (criteria.getSubGLCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubGLCode(), GlMapping_.subGLCode));
            }
            if (criteria.getSubGLDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubGLDescription(), GlMapping_.subGLDescription));
            }
            if (criteria.getMainGLCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMainGLCode(), GlMapping_.mainGLCode));
            }
            if (criteria.getMainGLDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMainGLDescription(), GlMapping_.mainGLDescription));
            }
            if (criteria.getGlType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGlType(), GlMapping_.glType));
            }
        }
        return specification;
    }
}
