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
import io.github.erp.domain.CardStatusFlag;
import io.github.erp.repository.CardStatusFlagRepository;
import io.github.erp.repository.search.CardStatusFlagSearchRepository;
import io.github.erp.service.criteria.CardStatusFlagCriteria;
import io.github.erp.service.dto.CardStatusFlagDTO;
import io.github.erp.service.mapper.CardStatusFlagMapper;
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
 * Service for executing complex queries for {@link CardStatusFlag} entities in the database.
 * The main input is a {@link CardStatusFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardStatusFlagDTO} or a {@link Page} of {@link CardStatusFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardStatusFlagQueryService extends QueryService<CardStatusFlag> {

    private final Logger log = LoggerFactory.getLogger(CardStatusFlagQueryService.class);

    private final CardStatusFlagRepository cardStatusFlagRepository;

    private final CardStatusFlagMapper cardStatusFlagMapper;

    private final CardStatusFlagSearchRepository cardStatusFlagSearchRepository;

    public CardStatusFlagQueryService(
        CardStatusFlagRepository cardStatusFlagRepository,
        CardStatusFlagMapper cardStatusFlagMapper,
        CardStatusFlagSearchRepository cardStatusFlagSearchRepository
    ) {
        this.cardStatusFlagRepository = cardStatusFlagRepository;
        this.cardStatusFlagMapper = cardStatusFlagMapper;
        this.cardStatusFlagSearchRepository = cardStatusFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardStatusFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardStatusFlagDTO> findByCriteria(CardStatusFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardStatusFlag> specification = createSpecification(criteria);
        return cardStatusFlagMapper.toDto(cardStatusFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardStatusFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardStatusFlagDTO> findByCriteria(CardStatusFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardStatusFlag> specification = createSpecification(criteria);
        return cardStatusFlagRepository.findAll(specification, page).map(cardStatusFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardStatusFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardStatusFlag> specification = createSpecification(criteria);
        return cardStatusFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link CardStatusFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardStatusFlag> createSpecification(CardStatusFlagCriteria criteria) {
        Specification<CardStatusFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardStatusFlag_.id));
            }
            if (criteria.getCardStatusFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getCardStatusFlag(), CardStatusFlag_.cardStatusFlag));
            }
            if (criteria.getCardStatusFlagDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCardStatusFlagDescription(), CardStatusFlag_.cardStatusFlagDescription)
                    );
            }
            if (criteria.getCardStatusFlagDetails() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCardStatusFlagDetails(), CardStatusFlag_.cardStatusFlagDetails));
            }
        }
        return specification;
    }
}
