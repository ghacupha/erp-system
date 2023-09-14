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
import io.github.erp.domain.AnticipatedMaturityPeriood;
import io.github.erp.repository.AnticipatedMaturityPerioodRepository;
import io.github.erp.repository.search.AnticipatedMaturityPerioodSearchRepository;
import io.github.erp.service.criteria.AnticipatedMaturityPerioodCriteria;
import io.github.erp.service.dto.AnticipatedMaturityPerioodDTO;
import io.github.erp.service.mapper.AnticipatedMaturityPerioodMapper;
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
 * Service for executing complex queries for {@link AnticipatedMaturityPeriood} entities in the database.
 * The main input is a {@link AnticipatedMaturityPerioodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnticipatedMaturityPerioodDTO} or a {@link Page} of {@link AnticipatedMaturityPerioodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnticipatedMaturityPerioodQueryService extends QueryService<AnticipatedMaturityPeriood> {

    private final Logger log = LoggerFactory.getLogger(AnticipatedMaturityPerioodQueryService.class);

    private final AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository;

    private final AnticipatedMaturityPerioodMapper anticipatedMaturityPerioodMapper;

    private final AnticipatedMaturityPerioodSearchRepository anticipatedMaturityPerioodSearchRepository;

    public AnticipatedMaturityPerioodQueryService(
        AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository,
        AnticipatedMaturityPerioodMapper anticipatedMaturityPerioodMapper,
        AnticipatedMaturityPerioodSearchRepository anticipatedMaturityPerioodSearchRepository
    ) {
        this.anticipatedMaturityPerioodRepository = anticipatedMaturityPerioodRepository;
        this.anticipatedMaturityPerioodMapper = anticipatedMaturityPerioodMapper;
        this.anticipatedMaturityPerioodSearchRepository = anticipatedMaturityPerioodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnticipatedMaturityPerioodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnticipatedMaturityPerioodDTO> findByCriteria(AnticipatedMaturityPerioodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnticipatedMaturityPeriood> specification = createSpecification(criteria);
        return anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPerioodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AnticipatedMaturityPerioodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnticipatedMaturityPerioodDTO> findByCriteria(AnticipatedMaturityPerioodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnticipatedMaturityPeriood> specification = createSpecification(criteria);
        return anticipatedMaturityPerioodRepository.findAll(specification, page).map(anticipatedMaturityPerioodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnticipatedMaturityPerioodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnticipatedMaturityPeriood> specification = createSpecification(criteria);
        return anticipatedMaturityPerioodRepository.count(specification);
    }

    /**
     * Function to convert {@link AnticipatedMaturityPerioodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnticipatedMaturityPeriood> createSpecification(AnticipatedMaturityPerioodCriteria criteria) {
        Specification<AnticipatedMaturityPeriood> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnticipatedMaturityPeriood_.id));
            }
            if (criteria.getAnticipatedMaturityTenorCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAnticipatedMaturityTenorCode(),
                            AnticipatedMaturityPeriood_.anticipatedMaturityTenorCode
                        )
                    );
            }
            if (criteria.getAniticipatedMaturityTenorType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAniticipatedMaturityTenorType(),
                            AnticipatedMaturityPeriood_.aniticipatedMaturityTenorType
                        )
                    );
            }
        }
        return specification;
    }
}
