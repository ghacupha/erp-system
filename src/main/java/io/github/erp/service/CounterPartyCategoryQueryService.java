package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CounterPartyCategory;
import io.github.erp.repository.CounterPartyCategoryRepository;
import io.github.erp.repository.search.CounterPartyCategorySearchRepository;
import io.github.erp.service.criteria.CounterPartyCategoryCriteria;
import io.github.erp.service.dto.CounterPartyCategoryDTO;
import io.github.erp.service.mapper.CounterPartyCategoryMapper;
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
 * Service for executing complex queries for {@link CounterPartyCategory} entities in the database.
 * The main input is a {@link CounterPartyCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterPartyCategoryDTO} or a {@link Page} of {@link CounterPartyCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterPartyCategoryQueryService extends QueryService<CounterPartyCategory> {

    private final Logger log = LoggerFactory.getLogger(CounterPartyCategoryQueryService.class);

    private final CounterPartyCategoryRepository counterPartyCategoryRepository;

    private final CounterPartyCategoryMapper counterPartyCategoryMapper;

    private final CounterPartyCategorySearchRepository counterPartyCategorySearchRepository;

    public CounterPartyCategoryQueryService(
        CounterPartyCategoryRepository counterPartyCategoryRepository,
        CounterPartyCategoryMapper counterPartyCategoryMapper,
        CounterPartyCategorySearchRepository counterPartyCategorySearchRepository
    ) {
        this.counterPartyCategoryRepository = counterPartyCategoryRepository;
        this.counterPartyCategoryMapper = counterPartyCategoryMapper;
        this.counterPartyCategorySearchRepository = counterPartyCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link CounterPartyCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterPartyCategoryDTO> findByCriteria(CounterPartyCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterPartyCategory> specification = createSpecification(criteria);
        return counterPartyCategoryMapper.toDto(counterPartyCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterPartyCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterPartyCategoryDTO> findByCriteria(CounterPartyCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterPartyCategory> specification = createSpecification(criteria);
        return counterPartyCategoryRepository.findAll(specification, page).map(counterPartyCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterPartyCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterPartyCategory> specification = createSpecification(criteria);
        return counterPartyCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterPartyCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterPartyCategory> createSpecification(CounterPartyCategoryCriteria criteria) {
        Specification<CounterPartyCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CounterPartyCategory_.id));
            }
            if (criteria.getCounterpartyCategoryCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCounterpartyCategoryCode(), CounterPartyCategory_.counterpartyCategoryCode)
                    );
            }
            if (criteria.getCounterpartyCategoryCodeDetails() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCounterpartyCategoryCodeDetails(),
                            CounterPartyCategory_.counterpartyCategoryCodeDetails
                        )
                    );
            }
        }
        return specification;
    }
}
