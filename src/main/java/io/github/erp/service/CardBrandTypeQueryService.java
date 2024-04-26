package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.CardBrandType;
import io.github.erp.repository.CardBrandTypeRepository;
import io.github.erp.repository.search.CardBrandTypeSearchRepository;
import io.github.erp.service.criteria.CardBrandTypeCriteria;
import io.github.erp.service.dto.CardBrandTypeDTO;
import io.github.erp.service.mapper.CardBrandTypeMapper;
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
 * Service for executing complex queries for {@link CardBrandType} entities in the database.
 * The main input is a {@link CardBrandTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardBrandTypeDTO} or a {@link Page} of {@link CardBrandTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardBrandTypeQueryService extends QueryService<CardBrandType> {

    private final Logger log = LoggerFactory.getLogger(CardBrandTypeQueryService.class);

    private final CardBrandTypeRepository cardBrandTypeRepository;

    private final CardBrandTypeMapper cardBrandTypeMapper;

    private final CardBrandTypeSearchRepository cardBrandTypeSearchRepository;

    public CardBrandTypeQueryService(
        CardBrandTypeRepository cardBrandTypeRepository,
        CardBrandTypeMapper cardBrandTypeMapper,
        CardBrandTypeSearchRepository cardBrandTypeSearchRepository
    ) {
        this.cardBrandTypeRepository = cardBrandTypeRepository;
        this.cardBrandTypeMapper = cardBrandTypeMapper;
        this.cardBrandTypeSearchRepository = cardBrandTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardBrandTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardBrandTypeDTO> findByCriteria(CardBrandTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardBrandType> specification = createSpecification(criteria);
        return cardBrandTypeMapper.toDto(cardBrandTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardBrandTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardBrandTypeDTO> findByCriteria(CardBrandTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardBrandType> specification = createSpecification(criteria);
        return cardBrandTypeRepository.findAll(specification, page).map(cardBrandTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardBrandTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardBrandType> specification = createSpecification(criteria);
        return cardBrandTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CardBrandTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardBrandType> createSpecification(CardBrandTypeCriteria criteria) {
        Specification<CardBrandType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardBrandType_.id));
            }
            if (criteria.getCardBrandTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCardBrandTypeCode(), CardBrandType_.cardBrandTypeCode));
            }
            if (criteria.getCardBrandType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardBrandType(), CardBrandType_.cardBrandType));
            }
        }
        return specification;
    }
}
