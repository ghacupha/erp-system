package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.CardTypes;
import io.github.erp.repository.CardTypesRepository;
import io.github.erp.repository.search.CardTypesSearchRepository;
import io.github.erp.service.criteria.CardTypesCriteria;
import io.github.erp.service.dto.CardTypesDTO;
import io.github.erp.service.mapper.CardTypesMapper;
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
 * Service for executing complex queries for {@link CardTypes} entities in the database.
 * The main input is a {@link CardTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardTypesDTO} or a {@link Page} of {@link CardTypesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardTypesQueryService extends QueryService<CardTypes> {

    private final Logger log = LoggerFactory.getLogger(CardTypesQueryService.class);

    private final CardTypesRepository cardTypesRepository;

    private final CardTypesMapper cardTypesMapper;

    private final CardTypesSearchRepository cardTypesSearchRepository;

    public CardTypesQueryService(
        CardTypesRepository cardTypesRepository,
        CardTypesMapper cardTypesMapper,
        CardTypesSearchRepository cardTypesSearchRepository
    ) {
        this.cardTypesRepository = cardTypesRepository;
        this.cardTypesMapper = cardTypesMapper;
        this.cardTypesSearchRepository = cardTypesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardTypesDTO> findByCriteria(CardTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardTypes> specification = createSpecification(criteria);
        return cardTypesMapper.toDto(cardTypesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardTypesDTO> findByCriteria(CardTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardTypes> specification = createSpecification(criteria);
        return cardTypesRepository.findAll(specification, page).map(cardTypesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardTypes> specification = createSpecification(criteria);
        return cardTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link CardTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardTypes> createSpecification(CardTypesCriteria criteria) {
        Specification<CardTypes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardTypes_.id));
            }
            if (criteria.getCardTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardTypeCode(), CardTypes_.cardTypeCode));
            }
            if (criteria.getCardType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardType(), CardTypes_.cardType));
            }
        }
        return specification;
    }
}
