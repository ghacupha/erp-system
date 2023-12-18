package io.github.erp.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.CollateralInformation;
import io.github.erp.repository.CollateralInformationRepository;
import io.github.erp.repository.search.CollateralInformationSearchRepository;
import io.github.erp.service.criteria.CollateralInformationCriteria;
import io.github.erp.service.dto.CollateralInformationDTO;
import io.github.erp.service.mapper.CollateralInformationMapper;
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
 * Service for executing complex queries for {@link CollateralInformation} entities in the database.
 * The main input is a {@link CollateralInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollateralInformationDTO} or a {@link Page} of {@link CollateralInformationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollateralInformationQueryService extends QueryService<CollateralInformation> {

    private final Logger log = LoggerFactory.getLogger(CollateralInformationQueryService.class);

    private final CollateralInformationRepository collateralInformationRepository;

    private final CollateralInformationMapper collateralInformationMapper;

    private final CollateralInformationSearchRepository collateralInformationSearchRepository;

    public CollateralInformationQueryService(
        CollateralInformationRepository collateralInformationRepository,
        CollateralInformationMapper collateralInformationMapper,
        CollateralInformationSearchRepository collateralInformationSearchRepository
    ) {
        this.collateralInformationRepository = collateralInformationRepository;
        this.collateralInformationMapper = collateralInformationMapper;
        this.collateralInformationSearchRepository = collateralInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CollateralInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollateralInformationDTO> findByCriteria(CollateralInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CollateralInformation> specification = createSpecification(criteria);
        return collateralInformationMapper.toDto(collateralInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollateralInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollateralInformationDTO> findByCriteria(CollateralInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CollateralInformation> specification = createSpecification(criteria);
        return collateralInformationRepository.findAll(specification, page).map(collateralInformationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollateralInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CollateralInformation> specification = createSpecification(criteria);
        return collateralInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link CollateralInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CollateralInformation> createSpecification(CollateralInformationCriteria criteria) {
        Specification<CollateralInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CollateralInformation_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), CollateralInformation_.reportingDate));
            }
            if (criteria.getCollateralId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCollateralId(), CollateralInformation_.collateralId));
            }
            if (criteria.getLoanContractId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLoanContractId(), CollateralInformation_.loanContractId));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerId(), CollateralInformation_.customerId));
            }
            if (criteria.getRegistrationPropertyNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getRegistrationPropertyNumber(),
                            CollateralInformation_.registrationPropertyNumber
                        )
                    );
            }
            if (criteria.getCollateralOMVInCCY() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCollateralOMVInCCY(), CollateralInformation_.collateralOMVInCCY));
            }
            if (criteria.getCollateralFSVInLCY() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCollateralFSVInLCY(), CollateralInformation_.collateralFSVInLCY));
            }
            if (criteria.getCollateralDiscountedValue() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCollateralDiscountedValue(), CollateralInformation_.collateralDiscountedValue)
                    );
            }
            if (criteria.getAmountCharged() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmountCharged(), CollateralInformation_.amountCharged));
            }
            if (criteria.getCollateralDiscountRate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCollateralDiscountRate(), CollateralInformation_.collateralDiscountRate)
                    );
            }
            if (criteria.getLoanToValueRatio() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLoanToValueRatio(), CollateralInformation_.loanToValueRatio));
            }
            if (criteria.getNameOfPropertyValuer() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getNameOfPropertyValuer(), CollateralInformation_.nameOfPropertyValuer)
                    );
            }
            if (criteria.getCollateralLastValuationDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getCollateralLastValuationDate(),
                            CollateralInformation_.collateralLastValuationDate
                        )
                    );
            }
            if (criteria.getInsuredFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getInsuredFlag(), CollateralInformation_.insuredFlag));
            }
            if (criteria.getNameOfInsurer() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNameOfInsurer(), CollateralInformation_.nameOfInsurer));
            }
            if (criteria.getAmountInsured() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmountInsured(), CollateralInformation_.amountInsured));
            }
            if (criteria.getInsuranceExpiryDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInsuranceExpiryDate(), CollateralInformation_.insuranceExpiryDate)
                    );
            }
            if (criteria.getGuaranteeInsurers() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getGuaranteeInsurers(), CollateralInformation_.guaranteeInsurers));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(CollateralInformation_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchCodeId(),
                            root -> root.join(CollateralInformation_.branchCode, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getCollateralTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCollateralTypeId(),
                            root -> root.join(CollateralInformation_.collateralType, JoinType.LEFT).get(CollateralType_.id)
                        )
                    );
            }
            if (criteria.getCountyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyCodeId(),
                            root -> root.join(CollateralInformation_.countyCode, JoinType.LEFT).get(CountySubCountyCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
