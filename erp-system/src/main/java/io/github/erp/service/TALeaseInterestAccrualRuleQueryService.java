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
import io.github.erp.domain.TALeaseInterestAccrualRule;
import io.github.erp.repository.TALeaseInterestAccrualRuleRepository;
import io.github.erp.repository.search.TALeaseInterestAccrualRuleSearchRepository;
import io.github.erp.service.criteria.TALeaseInterestAccrualRuleCriteria;
import io.github.erp.service.dto.TALeaseInterestAccrualRuleDTO;
import io.github.erp.service.mapper.TALeaseInterestAccrualRuleMapper;
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
 * Service for executing complex queries for {@link TALeaseInterestAccrualRule} entities in the database.
 * The main input is a {@link TALeaseInterestAccrualRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TALeaseInterestAccrualRuleDTO} or a {@link Page} of {@link TALeaseInterestAccrualRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TALeaseInterestAccrualRuleQueryService extends QueryService<TALeaseInterestAccrualRule> {

    private final Logger log = LoggerFactory.getLogger(TALeaseInterestAccrualRuleQueryService.class);

    private final TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository;

    private final TALeaseInterestAccrualRuleMapper tALeaseInterestAccrualRuleMapper;

    private final TALeaseInterestAccrualRuleSearchRepository tALeaseInterestAccrualRuleSearchRepository;

    public TALeaseInterestAccrualRuleQueryService(
        TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository,
        TALeaseInterestAccrualRuleMapper tALeaseInterestAccrualRuleMapper,
        TALeaseInterestAccrualRuleSearchRepository tALeaseInterestAccrualRuleSearchRepository
    ) {
        this.tALeaseInterestAccrualRuleRepository = tALeaseInterestAccrualRuleRepository;
        this.tALeaseInterestAccrualRuleMapper = tALeaseInterestAccrualRuleMapper;
        this.tALeaseInterestAccrualRuleSearchRepository = tALeaseInterestAccrualRuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TALeaseInterestAccrualRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TALeaseInterestAccrualRuleDTO> findByCriteria(TALeaseInterestAccrualRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TALeaseInterestAccrualRule> specification = createSpecification(criteria);
        return tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TALeaseInterestAccrualRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TALeaseInterestAccrualRuleDTO> findByCriteria(TALeaseInterestAccrualRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TALeaseInterestAccrualRule> specification = createSpecification(criteria);
        return tALeaseInterestAccrualRuleRepository.findAll(specification, page).map(tALeaseInterestAccrualRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TALeaseInterestAccrualRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TALeaseInterestAccrualRule> specification = createSpecification(criteria);
        return tALeaseInterestAccrualRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link TALeaseInterestAccrualRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TALeaseInterestAccrualRule> createSpecification(TALeaseInterestAccrualRuleCriteria criteria) {
        Specification<TALeaseInterestAccrualRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TALeaseInterestAccrualRule_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TALeaseInterestAccrualRule_.name));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), TALeaseInterestAccrualRule_.identifier));
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(TALeaseInterestAccrualRule_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getDebitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitId(),
                            root -> root.join(TALeaseInterestAccrualRule_.debit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditId(),
                            root -> root.join(TALeaseInterestAccrualRule_.credit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TALeaseInterestAccrualRule_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
