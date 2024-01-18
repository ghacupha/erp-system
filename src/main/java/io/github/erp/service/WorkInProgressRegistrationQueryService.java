package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.repository.WorkInProgressRegistrationRepository;
import io.github.erp.repository.search.WorkInProgressRegistrationSearchRepository;
import io.github.erp.service.criteria.WorkInProgressRegistrationCriteria;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import io.github.erp.service.mapper.WorkInProgressRegistrationMapper;
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
 * Service for executing complex queries for {@link WorkInProgressRegistration} entities in the database.
 * The main input is a {@link WorkInProgressRegistrationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkInProgressRegistrationDTO} or a {@link Page} of {@link WorkInProgressRegistrationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkInProgressRegistrationQueryService extends QueryService<WorkInProgressRegistration> {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressRegistrationQueryService.class);

    private final WorkInProgressRegistrationRepository workInProgressRegistrationRepository;

    private final WorkInProgressRegistrationMapper workInProgressRegistrationMapper;

    private final WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository;

    public WorkInProgressRegistrationQueryService(
        WorkInProgressRegistrationRepository workInProgressRegistrationRepository,
        WorkInProgressRegistrationMapper workInProgressRegistrationMapper,
        WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository
    ) {
        this.workInProgressRegistrationRepository = workInProgressRegistrationRepository;
        this.workInProgressRegistrationMapper = workInProgressRegistrationMapper;
        this.workInProgressRegistrationSearchRepository = workInProgressRegistrationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WorkInProgressRegistrationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkInProgressRegistrationDTO> findByCriteria(WorkInProgressRegistrationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkInProgressRegistration> specification = createSpecification(criteria);
        return workInProgressRegistrationMapper.toDto(workInProgressRegistrationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkInProgressRegistrationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkInProgressRegistrationDTO> findByCriteria(WorkInProgressRegistrationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkInProgressRegistration> specification = createSpecification(criteria);
        return workInProgressRegistrationRepository.findAll(specification, page).map(workInProgressRegistrationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkInProgressRegistrationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkInProgressRegistration> specification = createSpecification(criteria);
        return workInProgressRegistrationRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkInProgressRegistrationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkInProgressRegistration> createSpecification(WorkInProgressRegistrationCriteria criteria) {
        Specification<WorkInProgressRegistration> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkInProgressRegistration_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSequenceNumber(), WorkInProgressRegistration_.sequenceNumber));
            }
            if (criteria.getParticulars() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getParticulars(), WorkInProgressRegistration_.particulars));
            }
            if (criteria.getInstalmentAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInstalmentAmount(), WorkInProgressRegistration_.instalmentAmount)
                    );
            }
            if (criteria.getLevelOfCompletion() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getLevelOfCompletion(), WorkInProgressRegistration_.levelOfCompletion)
                    );
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getCompleted(), WorkInProgressRegistration_.completed));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(WorkInProgressRegistration_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getWorkInProgressGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkInProgressGroupId(),
                            root ->
                                root
                                    .join(WorkInProgressRegistration_.workInProgressGroup, JoinType.LEFT)
                                    .get(WorkInProgressRegistration_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(WorkInProgressRegistration_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getWorkProjectRegisterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkProjectRegisterId(),
                            root -> root.join(WorkInProgressRegistration_.workProjectRegister, JoinType.LEFT).get(WorkProjectRegister_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(WorkInProgressRegistration_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getAssetAccessoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetAccessoryId(),
                            root -> root.join(WorkInProgressRegistration_.assetAccessories, JoinType.LEFT).get(AssetAccessory_.id)
                        )
                    );
            }
            if (criteria.getAssetWarrantyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetWarrantyId(),
                            root -> root.join(WorkInProgressRegistration_.assetWarranties, JoinType.LEFT).get(AssetWarranty_.id)
                        )
                    );
            }
            if (criteria.getInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInvoiceId(),
                            root -> root.join(WorkInProgressRegistration_.invoice, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getOutletCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutletCodeId(),
                            root -> root.join(WorkInProgressRegistration_.outletCode, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getSettlementTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementTransactionId(),
                            root -> root.join(WorkInProgressRegistration_.settlementTransaction, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(WorkInProgressRegistration_.purchaseOrder, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getDeliveryNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryNoteId(),
                            root -> root.join(WorkInProgressRegistration_.deliveryNote, JoinType.LEFT).get(DeliveryNote_.id)
                        )
                    );
            }
            if (criteria.getJobSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobSheetId(),
                            root -> root.join(WorkInProgressRegistration_.jobSheet, JoinType.LEFT).get(JobSheet_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealerId(),
                            root -> root.join(WorkInProgressRegistration_.dealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
