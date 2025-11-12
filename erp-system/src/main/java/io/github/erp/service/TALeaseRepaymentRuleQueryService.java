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
import io.github.erp.domain.TALeaseRepaymentRule;
import io.github.erp.repository.TALeaseRepaymentRuleRepository;
import io.github.erp.repository.search.TALeaseRepaymentRuleSearchRepository;
import io.github.erp.service.criteria.TALeaseRepaymentRuleCriteria;
import io.github.erp.service.dto.TALeaseRepaymentRuleDTO;
import io.github.erp.service.mapper.TALeaseRepaymentRuleMapper;
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
 * Service for executing complex queries for {@link TALeaseRepaymentRule} entities in the database.
 * The main input is a {@link TALeaseRepaymentRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TALeaseRepaymentRuleDTO} or a {@link Page} of {@link TALeaseRepaymentRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TALeaseRepaymentRuleQueryService extends QueryService<TALeaseRepaymentRule> {

    private final Logger log = LoggerFactory.getLogger(TALeaseRepaymentRuleQueryService.class);

    private final TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository;

    private final TALeaseRepaymentRuleMapper tALeaseRepaymentRuleMapper;

    private final TALeaseRepaymentRuleSearchRepository tALeaseRepaymentRuleSearchRepository;

    public TALeaseRepaymentRuleQueryService(
        TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository,
        TALeaseRepaymentRuleMapper tALeaseRepaymentRuleMapper,
        TALeaseRepaymentRuleSearchRepository tALeaseRepaymentRuleSearchRepository
    ) {
        this.tALeaseRepaymentRuleRepository = tALeaseRepaymentRuleRepository;
        this.tALeaseRepaymentRuleMapper = tALeaseRepaymentRuleMapper;
        this.tALeaseRepaymentRuleSearchRepository = tALeaseRepaymentRuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TALeaseRepaymentRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TALeaseRepaymentRuleDTO> findByCriteria(TALeaseRepaymentRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TALeaseRepaymentRule> specification = createSpecification(criteria);
        return tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TALeaseRepaymentRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TALeaseRepaymentRuleDTO> findByCriteria(TALeaseRepaymentRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TALeaseRepaymentRule> specification = createSpecification(criteria);
        return tALeaseRepaymentRuleRepository.findAll(specification, page).map(tALeaseRepaymentRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TALeaseRepaymentRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TALeaseRepaymentRule> specification = createSpecification(criteria);
        return tALeaseRepaymentRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link TALeaseRepaymentRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TALeaseRepaymentRule> createSpecification(TALeaseRepaymentRuleCriteria criteria) {
        Specification<TALeaseRepaymentRule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TALeaseRepaymentRule_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TALeaseRepaymentRule_.name));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), TALeaseRepaymentRule_.identifier));
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(TALeaseRepaymentRule_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getDebitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitId(),
                            root -> root.join(TALeaseRepaymentRule_.debit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditId(),
                            root -> root.join(TALeaseRepaymentRule_.credit, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TALeaseRepaymentRule_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
