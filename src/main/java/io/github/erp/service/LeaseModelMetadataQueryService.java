package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.LeaseModelMetadata;
import io.github.erp.repository.LeaseModelMetadataRepository;
import io.github.erp.repository.search.LeaseModelMetadataSearchRepository;
import io.github.erp.service.criteria.LeaseModelMetadataCriteria;
import io.github.erp.service.dto.LeaseModelMetadataDTO;
import io.github.erp.service.mapper.LeaseModelMetadataMapper;
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
 * Service for executing complex queries for {@link LeaseModelMetadata} entities in the database.
 * The main input is a {@link LeaseModelMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseModelMetadataDTO} or a {@link Page} of {@link LeaseModelMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseModelMetadataQueryService extends QueryService<LeaseModelMetadata> {

    private final Logger log = LoggerFactory.getLogger(LeaseModelMetadataQueryService.class);

    private final LeaseModelMetadataRepository leaseModelMetadataRepository;

    private final LeaseModelMetadataMapper leaseModelMetadataMapper;

    private final LeaseModelMetadataSearchRepository leaseModelMetadataSearchRepository;

    public LeaseModelMetadataQueryService(
        LeaseModelMetadataRepository leaseModelMetadataRepository,
        LeaseModelMetadataMapper leaseModelMetadataMapper,
        LeaseModelMetadataSearchRepository leaseModelMetadataSearchRepository
    ) {
        this.leaseModelMetadataRepository = leaseModelMetadataRepository;
        this.leaseModelMetadataMapper = leaseModelMetadataMapper;
        this.leaseModelMetadataSearchRepository = leaseModelMetadataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseModelMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseModelMetadataDTO> findByCriteria(LeaseModelMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseModelMetadata> specification = createSpecification(criteria);
        return leaseModelMetadataMapper.toDto(leaseModelMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseModelMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseModelMetadataDTO> findByCriteria(LeaseModelMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseModelMetadata> specification = createSpecification(criteria);
        return leaseModelMetadataRepository.findAll(specification, page).map(leaseModelMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseModelMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseModelMetadata> specification = createSpecification(criteria);
        return leaseModelMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseModelMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseModelMetadata> createSpecification(LeaseModelMetadataCriteria criteria) {
        Specification<LeaseModelMetadata> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseModelMetadata_.id));
            }
            if (criteria.getModelTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelTitle(), LeaseModelMetadata_.modelTitle));
            }
            if (criteria.getModelVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModelVersion(), LeaseModelMetadata_.modelVersion));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeaseModelMetadata_.description));
            }
            if (criteria.getAnnualDiscountingRate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAnnualDiscountingRate(), LeaseModelMetadata_.annualDiscountingRate)
                    );
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), LeaseModelMetadata_.commencementDate));
            }
            if (criteria.getTerminalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTerminalDate(), LeaseModelMetadata_.terminalDate));
            }
            if (criteria.getTotalReportingPeriods() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalReportingPeriods(), LeaseModelMetadata_.totalReportingPeriods)
                    );
            }
            if (criteria.getReportingPeriodsPerYear() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getReportingPeriodsPerYear(), LeaseModelMetadata_.reportingPeriodsPerYear)
                    );
            }
            if (criteria.getSettlementPeriodsPerYear() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSettlementPeriodsPerYear(), LeaseModelMetadata_.settlementPeriodsPerYear)
                    );
            }
            if (criteria.getInitialLiabilityAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInitialLiabilityAmount(), LeaseModelMetadata_.initialLiabilityAmount)
                    );
            }
            if (criteria.getInitialROUAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getInitialROUAmount(), LeaseModelMetadata_.initialROUAmount));
            }
            if (criteria.getTotalDepreciationPeriods() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalDepreciationPeriods(), LeaseModelMetadata_.totalDepreciationPeriods)
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(LeaseModelMetadata_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getLeaseMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseMappingId(),
                            root -> root.join(LeaseModelMetadata_.leaseMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseModelMetadata_.leaseContract, JoinType.LEFT).get(LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getPredecessorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPredecessorId(),
                            root -> root.join(LeaseModelMetadata_.predecessor, JoinType.LEFT).get(LeaseModelMetadata_.id)
                        )
                    );
            }
            if (criteria.getLiabilityCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLiabilityCurrencyId(),
                            root -> root.join(LeaseModelMetadata_.liabilityCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getRouAssetCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouAssetCurrencyId(),
                            root -> root.join(LeaseModelMetadata_.rouAssetCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getModelAttachmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getModelAttachmentsId(),
                            root -> root.join(LeaseModelMetadata_.modelAttachments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(LeaseModelMetadata_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getLeaseLiabilityAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseLiabilityAccountId(),
                            root -> root.join(LeaseModelMetadata_.leaseLiabilityAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestPayableAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestPayableAccountId(),
                            root -> root.join(LeaseModelMetadata_.interestPayableAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestExpenseAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestExpenseAccountId(),
                            root -> root.join(LeaseModelMetadata_.interestExpenseAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getRouAssetAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouAssetAccountId(),
                            root -> root.join(LeaseModelMetadata_.rouAssetAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getRouDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouDepreciationAccountId(),
                            root -> root.join(LeaseModelMetadata_.rouDepreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAccruedDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAccruedDepreciationAccountId(),
                            root -> root.join(LeaseModelMetadata_.accruedDepreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
