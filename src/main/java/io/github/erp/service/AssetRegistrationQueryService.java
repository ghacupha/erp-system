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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.criteria.AssetRegistrationCriteria;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.mapper.AssetRegistrationMapper;
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
 * Service for executing complex queries for {@link AssetRegistration} entities in the database.
 * The main input is a {@link AssetRegistrationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetRegistrationDTO} or a {@link Page} of {@link AssetRegistrationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetRegistrationQueryService extends QueryService<AssetRegistration> {

    private final Logger log = LoggerFactory.getLogger(AssetRegistrationQueryService.class);

    private final AssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;

    public AssetRegistrationQueryService(
        AssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationMapper assetRegistrationMapper,
        AssetRegistrationSearchRepository assetRegistrationSearchRepository
    ) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetRegistrationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetRegistrationDTO> findByCriteria(AssetRegistrationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetRegistration> specification = createSpecification(criteria);
        return assetRegistrationMapper.toDto(assetRegistrationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetRegistrationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> findByCriteria(AssetRegistrationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetRegistration> specification = createSpecification(criteria);
        return assetRegistrationRepository.findAll(specification, page).map(assetRegistrationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetRegistrationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetRegistration> specification = createSpecification(criteria);
        return assetRegistrationRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetRegistrationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetRegistration> createSpecification(AssetRegistrationCriteria criteria) {
        Specification<AssetRegistration> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetRegistration_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetNumber(), AssetRegistration_.assetNumber));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), AssetRegistration_.assetTag));
            }
            if (criteria.getAssetDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetDetails(), AssetRegistration_.assetDetails));
            }
            if (criteria.getAssetCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetCost(), AssetRegistration_.assetCost));
            }
            if (criteria.getModelNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelNumber(), AssetRegistration_.modelNumber));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), AssetRegistration_.serialNumber));
            }
            if (criteria.getCapitalizationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCapitalizationDate(), AssetRegistration_.capitalizationDate));
            }
            if (criteria.getHistoricalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHistoricalCost(), AssetRegistration_.historicalCost));
            }
            if (criteria.getRegistrationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegistrationDate(), AssetRegistration_.registrationDate));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetRegistration_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPaymentInvoicesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentInvoicesId(),
                            root -> root.join(AssetRegistration_.paymentInvoices, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getOtherRelatedServiceOutletsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOtherRelatedServiceOutletsId(),
                            root -> root.join(AssetRegistration_.otherRelatedServiceOutlets, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getOtherRelatedSettlementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOtherRelatedSettlementsId(),
                            root -> root.join(AssetRegistration_.otherRelatedSettlements, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(AssetRegistration_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(AssetRegistration_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getDeliveryNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryNoteId(),
                            root -> root.join(AssetRegistration_.deliveryNotes, JoinType.LEFT).get(DeliveryNote_.id)
                        )
                    );
            }
            if (criteria.getJobSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobSheetId(),
                            root -> root.join(AssetRegistration_.jobSheets, JoinType.LEFT).get(JobSheet_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealerId(),
                            root -> root.join(AssetRegistration_.dealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDesignatedUsersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDesignatedUsersId(),
                            root -> root.join(AssetRegistration_.designatedUsers, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(AssetRegistration_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(AssetRegistration_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getAssetWarrantyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetWarrantyId(),
                            root -> root.join(AssetRegistration_.assetWarranties, JoinType.LEFT).get(AssetWarranty_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(AssetRegistration_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getAssetAccessoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetAccessoryId(),
                            root -> root.join(AssetRegistration_.assetAccessories, JoinType.LEFT).get(AssetAccessory_.id)
                        )
                    );
            }
            if (criteria.getMainServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMainServiceOutletId(),
                            root -> root.join(AssetRegistration_.mainServiceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getAcquiringTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAcquiringTransactionId(),
                            root -> root.join(AssetRegistration_.acquiringTransaction, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
