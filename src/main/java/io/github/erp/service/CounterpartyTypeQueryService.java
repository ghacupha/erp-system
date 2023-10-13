package io.github.erp.service;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.CounterpartyType;
import io.github.erp.repository.CounterpartyTypeRepository;
import io.github.erp.repository.search.CounterpartyTypeSearchRepository;
import io.github.erp.service.criteria.CounterpartyTypeCriteria;
import io.github.erp.service.dto.CounterpartyTypeDTO;
import io.github.erp.service.mapper.CounterpartyTypeMapper;
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
 * Service for executing complex queries for {@link CounterpartyType} entities in the database.
 * The main input is a {@link CounterpartyTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterpartyTypeDTO} or a {@link Page} of {@link CounterpartyTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterpartyTypeQueryService extends QueryService<CounterpartyType> {

    private final Logger log = LoggerFactory.getLogger(CounterpartyTypeQueryService.class);

    private final CounterpartyTypeRepository counterpartyTypeRepository;

    private final CounterpartyTypeMapper counterpartyTypeMapper;

    private final CounterpartyTypeSearchRepository counterpartyTypeSearchRepository;

    public CounterpartyTypeQueryService(
        CounterpartyTypeRepository counterpartyTypeRepository,
        CounterpartyTypeMapper counterpartyTypeMapper,
        CounterpartyTypeSearchRepository counterpartyTypeSearchRepository
    ) {
        this.counterpartyTypeRepository = counterpartyTypeRepository;
        this.counterpartyTypeMapper = counterpartyTypeMapper;
        this.counterpartyTypeSearchRepository = counterpartyTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CounterpartyTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterpartyTypeDTO> findByCriteria(CounterpartyTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterpartyType> specification = createSpecification(criteria);
        return counterpartyTypeMapper.toDto(counterpartyTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterpartyTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterpartyTypeDTO> findByCriteria(CounterpartyTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterpartyType> specification = createSpecification(criteria);
        return counterpartyTypeRepository.findAll(specification, page).map(counterpartyTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterpartyTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterpartyType> specification = createSpecification(criteria);
        return counterpartyTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterpartyTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterpartyType> createSpecification(CounterpartyTypeCriteria criteria) {
        Specification<CounterpartyType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CounterpartyType_.id));
            }
            if (criteria.getCounterpartyTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCounterpartyTypeCode(), CounterpartyType_.counterpartyTypeCode));
            }
            if (criteria.getCounterPartyType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCounterPartyType(), CounterpartyType_.counterPartyType));
            }
        }
        return specification;
    }
}
