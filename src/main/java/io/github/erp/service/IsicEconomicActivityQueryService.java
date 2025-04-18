package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.IsicEconomicActivity;
import io.github.erp.repository.IsicEconomicActivityRepository;
import io.github.erp.repository.search.IsicEconomicActivitySearchRepository;
import io.github.erp.service.criteria.IsicEconomicActivityCriteria;
import io.github.erp.service.dto.IsicEconomicActivityDTO;
import io.github.erp.service.mapper.IsicEconomicActivityMapper;
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
 * Service for executing complex queries for {@link IsicEconomicActivity} entities in the database.
 * The main input is a {@link IsicEconomicActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IsicEconomicActivityDTO} or a {@link Page} of {@link IsicEconomicActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IsicEconomicActivityQueryService extends QueryService<IsicEconomicActivity> {

    private final Logger log = LoggerFactory.getLogger(IsicEconomicActivityQueryService.class);

    private final IsicEconomicActivityRepository isicEconomicActivityRepository;

    private final IsicEconomicActivityMapper isicEconomicActivityMapper;

    private final IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository;

    public IsicEconomicActivityQueryService(
        IsicEconomicActivityRepository isicEconomicActivityRepository,
        IsicEconomicActivityMapper isicEconomicActivityMapper,
        IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository
    ) {
        this.isicEconomicActivityRepository = isicEconomicActivityRepository;
        this.isicEconomicActivityMapper = isicEconomicActivityMapper;
        this.isicEconomicActivitySearchRepository = isicEconomicActivitySearchRepository;
    }

    /**
     * Return a {@link List} of {@link IsicEconomicActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IsicEconomicActivityDTO> findByCriteria(IsicEconomicActivityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IsicEconomicActivity> specification = createSpecification(criteria);
        return isicEconomicActivityMapper.toDto(isicEconomicActivityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IsicEconomicActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IsicEconomicActivityDTO> findByCriteria(IsicEconomicActivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IsicEconomicActivity> specification = createSpecification(criteria);
        return isicEconomicActivityRepository.findAll(specification, page).map(isicEconomicActivityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IsicEconomicActivityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IsicEconomicActivity> specification = createSpecification(criteria);
        return isicEconomicActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link IsicEconomicActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IsicEconomicActivity> createSpecification(IsicEconomicActivityCriteria criteria) {
        Specification<IsicEconomicActivity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IsicEconomicActivity_.id));
            }
            if (criteria.getBusinessEconomicActivityCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBusinessEconomicActivityCode(),
                            IsicEconomicActivity_.businessEconomicActivityCode
                        )
                    );
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), IsicEconomicActivity_.section));
            }
            if (criteria.getSectionLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSectionLabel(), IsicEconomicActivity_.sectionLabel));
            }
            if (criteria.getDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivision(), IsicEconomicActivity_.division));
            }
            if (criteria.getDivisionLabel() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDivisionLabel(), IsicEconomicActivity_.divisionLabel));
            }
            if (criteria.getGroupCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupCode(), IsicEconomicActivity_.groupCode));
            }
            if (criteria.getGroupLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupLabel(), IsicEconomicActivity_.groupLabel));
            }
            if (criteria.getClassCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassCode(), IsicEconomicActivity_.classCode));
            }
            if (criteria.getBusinessEconomicActivityType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBusinessEconomicActivityType(),
                            IsicEconomicActivity_.businessEconomicActivityType
                        )
                    );
            }
        }
        return specification;
    }
}
