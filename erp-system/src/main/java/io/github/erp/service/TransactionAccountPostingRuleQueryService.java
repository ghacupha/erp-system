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
import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.service.criteria.TransactionAccountPostingRuleCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRuleDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRuleMapper;
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
 * Service for executing complex queries for {@link TransactionAccountPostingRule} entities in the database.
 * The main input is a {@link TransactionAccountPostingRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionAccountPostingRuleDTO} or a {@link Page} of {@link TransactionAccountPostingRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionAccountPostingRuleQueryService extends QueryService<TransactionAccountPostingRule> {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRuleQueryService.class);

    private final TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository;

    private final TransactionAccountPostingRuleMapper transactionAccountPostingRuleMapper;

    private final TransactionAccountPostingRuleSearchRepository transactionAccountPostingRuleSearchRepository;

    public TransactionAccountPostingRuleQueryService(
        TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository,
        TransactionAccountPostingRuleMapper transactionAccountPostingRuleMapper,
        TransactionAccountPostingRuleSearchRepository transactionAccountPostingRuleSearchRepository
    ) {
        this.transactionAccountPostingRuleRepository = transactionAccountPostingRuleRepository;
        this.transactionAccountPostingRuleMapper = transactionAccountPostingRuleMapper;
        this.transactionAccountPostingRuleSearchRepository = transactionAccountPostingRuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionAccountPostingRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionAccountPostingRuleDTO> findByCriteria(TransactionAccountPostingRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionAccountPostingRule> specification = createSpecification(criteria);
        return transactionAccountPostingRuleMapper.toDto(transactionAccountPostingRuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionAccountPostingRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRuleDTO> findByCriteria(TransactionAccountPostingRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionAccountPostingRule> specification = createSpecification(criteria);
        return transactionAccountPostingRuleRepository.findAll(specification, page).map(transactionAccountPostingRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionAccountPostingRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionAccountPostingRule> specification = createSpecification(criteria);
        return transactionAccountPostingRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionAccountPostingRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionAccountPostingRule> createSpecification(TransactionAccountPostingRuleCriteria criteria) {
        Specification<TransactionAccountPostingRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionAccountPostingRule_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TransactionAccountPostingRule_.name));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), TransactionAccountPostingRule_.identifier));
            }
            if (criteria.getDebitAccountTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountTypeId(),
                            root ->
                                root
                                    .join(TransactionAccountPostingRule_.debitAccountType, JoinType.LEFT)
                                    .get(TransactionAccountCategory_.id)
                        )
                    );
            }
            if (criteria.getCreditAccountTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditAccountTypeId(),
                            root ->
                                root
                                    .join(TransactionAccountPostingRule_.creditAccountType, JoinType.LEFT)
                                    .get(TransactionAccountCategory_.id)
                        )
                    );
            }
            if (criteria.getTransactionContextId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionContextId(),
                            root -> root.join(TransactionAccountPostingRule_.transactionContext, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
