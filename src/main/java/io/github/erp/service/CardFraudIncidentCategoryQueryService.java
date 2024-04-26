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
import io.github.erp.domain.CardFraudIncidentCategory;
import io.github.erp.repository.CardFraudIncidentCategoryRepository;
import io.github.erp.repository.search.CardFraudIncidentCategorySearchRepository;
import io.github.erp.service.criteria.CardFraudIncidentCategoryCriteria;
import io.github.erp.service.dto.CardFraudIncidentCategoryDTO;
import io.github.erp.service.mapper.CardFraudIncidentCategoryMapper;
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
 * Service for executing complex queries for {@link CardFraudIncidentCategory} entities in the database.
 * The main input is a {@link CardFraudIncidentCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardFraudIncidentCategoryDTO} or a {@link Page} of {@link CardFraudIncidentCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardFraudIncidentCategoryQueryService extends QueryService<CardFraudIncidentCategory> {

    private final Logger log = LoggerFactory.getLogger(CardFraudIncidentCategoryQueryService.class);

    private final CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository;

    private final CardFraudIncidentCategoryMapper cardFraudIncidentCategoryMapper;

    private final CardFraudIncidentCategorySearchRepository cardFraudIncidentCategorySearchRepository;

    public CardFraudIncidentCategoryQueryService(
        CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository,
        CardFraudIncidentCategoryMapper cardFraudIncidentCategoryMapper,
        CardFraudIncidentCategorySearchRepository cardFraudIncidentCategorySearchRepository
    ) {
        this.cardFraudIncidentCategoryRepository = cardFraudIncidentCategoryRepository;
        this.cardFraudIncidentCategoryMapper = cardFraudIncidentCategoryMapper;
        this.cardFraudIncidentCategorySearchRepository = cardFraudIncidentCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardFraudIncidentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardFraudIncidentCategoryDTO> findByCriteria(CardFraudIncidentCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardFraudIncidentCategory> specification = createSpecification(criteria);
        return cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardFraudIncidentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardFraudIncidentCategoryDTO> findByCriteria(CardFraudIncidentCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardFraudIncidentCategory> specification = createSpecification(criteria);
        return cardFraudIncidentCategoryRepository.findAll(specification, page).map(cardFraudIncidentCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardFraudIncidentCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardFraudIncidentCategory> specification = createSpecification(criteria);
        return cardFraudIncidentCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CardFraudIncidentCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardFraudIncidentCategory> createSpecification(CardFraudIncidentCategoryCriteria criteria) {
        Specification<CardFraudIncidentCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardFraudIncidentCategory_.id));
            }
            if (criteria.getCardFraudCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCardFraudCategoryTypeCode(),
                            CardFraudIncidentCategory_.cardFraudCategoryTypeCode
                        )
                    );
            }
            if (criteria.getCardFraudCategoryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCardFraudCategoryType(), CardFraudIncidentCategory_.cardFraudCategoryType)
                    );
            }
        }
        return specification;
    }
}
