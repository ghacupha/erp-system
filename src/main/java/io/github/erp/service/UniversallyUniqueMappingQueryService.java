package io.github.erp.service;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.service.criteria.UniversallyUniqueMappingCriteria;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
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
 * Service for executing complex queries for {@link UniversallyUniqueMapping} entities in the database.
 * The main input is a {@link UniversallyUniqueMappingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UniversallyUniqueMappingDTO} or a {@link Page} of {@link UniversallyUniqueMappingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UniversallyUniqueMappingQueryService extends QueryService<UniversallyUniqueMapping> {

    private final Logger log = LoggerFactory.getLogger(UniversallyUniqueMappingQueryService.class);

    private final UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    private final UniversallyUniqueMappingMapper universallyUniqueMappingMapper;

    private final UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository;

    public UniversallyUniqueMappingQueryService(
        UniversallyUniqueMappingRepository universallyUniqueMappingRepository,
        UniversallyUniqueMappingMapper universallyUniqueMappingMapper,
        UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository
    ) {
        this.universallyUniqueMappingRepository = universallyUniqueMappingRepository;
        this.universallyUniqueMappingMapper = universallyUniqueMappingMapper;
        this.universallyUniqueMappingSearchRepository = universallyUniqueMappingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UniversallyUniqueMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UniversallyUniqueMappingDTO> findByCriteria(UniversallyUniqueMappingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UniversallyUniqueMapping> specification = createSpecification(criteria);
        return universallyUniqueMappingMapper.toDto(universallyUniqueMappingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UniversallyUniqueMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UniversallyUniqueMappingDTO> findByCriteria(UniversallyUniqueMappingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UniversallyUniqueMapping> specification = createSpecification(criteria);
        return universallyUniqueMappingRepository.findAll(specification, page).map(universallyUniqueMappingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UniversallyUniqueMappingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UniversallyUniqueMapping> specification = createSpecification(criteria);
        return universallyUniqueMappingRepository.count(specification);
    }

    /**
     * Function to convert {@link UniversallyUniqueMappingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UniversallyUniqueMapping> createSpecification(UniversallyUniqueMappingCriteria criteria) {
        Specification<UniversallyUniqueMapping> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UniversallyUniqueMapping_.id));
            }
            if (criteria.getUniversalKey() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getUniversalKey(), UniversallyUniqueMapping_.universalKey));
            }
            if (criteria.getMappedValue() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMappedValue(), UniversallyUniqueMapping_.mappedValue));
            }
        }
        return specification;
    }
}
