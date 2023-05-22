package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.AssetAccessory;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessorySearchRepository;
import io.github.erp.service.criteria.AssetAccessoryCriteria;
import io.github.erp.service.dto.AssetAccessoryDTO;
import io.github.erp.service.mapper.AssetAccessoryMapper;
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
 * Service for executing complex queries for {@link AssetAccessory} entities in the database.
 * The main input is a {@link AssetAccessoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetAccessoryDTO} or a {@link Page} of {@link AssetAccessoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetAccessoryQueryService extends QueryService<AssetAccessory> {

    private final Logger log = LoggerFactory.getLogger(AssetAccessoryQueryService.class);

    private final AssetAccessoryRepository assetAccessoryRepository;

    private final AssetAccessoryMapper assetAccessoryMapper;

    private final AssetAccessorySearchRepository assetAccessorySearchRepository;

    public AssetAccessoryQueryService(
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryMapper assetAccessoryMapper,
        AssetAccessorySearchRepository assetAccessorySearchRepository
    ) {
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryMapper = assetAccessoryMapper;
        this.assetAccessorySearchRepository = assetAccessorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetAccessoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetAccessoryDTO> findByCriteria(AssetAccessoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetAccessory> specification = createSpecification(criteria);
        return assetAccessoryMapper.toDto(assetAccessoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetAccessoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> findByCriteria(AssetAccessoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetAccessory> specification = createSpecification(criteria);
        return assetAccessoryRepository.findAll(specification, page).map(assetAccessoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetAccessoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetAccessory> specification = createSpecification(criteria);
        return assetAccessoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetAccessoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetAccessory> createSpecification(AssetAccessoryCriteria criteria) {
        Specification<AssetAccessory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetAccessory_.id));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), AssetAccessory_.assetTag));
            }
            if (criteria.getAssetDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetDetails(), AssetAccessory_.assetDetails));
            }
            if (criteria.getModelNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelNumber(), AssetAccessory_.modelNumber));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), AssetAccessory_.serialNumber));
            }
            if (criteria.getAssetRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetRegistrationId(),
                            root -> root.join(AssetAccessory_.assetRegistration, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetAccessory_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPaymentInvoicesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentInvoicesId(),
                            root -> root.join(AssetAccessory_.paymentInvoices, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(AssetAccessory_.serviceOutlets, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getSettlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementId(),
                            root -> root.join(AssetAccessory_.settlements, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(AssetAccessory_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(AssetAccessory_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getDeliveryNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryNoteId(),
                            root -> root.join(AssetAccessory_.deliveryNotes, JoinType.LEFT).get(DeliveryNote_.id)
                        )
                    );
            }
            if (criteria.getJobSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobSheetId(),
                            root -> root.join(AssetAccessory_.jobSheets, JoinType.LEFT).get(JobSheet_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDealerId(), root -> root.join(AssetAccessory_.dealer, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getDesignatedUsersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDesignatedUsersId(),
                            root -> root.join(AssetAccessory_.designatedUsers, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(AssetAccessory_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(AssetAccessory_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(AssetAccessory_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
