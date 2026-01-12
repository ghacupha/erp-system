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
import io.github.erp.domain.LeaseSettlement;
import io.github.erp.repository.LeaseSettlementRepository;
import io.github.erp.service.criteria.LeaseSettlementCriteria;
import io.github.erp.service.dto.LeaseSettlementDTO;
import io.github.erp.service.mapper.LeaseSettlementMapper;
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
 * Service for executing complex queries for {@link LeaseSettlement} entities in the database.
 * The main input is a {@link LeaseSettlementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseSettlementDTO} or a {@link Page} of {@link LeaseSettlementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseSettlementQueryService extends QueryService<LeaseSettlement> {

    private final Logger log = LoggerFactory.getLogger(LeaseSettlementQueryService.class);

    private final LeaseSettlementRepository leaseSettlementRepository;

    private final LeaseSettlementMapper leaseSettlementMapper;

    public LeaseSettlementQueryService(
        LeaseSettlementRepository leaseSettlementRepository,
        LeaseSettlementMapper leaseSettlementMapper
    ) {
        this.leaseSettlementRepository = leaseSettlementRepository;
        this.leaseSettlementMapper = leaseSettlementMapper;
    }

    /**
     * Return a {@link List} of {@link LeaseSettlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseSettlementDTO> findByCriteria(LeaseSettlementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseSettlement> specification = createSpecification(criteria);
        return leaseSettlementMapper.toDto(leaseSettlementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseSettlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseSettlementDTO> findByCriteria(LeaseSettlementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseSettlement> specification = createSpecification(criteria);
        return leaseSettlementRepository.findAll(specification, page).map(leaseSettlementMapper::toDto);
    }

    /**
     * Return the count of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the count of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseSettlementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseSettlement> specification = createSpecification(criteria);
        return leaseSettlementRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseSettlementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseSettlement> createSpecification(LeaseSettlementCriteria criteria) {
        Specification<LeaseSettlement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseSettlement_.id));
            }
            if (criteria.getSettlementDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSettlementDate(), LeaseSettlement_.settlementDate));
            }
            if (criteria.getInvoiceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvoiceAmount(), LeaseSettlement_.invoiceAmount));
            }
            if (criteria.getInvoiceReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceReference(), LeaseSettlement_.invoiceReference));
            }
            if (criteria.getVarianceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVarianceAmount(), LeaseSettlement_.varianceAmount));
            }
            if (criteria.getVarianceReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVarianceReason(), LeaseSettlement_.varianceReason));
            }
            if (criteria.getPostingId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostingId(), LeaseSettlement_.postingId));
            }
            if (criteria.getReconciliationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReconciliationStatus(), LeaseSettlement_.reconciliationStatus));
            }
            if (criteria.getLeaseContractId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getLeaseContractId(),
                        root -> root.join(LeaseSettlement_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                    )
                );
            }
            if (criteria.getPeriodId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getPeriodId(),
                        root -> root.join(LeaseSettlement_.period, JoinType.LEFT).get(LeaseRepaymentPeriod_.id)
                    )
                );
            }
            if (criteria.getLeasePaymentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getLeasePaymentId(),
                        root -> root.join(LeaseSettlement_.leasePayment, JoinType.LEFT).get(LeasePayment_.id)
                    )
                );
            }
        }
        return specification;
    }
}
