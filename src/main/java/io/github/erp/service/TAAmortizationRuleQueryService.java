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
import io.github.erp.domain.TAAmortizationRule;
import io.github.erp.repository.TAAmortizationRuleRepository;
import io.github.erp.repository.search.TAAmortizationRuleSearchRepository;
import io.github.erp.service.criteria.TAAmortizationRuleCriteria;
import io.github.erp.service.dto.TAAmortizationRuleDTO;
import io.github.erp.service.mapper.TAAmortizationRuleMapper;
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
 * Service for executing complex queries for {@link TAAmortizationRule} entities in the database.
 * The main input is a {@link TAAmortizationRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TAAmortizationRuleDTO} or a {@link Page} of {@link TAAmortizationRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TAAmortizationRuleQueryService extends QueryService<TAAmortizationRule> {

    private final Logger log = LoggerFactory.getLogger(TAAmortizationRuleQueryService.class);

    private final TAAmortizationRuleRepository tAAmortizationRuleRepository;

    private final TAAmortizationRuleMapper tAAmortizationRuleMapper;

    private final TAAmortizationRuleSearchRepository tAAmortizationRuleSearchRepository;

    public TAAmortizationRuleQueryService(
        TAAmortizationRuleRepository tAAmortizationRuleRepository,
        TAAmortizationRuleMapper tAAmortizationRuleMapper,
        TAAmortizationRuleSearchRepository tAAmortizationRuleSearchRepository
    ) {
        this.tAAmortizationRuleRepository = tAAmortizationRuleRepository;
        this.tAAmortizationRuleMapper = tAAmortizationRuleMapper;
        this.tAAmortizationRuleSearchRepository = tAAmortizationRuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TAAmortizationRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TAAmortizationRuleDTO> findByCriteria(TAAmortizationRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TAAmortizationRule> specification = createSpecification(criteria);
        return tAAmortizationRuleMapper.toDto(tAAmortizationRuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TAAmortizationRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TAAmortizationRuleDTO> findByCriteria(TAAmortizationRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TAAmortizationRule> specification = createSpecification(criteria);
        return tAAmortizationRuleRepository.findAll(specification, page).map(tAAmortizationRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TAAmortizationRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TAAmortizationRule> specification = createSpecification(criteria);
        return tAAmortizationRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link TAAmortizationRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TAAmortizationRule> createSpecification(TAAmortizationRuleCriteria criteria) {
        Specification<TAAmortizationRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TAAmortizationRule_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TAAmortizationRule_.name));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), TAAmortizationRule_.identifier));
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(TAAmortizationRule_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getDebitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitId(),
                            root -> root.join(TAAmortizationRule_.debit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditId(),
                            root -> root.join(TAAmortizationRule_.credit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TAAmortizationRule_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
