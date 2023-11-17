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
import io.github.erp.domain.CardUsageInformation;
import io.github.erp.repository.CardUsageInformationRepository;
import io.github.erp.repository.search.CardUsageInformationSearchRepository;
import io.github.erp.service.criteria.CardUsageInformationCriteria;
import io.github.erp.service.dto.CardUsageInformationDTO;
import io.github.erp.service.mapper.CardUsageInformationMapper;
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
 * Service for executing complex queries for {@link CardUsageInformation} entities in the database.
 * The main input is a {@link CardUsageInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardUsageInformationDTO} or a {@link Page} of {@link CardUsageInformationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardUsageInformationQueryService extends QueryService<CardUsageInformation> {

    private final Logger log = LoggerFactory.getLogger(CardUsageInformationQueryService.class);

    private final CardUsageInformationRepository cardUsageInformationRepository;

    private final CardUsageInformationMapper cardUsageInformationMapper;

    private final CardUsageInformationSearchRepository cardUsageInformationSearchRepository;

    public CardUsageInformationQueryService(
        CardUsageInformationRepository cardUsageInformationRepository,
        CardUsageInformationMapper cardUsageInformationMapper,
        CardUsageInformationSearchRepository cardUsageInformationSearchRepository
    ) {
        this.cardUsageInformationRepository = cardUsageInformationRepository;
        this.cardUsageInformationMapper = cardUsageInformationMapper;
        this.cardUsageInformationSearchRepository = cardUsageInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardUsageInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardUsageInformationDTO> findByCriteria(CardUsageInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardUsageInformation> specification = createSpecification(criteria);
        return cardUsageInformationMapper.toDto(cardUsageInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardUsageInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardUsageInformationDTO> findByCriteria(CardUsageInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardUsageInformation> specification = createSpecification(criteria);
        return cardUsageInformationRepository.findAll(specification, page).map(cardUsageInformationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardUsageInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardUsageInformation> specification = createSpecification(criteria);
        return cardUsageInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link CardUsageInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardUsageInformation> createSpecification(CardUsageInformationCriteria criteria) {
        Specification<CardUsageInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardUsageInformation_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), CardUsageInformation_.reportingDate));
            }
            if (criteria.getTotalNumberOfLiveCards() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalNumberOfLiveCards(), CardUsageInformation_.totalNumberOfLiveCards)
                    );
            }
            if (criteria.getTotalActiveCards() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTotalActiveCards(), CardUsageInformation_.totalActiveCards));
            }
            if (criteria.getTotalNumberOfTransactionsDone() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalNumberOfTransactionsDone(),
                            CardUsageInformation_.totalNumberOfTransactionsDone
                        )
                    );
            }
            if (criteria.getTotalValueOfTransactionsDoneInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalValueOfTransactionsDoneInLCY(),
                            CardUsageInformation_.totalValueOfTransactionsDoneInLCY
                        )
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(CardUsageInformation_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getCardTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardTypeId(),
                            root -> root.join(CardUsageInformation_.cardType, JoinType.LEFT).get(CardTypes_.id)
                        )
                    );
            }
            if (criteria.getCardBrandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardBrandId(),
                            root -> root.join(CardUsageInformation_.cardBrand, JoinType.LEFT).get(CardBrandType_.id)
                        )
                    );
            }
            if (criteria.getCardCategoryTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardCategoryTypeId(),
                            root -> root.join(CardUsageInformation_.cardCategoryType, JoinType.LEFT).get(CardCategoryType_.id)
                        )
                    );
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(CardUsageInformation_.transactionType, JoinType.LEFT).get(BankTransactionType_.id)
                        )
                    );
            }
            if (criteria.getChannelTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChannelTypeId(),
                            root -> root.join(CardUsageInformation_.channelType, JoinType.LEFT).get(ChannelType_.id)
                        )
                    );
            }
            if (criteria.getCardStateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardStateId(),
                            root -> root.join(CardUsageInformation_.cardState, JoinType.LEFT).get(CardState_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
