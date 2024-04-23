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
import io.github.erp.domain.CounterPartyDealType;
import io.github.erp.repository.CounterPartyDealTypeRepository;
import io.github.erp.repository.search.CounterPartyDealTypeSearchRepository;
import io.github.erp.service.criteria.CounterPartyDealTypeCriteria;
import io.github.erp.service.dto.CounterPartyDealTypeDTO;
import io.github.erp.service.mapper.CounterPartyDealTypeMapper;
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
 * Service for executing complex queries for {@link CounterPartyDealType} entities in the database.
 * The main input is a {@link CounterPartyDealTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterPartyDealTypeDTO} or a {@link Page} of {@link CounterPartyDealTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterPartyDealTypeQueryService extends QueryService<CounterPartyDealType> {

    private final Logger log = LoggerFactory.getLogger(CounterPartyDealTypeQueryService.class);

    private final CounterPartyDealTypeRepository counterPartyDealTypeRepository;

    private final CounterPartyDealTypeMapper counterPartyDealTypeMapper;

    private final CounterPartyDealTypeSearchRepository counterPartyDealTypeSearchRepository;

    public CounterPartyDealTypeQueryService(
        CounterPartyDealTypeRepository counterPartyDealTypeRepository,
        CounterPartyDealTypeMapper counterPartyDealTypeMapper,
        CounterPartyDealTypeSearchRepository counterPartyDealTypeSearchRepository
    ) {
        this.counterPartyDealTypeRepository = counterPartyDealTypeRepository;
        this.counterPartyDealTypeMapper = counterPartyDealTypeMapper;
        this.counterPartyDealTypeSearchRepository = counterPartyDealTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CounterPartyDealTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterPartyDealTypeDTO> findByCriteria(CounterPartyDealTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterPartyDealType> specification = createSpecification(criteria);
        return counterPartyDealTypeMapper.toDto(counterPartyDealTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterPartyDealTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterPartyDealTypeDTO> findByCriteria(CounterPartyDealTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterPartyDealType> specification = createSpecification(criteria);
        return counterPartyDealTypeRepository.findAll(specification, page).map(counterPartyDealTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterPartyDealTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterPartyDealType> specification = createSpecification(criteria);
        return counterPartyDealTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterPartyDealTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterPartyDealType> createSpecification(CounterPartyDealTypeCriteria criteria) {
        Specification<CounterPartyDealType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CounterPartyDealType_.id));
            }
            if (criteria.getCounterpartyDealCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCounterpartyDealCode(), CounterPartyDealType_.counterpartyDealCode)
                    );
            }
            if (criteria.getCounterpartyDealTypeDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCounterpartyDealTypeDetails(),
                            CounterPartyDealType_.counterpartyDealTypeDetails
                        )
                    );
            }
        }
        return specification;
    }
}
