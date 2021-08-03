package io.github.erp.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.erp.domain.TaxRule;
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.repository.TaxRuleRepository;
import io.github.erp.repository.search.TaxRuleSearchRepository;
import io.github.erp.service.dto.TaxRuleCriteria;
import io.github.erp.service.dto.TaxRuleDTO;
import io.github.erp.service.mapper.TaxRuleMapper;

/**
 * Service for executing complex queries for {@link TaxRule} entities in the database.
 * The main input is a {@link TaxRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxRuleDTO} or a {@link Page} of {@link TaxRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxRuleQueryService extends QueryService<TaxRule> {

    private final Logger log = LoggerFactory.getLogger(TaxRuleQueryService.class);

    private final TaxRuleRepository taxRuleRepository;

    private final TaxRuleMapper taxRuleMapper;

    private final TaxRuleSearchRepository taxRuleSearchRepository;

    public TaxRuleQueryService(TaxRuleRepository taxRuleRepository, TaxRuleMapper taxRuleMapper, TaxRuleSearchRepository taxRuleSearchRepository) {
        this.taxRuleRepository = taxRuleRepository;
        this.taxRuleMapper = taxRuleMapper;
        this.taxRuleSearchRepository = taxRuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaxRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxRuleDTO> findByCriteria(TaxRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaxRule> specification = createSpecification(criteria);
        return taxRuleMapper.toDto(taxRuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaxRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxRuleDTO> findByCriteria(TaxRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaxRule> specification = createSpecification(criteria);
        return taxRuleRepository.findAll(specification, page)
            .map(taxRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaxRule> specification = createSpecification(criteria);
        return taxRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxRule> createSpecification(TaxRuleCriteria criteria) {
        Specification<TaxRule> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaxRule_.id));
            }
            if (criteria.getPaymentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentNumber(), TaxRule_.paymentNumber));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), TaxRule_.paymentDate));
            }
            if (criteria.getTelcoExciseDuty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTelcoExciseDuty(), TaxRule_.telcoExciseDuty));
            }
            if (criteria.getValueAddedTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueAddedTax(), TaxRule_.valueAddedTax));
            }
            if (criteria.getWithholdingVAT() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWithholdingVAT(), TaxRule_.withholdingVAT));
            }
            if (criteria.getWithholdingTaxConsultancy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWithholdingTaxConsultancy(), TaxRule_.withholdingTaxConsultancy));
            }
            if (criteria.getWithholdingTaxRent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWithholdingTaxRent(), TaxRule_.withholdingTaxRent));
            }
            if (criteria.getCateringLevy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCateringLevy(), TaxRule_.cateringLevy));
            }
            if (criteria.getServiceCharge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getServiceCharge(), TaxRule_.serviceCharge));
            }
            if (criteria.getWithholdingTaxImportedService() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWithholdingTaxImportedService(), TaxRule_.withholdingTaxImportedService));
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentId(),
                    root -> root.join(TaxRule_.payment, JoinType.LEFT).get(Payment_.id)));
            }
        }
        return specification;
    }
}
