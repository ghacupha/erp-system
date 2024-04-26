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
import io.github.erp.domain.CreditCardFacility;
import io.github.erp.repository.CreditCardFacilityRepository;
import io.github.erp.repository.search.CreditCardFacilitySearchRepository;
import io.github.erp.service.criteria.CreditCardFacilityCriteria;
import io.github.erp.service.dto.CreditCardFacilityDTO;
import io.github.erp.service.mapper.CreditCardFacilityMapper;
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
 * Service for executing complex queries for {@link CreditCardFacility} entities in the database.
 * The main input is a {@link CreditCardFacilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CreditCardFacilityDTO} or a {@link Page} of {@link CreditCardFacilityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CreditCardFacilityQueryService extends QueryService<CreditCardFacility> {

    private final Logger log = LoggerFactory.getLogger(CreditCardFacilityQueryService.class);

    private final CreditCardFacilityRepository creditCardFacilityRepository;

    private final CreditCardFacilityMapper creditCardFacilityMapper;

    private final CreditCardFacilitySearchRepository creditCardFacilitySearchRepository;

    public CreditCardFacilityQueryService(
        CreditCardFacilityRepository creditCardFacilityRepository,
        CreditCardFacilityMapper creditCardFacilityMapper,
        CreditCardFacilitySearchRepository creditCardFacilitySearchRepository
    ) {
        this.creditCardFacilityRepository = creditCardFacilityRepository;
        this.creditCardFacilityMapper = creditCardFacilityMapper;
        this.creditCardFacilitySearchRepository = creditCardFacilitySearchRepository;
    }

    /**
     * Return a {@link List} of {@link CreditCardFacilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CreditCardFacilityDTO> findByCriteria(CreditCardFacilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CreditCardFacility> specification = createSpecification(criteria);
        return creditCardFacilityMapper.toDto(creditCardFacilityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CreditCardFacilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CreditCardFacilityDTO> findByCriteria(CreditCardFacilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CreditCardFacility> specification = createSpecification(criteria);
        return creditCardFacilityRepository.findAll(specification, page).map(creditCardFacilityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CreditCardFacilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CreditCardFacility> specification = createSpecification(criteria);
        return creditCardFacilityRepository.count(specification);
    }

    /**
     * Function to convert {@link CreditCardFacilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CreditCardFacility> createSpecification(CreditCardFacilityCriteria criteria) {
        Specification<CreditCardFacility> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CreditCardFacility_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), CreditCardFacility_.reportingDate));
            }
            if (criteria.getTotalNumberOfActiveCreditCards() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalNumberOfActiveCreditCards(),
                            CreditCardFacility_.totalNumberOfActiveCreditCards
                        )
                    );
            }
            if (criteria.getTotalCreditCardLimitsInCCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalCreditCardLimitsInCCY(), CreditCardFacility_.totalCreditCardLimitsInCCY)
                    );
            }
            if (criteria.getTotalCreditCardLimitsInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalCreditCardLimitsInLCY(), CreditCardFacility_.totalCreditCardLimitsInLCY)
                    );
            }
            if (criteria.getTotalCreditCardAmountUtilisedInCCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalCreditCardAmountUtilisedInCCY(),
                            CreditCardFacility_.totalCreditCardAmountUtilisedInCCY
                        )
                    );
            }
            if (criteria.getTotalCreditCardAmountUtilisedInLcy() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalCreditCardAmountUtilisedInLcy(),
                            CreditCardFacility_.totalCreditCardAmountUtilisedInLcy
                        )
                    );
            }
            if (criteria.getTotalNPACreditCardAmountInFCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalNPACreditCardAmountInFCY(),
                            CreditCardFacility_.totalNPACreditCardAmountInFCY
                        )
                    );
            }
            if (criteria.getTotalNPACreditCardAmountInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalNPACreditCardAmountInLCY(),
                            CreditCardFacility_.totalNPACreditCardAmountInLCY
                        )
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(CreditCardFacility_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getCustomerCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerCategoryId(),
                            root -> root.join(CreditCardFacility_.customerCategory, JoinType.LEFT).get(CreditCardOwnership_.id)
                        )
                    );
            }
            if (criteria.getCurrencyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyCodeId(),
                            root -> root.join(CreditCardFacility_.currencyCode, JoinType.LEFT).get(IsoCurrencyCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
