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
import io.github.erp.domain.CardCharges;
import io.github.erp.repository.CardChargesRepository;
import io.github.erp.repository.search.CardChargesSearchRepository;
import io.github.erp.service.criteria.CardChargesCriteria;
import io.github.erp.service.dto.CardChargesDTO;
import io.github.erp.service.mapper.CardChargesMapper;
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
 * Service for executing complex queries for {@link CardCharges} entities in the database.
 * The main input is a {@link CardChargesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardChargesDTO} or a {@link Page} of {@link CardChargesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardChargesQueryService extends QueryService<CardCharges> {

    private final Logger log = LoggerFactory.getLogger(CardChargesQueryService.class);

    private final CardChargesRepository cardChargesRepository;

    private final CardChargesMapper cardChargesMapper;

    private final CardChargesSearchRepository cardChargesSearchRepository;

    public CardChargesQueryService(
        CardChargesRepository cardChargesRepository,
        CardChargesMapper cardChargesMapper,
        CardChargesSearchRepository cardChargesSearchRepository
    ) {
        this.cardChargesRepository = cardChargesRepository;
        this.cardChargesMapper = cardChargesMapper;
        this.cardChargesSearchRepository = cardChargesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardChargesDTO> findByCriteria(CardChargesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardCharges> specification = createSpecification(criteria);
        return cardChargesMapper.toDto(cardChargesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardChargesDTO> findByCriteria(CardChargesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardCharges> specification = createSpecification(criteria);
        return cardChargesRepository.findAll(specification, page).map(cardChargesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardChargesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardCharges> specification = createSpecification(criteria);
        return cardChargesRepository.count(specification);
    }

    /**
     * Function to convert {@link CardChargesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardCharges> createSpecification(CardChargesCriteria criteria) {
        Specification<CardCharges> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardCharges_.id));
            }
            if (criteria.getCardChargeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardChargeType(), CardCharges_.cardChargeType));
            }
            if (criteria.getCardChargeTypeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCardChargeTypeName(), CardCharges_.cardChargeTypeName));
            }
        }
        return specification;
    }
}
