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
import io.github.erp.domain.CardIssuerCharges;
import io.github.erp.repository.CardIssuerChargesRepository;
import io.github.erp.repository.search.CardIssuerChargesSearchRepository;
import io.github.erp.service.criteria.CardIssuerChargesCriteria;
import io.github.erp.service.dto.CardIssuerChargesDTO;
import io.github.erp.service.mapper.CardIssuerChargesMapper;
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
 * Service for executing complex queries for {@link CardIssuerCharges} entities in the database.
 * The main input is a {@link CardIssuerChargesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardIssuerChargesDTO} or a {@link Page} of {@link CardIssuerChargesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardIssuerChargesQueryService extends QueryService<CardIssuerCharges> {

    private final Logger log = LoggerFactory.getLogger(CardIssuerChargesQueryService.class);

    private final CardIssuerChargesRepository cardIssuerChargesRepository;

    private final CardIssuerChargesMapper cardIssuerChargesMapper;

    private final CardIssuerChargesSearchRepository cardIssuerChargesSearchRepository;

    public CardIssuerChargesQueryService(
        CardIssuerChargesRepository cardIssuerChargesRepository,
        CardIssuerChargesMapper cardIssuerChargesMapper,
        CardIssuerChargesSearchRepository cardIssuerChargesSearchRepository
    ) {
        this.cardIssuerChargesRepository = cardIssuerChargesRepository;
        this.cardIssuerChargesMapper = cardIssuerChargesMapper;
        this.cardIssuerChargesSearchRepository = cardIssuerChargesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardIssuerChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardIssuerChargesDTO> findByCriteria(CardIssuerChargesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardIssuerCharges> specification = createSpecification(criteria);
        return cardIssuerChargesMapper.toDto(cardIssuerChargesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardIssuerChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardIssuerChargesDTO> findByCriteria(CardIssuerChargesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardIssuerCharges> specification = createSpecification(criteria);
        return cardIssuerChargesRepository.findAll(specification, page).map(cardIssuerChargesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardIssuerChargesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardIssuerCharges> specification = createSpecification(criteria);
        return cardIssuerChargesRepository.count(specification);
    }

    /**
     * Function to convert {@link CardIssuerChargesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardIssuerCharges> createSpecification(CardIssuerChargesCriteria criteria) {
        Specification<CardIssuerCharges> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardIssuerCharges_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), CardIssuerCharges_.reportingDate));
            }
            if (criteria.getCardFeeChargeInLCY() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCardFeeChargeInLCY(), CardIssuerCharges_.cardFeeChargeInLCY));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(CardIssuerCharges_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getCardCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardCategoryId(),
                            root -> root.join(CardIssuerCharges_.cardCategory, JoinType.LEFT).get(CardCategoryType_.id)
                        )
                    );
            }
            if (criteria.getCardTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardTypeId(),
                            root -> root.join(CardIssuerCharges_.cardType, JoinType.LEFT).get(CardTypes_.id)
                        )
                    );
            }
            if (criteria.getCardBrandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardBrandId(),
                            root -> root.join(CardIssuerCharges_.cardBrand, JoinType.LEFT).get(CardBrandType_.id)
                        )
                    );
            }
            if (criteria.getCardClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardClassId(),
                            root -> root.join(CardIssuerCharges_.cardClass, JoinType.LEFT).get(CardClassType_.id)
                        )
                    );
            }
            if (criteria.getCardChargeTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardChargeTypeId(),
                            root -> root.join(CardIssuerCharges_.cardChargeType, JoinType.LEFT).get(CardCharges_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
