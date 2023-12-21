package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.CardClassType;
import io.github.erp.repository.CardClassTypeRepository;
import io.github.erp.repository.search.CardClassTypeSearchRepository;
import io.github.erp.service.criteria.CardClassTypeCriteria;
import io.github.erp.service.dto.CardClassTypeDTO;
import io.github.erp.service.mapper.CardClassTypeMapper;
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
 * Service for executing complex queries for {@link CardClassType} entities in the database.
 * The main input is a {@link CardClassTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardClassTypeDTO} or a {@link Page} of {@link CardClassTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardClassTypeQueryService extends QueryService<CardClassType> {

    private final Logger log = LoggerFactory.getLogger(CardClassTypeQueryService.class);

    private final CardClassTypeRepository cardClassTypeRepository;

    private final CardClassTypeMapper cardClassTypeMapper;

    private final CardClassTypeSearchRepository cardClassTypeSearchRepository;

    public CardClassTypeQueryService(
        CardClassTypeRepository cardClassTypeRepository,
        CardClassTypeMapper cardClassTypeMapper,
        CardClassTypeSearchRepository cardClassTypeSearchRepository
    ) {
        this.cardClassTypeRepository = cardClassTypeRepository;
        this.cardClassTypeMapper = cardClassTypeMapper;
        this.cardClassTypeSearchRepository = cardClassTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardClassTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardClassTypeDTO> findByCriteria(CardClassTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardClassType> specification = createSpecification(criteria);
        return cardClassTypeMapper.toDto(cardClassTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardClassTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardClassTypeDTO> findByCriteria(CardClassTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardClassType> specification = createSpecification(criteria);
        return cardClassTypeRepository.findAll(specification, page).map(cardClassTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardClassTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardClassType> specification = createSpecification(criteria);
        return cardClassTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CardClassTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardClassType> createSpecification(CardClassTypeCriteria criteria) {
        Specification<CardClassType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardClassType_.id));
            }
            if (criteria.getCardClassTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCardClassTypeCode(), CardClassType_.cardClassTypeCode));
            }
            if (criteria.getCardClassType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardClassType(), CardClassType_.cardClassType));
            }
        }
        return specification;
    }
}
