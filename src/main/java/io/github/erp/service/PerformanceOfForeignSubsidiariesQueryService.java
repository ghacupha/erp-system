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
import io.github.erp.domain.PerformanceOfForeignSubsidiaries;
import io.github.erp.repository.PerformanceOfForeignSubsidiariesRepository;
import io.github.erp.repository.search.PerformanceOfForeignSubsidiariesSearchRepository;
import io.github.erp.service.criteria.PerformanceOfForeignSubsidiariesCriteria;
import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
import io.github.erp.service.mapper.PerformanceOfForeignSubsidiariesMapper;
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
 * Service for executing complex queries for {@link PerformanceOfForeignSubsidiaries} entities in the database.
 * The main input is a {@link PerformanceOfForeignSubsidiariesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PerformanceOfForeignSubsidiariesDTO} or a {@link Page} of {@link PerformanceOfForeignSubsidiariesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PerformanceOfForeignSubsidiariesQueryService extends QueryService<PerformanceOfForeignSubsidiaries> {

    private final Logger log = LoggerFactory.getLogger(PerformanceOfForeignSubsidiariesQueryService.class);

    private final PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository;

    private final PerformanceOfForeignSubsidiariesMapper performanceOfForeignSubsidiariesMapper;

    private final PerformanceOfForeignSubsidiariesSearchRepository performanceOfForeignSubsidiariesSearchRepository;

    public PerformanceOfForeignSubsidiariesQueryService(
        PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository,
        PerformanceOfForeignSubsidiariesMapper performanceOfForeignSubsidiariesMapper,
        PerformanceOfForeignSubsidiariesSearchRepository performanceOfForeignSubsidiariesSearchRepository
    ) {
        this.performanceOfForeignSubsidiariesRepository = performanceOfForeignSubsidiariesRepository;
        this.performanceOfForeignSubsidiariesMapper = performanceOfForeignSubsidiariesMapper;
        this.performanceOfForeignSubsidiariesSearchRepository = performanceOfForeignSubsidiariesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PerformanceOfForeignSubsidiariesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PerformanceOfForeignSubsidiariesDTO> findByCriteria(PerformanceOfForeignSubsidiariesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PerformanceOfForeignSubsidiaries> specification = createSpecification(criteria);
        return performanceOfForeignSubsidiariesMapper.toDto(performanceOfForeignSubsidiariesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PerformanceOfForeignSubsidiariesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PerformanceOfForeignSubsidiariesDTO> findByCriteria(PerformanceOfForeignSubsidiariesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PerformanceOfForeignSubsidiaries> specification = createSpecification(criteria);
        return performanceOfForeignSubsidiariesRepository.findAll(specification, page).map(performanceOfForeignSubsidiariesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PerformanceOfForeignSubsidiariesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PerformanceOfForeignSubsidiaries> specification = createSpecification(criteria);
        return performanceOfForeignSubsidiariesRepository.count(specification);
    }

    /**
     * Function to convert {@link PerformanceOfForeignSubsidiariesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PerformanceOfForeignSubsidiaries> createSpecification(PerformanceOfForeignSubsidiariesCriteria criteria) {
        Specification<PerformanceOfForeignSubsidiaries> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PerformanceOfForeignSubsidiaries_.id));
            }
            if (criteria.getSubsidiaryName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSubsidiaryName(), PerformanceOfForeignSubsidiaries_.subsidiaryName)
                    );
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getReportingDate(), PerformanceOfForeignSubsidiaries_.reportingDate)
                    );
            }
            if (criteria.getSubsidiaryId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubsidiaryId(), PerformanceOfForeignSubsidiaries_.subsidiaryId));
            }
            if (criteria.getGrossLoansAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getGrossLoansAmount(), PerformanceOfForeignSubsidiaries_.grossLoansAmount)
                    );
            }
            if (criteria.getGrossNPALoanAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getGrossNPALoanAmount(), PerformanceOfForeignSubsidiaries_.grossNPALoanAmount)
                    );
            }
            if (criteria.getGrossAssetsAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getGrossAssetsAmount(), PerformanceOfForeignSubsidiaries_.grossAssetsAmount)
                    );
            }
            if (criteria.getGrossDepositsAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getGrossDepositsAmount(), PerformanceOfForeignSubsidiaries_.grossDepositsAmount)
                    );
            }
            if (criteria.getProfitBeforeTax() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getProfitBeforeTax(), PerformanceOfForeignSubsidiaries_.profitBeforeTax)
                    );
            }
            if (criteria.getTotalCapitalAdequacyRatio() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalCapitalAdequacyRatio(),
                            PerformanceOfForeignSubsidiaries_.totalCapitalAdequacyRatio
                        )
                    );
            }
            if (criteria.getLiquidityRatio() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getLiquidityRatio(), PerformanceOfForeignSubsidiaries_.liquidityRatio)
                    );
            }
            if (criteria.getGeneralProvisions() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getGeneralProvisions(), PerformanceOfForeignSubsidiaries_.generalProvisions)
                    );
            }
            if (criteria.getSpecificProvisions() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSpecificProvisions(), PerformanceOfForeignSubsidiaries_.specificProvisions)
                    );
            }
            if (criteria.getInterestInSuspenseAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getInterestInSuspenseAmount(),
                            PerformanceOfForeignSubsidiaries_.interestInSuspenseAmount
                        )
                    );
            }
            if (criteria.getTotalNumberOfStaff() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalNumberOfStaff(), PerformanceOfForeignSubsidiaries_.totalNumberOfStaff)
                    );
            }
            if (criteria.getNumberOfBranches() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfBranches(), PerformanceOfForeignSubsidiaries_.numberOfBranches)
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(PerformanceOfForeignSubsidiaries_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getSubsidiaryCountryCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubsidiaryCountryCodeId(),
                            root ->
                                root.join(PerformanceOfForeignSubsidiaries_.subsidiaryCountryCode, JoinType.LEFT).get(IsoCountryCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
