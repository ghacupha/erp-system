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
import io.github.erp.domain.CardCategoryType;
import io.github.erp.repository.CardCategoryTypeRepository;
import io.github.erp.repository.search.CardCategoryTypeSearchRepository;
import io.github.erp.service.criteria.CardCategoryTypeCriteria;
import io.github.erp.service.dto.CardCategoryTypeDTO;
import io.github.erp.service.mapper.CardCategoryTypeMapper;
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
 * Service for executing complex queries for {@link CardCategoryType} entities in the database.
 * The main input is a {@link CardCategoryTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardCategoryTypeDTO} or a {@link Page} of {@link CardCategoryTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardCategoryTypeQueryService extends QueryService<CardCategoryType> {

    private final Logger log = LoggerFactory.getLogger(CardCategoryTypeQueryService.class);

    private final CardCategoryTypeRepository cardCategoryTypeRepository;

    private final CardCategoryTypeMapper cardCategoryTypeMapper;

    private final CardCategoryTypeSearchRepository cardCategoryTypeSearchRepository;

    public CardCategoryTypeQueryService(
        CardCategoryTypeRepository cardCategoryTypeRepository,
        CardCategoryTypeMapper cardCategoryTypeMapper,
        CardCategoryTypeSearchRepository cardCategoryTypeSearchRepository
    ) {
        this.cardCategoryTypeRepository = cardCategoryTypeRepository;
        this.cardCategoryTypeMapper = cardCategoryTypeMapper;
        this.cardCategoryTypeSearchRepository = cardCategoryTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardCategoryTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardCategoryTypeDTO> findByCriteria(CardCategoryTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardCategoryType> specification = createSpecification(criteria);
        return cardCategoryTypeMapper.toDto(cardCategoryTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardCategoryTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardCategoryTypeDTO> findByCriteria(CardCategoryTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardCategoryType> specification = createSpecification(criteria);
        return cardCategoryTypeRepository.findAll(specification, page).map(cardCategoryTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardCategoryTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardCategoryType> specification = createSpecification(criteria);
        return cardCategoryTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CardCategoryTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardCategoryType> createSpecification(CardCategoryTypeCriteria criteria) {
        Specification<CardCategoryType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardCategoryType_.id));
            }
            if (criteria.getCardCategoryFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getCardCategoryFlag(), CardCategoryType_.cardCategoryFlag));
            }
            if (criteria.getCardCategoryDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCardCategoryDescription(), CardCategoryType_.cardCategoryDescription)
                    );
            }
        }
        return specification;
    }
}
