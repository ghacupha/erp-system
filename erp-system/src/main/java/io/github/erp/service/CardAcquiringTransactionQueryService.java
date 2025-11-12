package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.CardAcquiringTransaction;
import io.github.erp.repository.CardAcquiringTransactionRepository;
import io.github.erp.repository.search.CardAcquiringTransactionSearchRepository;
import io.github.erp.service.criteria.CardAcquiringTransactionCriteria;
import io.github.erp.service.dto.CardAcquiringTransactionDTO;
import io.github.erp.service.mapper.CardAcquiringTransactionMapper;
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
 * Service for executing complex queries for {@link CardAcquiringTransaction} entities in the database.
 * The main input is a {@link CardAcquiringTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardAcquiringTransactionDTO} or a {@link Page} of {@link CardAcquiringTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardAcquiringTransactionQueryService extends QueryService<CardAcquiringTransaction> {

    private final Logger log = LoggerFactory.getLogger(CardAcquiringTransactionQueryService.class);

    private final CardAcquiringTransactionRepository cardAcquiringTransactionRepository;

    private final CardAcquiringTransactionMapper cardAcquiringTransactionMapper;

    private final CardAcquiringTransactionSearchRepository cardAcquiringTransactionSearchRepository;

    public CardAcquiringTransactionQueryService(
        CardAcquiringTransactionRepository cardAcquiringTransactionRepository,
        CardAcquiringTransactionMapper cardAcquiringTransactionMapper,
        CardAcquiringTransactionSearchRepository cardAcquiringTransactionSearchRepository
    ) {
        this.cardAcquiringTransactionRepository = cardAcquiringTransactionRepository;
        this.cardAcquiringTransactionMapper = cardAcquiringTransactionMapper;
        this.cardAcquiringTransactionSearchRepository = cardAcquiringTransactionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardAcquiringTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardAcquiringTransactionDTO> findByCriteria(CardAcquiringTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardAcquiringTransaction> specification = createSpecification(criteria);
        return cardAcquiringTransactionMapper.toDto(cardAcquiringTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardAcquiringTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardAcquiringTransactionDTO> findByCriteria(CardAcquiringTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardAcquiringTransaction> specification = createSpecification(criteria);
        return cardAcquiringTransactionRepository.findAll(specification, page).map(cardAcquiringTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardAcquiringTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardAcquiringTransaction> specification = createSpecification(criteria);
        return cardAcquiringTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link CardAcquiringTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardAcquiringTransaction> createSpecification(CardAcquiringTransactionCriteria criteria) {
        Specification<CardAcquiringTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardAcquiringTransaction_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), CardAcquiringTransaction_.reportingDate));
            }
            if (criteria.getTerminalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerminalId(), CardAcquiringTransaction_.terminalId));
            }
            if (criteria.getNumberOfTransactions() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfTransactions(), CardAcquiringTransaction_.numberOfTransactions)
                    );
            }
            if (criteria.getValueOfTransactionsInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getValueOfTransactionsInLCY(), CardAcquiringTransaction_.valueOfTransactionsInLCY)
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(CardAcquiringTransaction_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getChannelTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChannelTypeId(),
                            root -> root.join(CardAcquiringTransaction_.channelType, JoinType.LEFT).get(ChannelType_.id)
                        )
                    );
            }
            if (criteria.getCardBrandTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardBrandTypeId(),
                            root -> root.join(CardAcquiringTransaction_.cardBrandType, JoinType.LEFT).get(CardBrandType_.id)
                        )
                    );
            }
            if (criteria.getCurrencyOfTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyOfTransactionId(),
                            root -> root.join(CardAcquiringTransaction_.currencyOfTransaction, JoinType.LEFT).get(IsoCurrencyCode_.id)
                        )
                    );
            }
            if (criteria.getCardIssuerCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCardIssuerCategoryId(),
                            root -> root.join(CardAcquiringTransaction_.cardIssuerCategory, JoinType.LEFT).get(CardCategoryType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
