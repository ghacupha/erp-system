package io.github.erp.service;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(AssetRegistration_.serviceOutlets, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getSettlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementId(),
                            root -> root.join(AssetRegistration_.settlements, JoinType.LEFT).get(Settlement_.id)
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
        }
        return specification;
    }
}
