package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.CardState;
import io.github.erp.repository.CardStateRepository;
import io.github.erp.repository.search.CardStateSearchRepository;
import io.github.erp.service.criteria.CardStateCriteria;
import io.github.erp.service.dto.CardStateDTO;
import io.github.erp.service.mapper.CardStateMapper;
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
 * Service for executing complex queries for {@link CardState} entities in the database.
 * The main input is a {@link CardStateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardStateDTO} or a {@link Page} of {@link CardStateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardStateQueryService extends QueryService<CardState> {

    private final Logger log = LoggerFactory.getLogger(CardStateQueryService.class);

    private final CardStateRepository cardStateRepository;

    private final CardStateMapper cardStateMapper;

    private final CardStateSearchRepository cardStateSearchRepository;

    public CardStateQueryService(
        CardStateRepository cardStateRepository,
        CardStateMapper cardStateMapper,
        CardStateSearchRepository cardStateSearchRepository
    ) {
        this.cardStateRepository = cardStateRepository;
        this.cardStateMapper = cardStateMapper;
        this.cardStateSearchRepository = cardStateSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardStateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardStateDTO> findByCriteria(CardStateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardState> specification = createSpecification(criteria);
        return cardStateMapper.toDto(cardStateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardStateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardStateDTO> findByCriteria(CardStateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardState> specification = createSpecification(criteria);
        return cardStateRepository.findAll(specification, page).map(cardStateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardStateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardState> specification = createSpecification(criteria);
        return cardStateRepository.count(specification);
    }

    /**
     * Function to convert {@link CardStateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardState> createSpecification(CardStateCriteria criteria) {
        Specification<CardState> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardState_.id));
            }
            if (criteria.getCardStateFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getCardStateFlag(), CardState_.cardStateFlag));
            }
            if (criteria.getCardStateFlagDetails() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCardStateFlagDetails(), CardState_.cardStateFlagDetails));
            }
            if (criteria.getCardStateFlagDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCardStateFlagDescription(), CardState_.cardStateFlagDescription)
                    );
            }
        }
        return specification;
    }
}
