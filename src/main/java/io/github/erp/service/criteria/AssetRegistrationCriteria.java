package io.github.erp.service.criteria;

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

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AssetRegistration} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetRegistrationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-registrations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetRegistrationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetNumber;

    private StringFilter assetTag;

    private StringFilter assetDetails;

    private BigDecimalFilter assetCost;

    private StringFilter modelNumber;

    private StringFilter serialNumber;

    private LocalDateFilter capitalizationDate;

    private BigDecimalFilter historicalCost;

    private LocalDateFilter registrationDate;

    private LongFilter placeholderId;

    private LongFilter paymentInvoicesId;

    private LongFilter otherRelatedServiceOutletsId;

    private LongFilter otherRelatedSettlementsId;

    private LongFilter assetCategoryId;

    private LongFilter purchaseOrderId;

    private LongFilter deliveryNoteId;

    private LongFilter jobSheetId;

    private LongFilter dealerId;

    private LongFilter designatedUsersId;

    private LongFilter settlementCurrencyId;

    private LongFilter businessDocumentId;

    private LongFilter assetWarrantyId;

    private LongFilter universallyUniqueMappingId;

    private LongFilter assetAccessoryId;

    private LongFilter mainServiceOutletId;

    private LongFilter acquiringTransactionId;

    private Boolean distinct;

    public AssetRegistrationCriteria() {}

    public AssetRegistrationCriteria(AssetRegistrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.assetDetails = other.assetDetails == null ? null : other.assetDetails.copy();
        this.assetCost = other.assetCost == null ? null : other.assetCost.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.capitalizationDate = other.capitalizationDate == null ? null : other.capitalizationDate.copy();
        this.historicalCost = other.historicalCost == null ? null : other.historicalCost.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentInvoicesId = other.paymentInvoicesId == null ? null : other.paymentInvoicesId.copy();
        this.otherRelatedServiceOutletsId = other.otherRelatedServiceOutletsId == null ? null : other.otherRelatedServiceOutletsId.copy();
        this.otherRelatedSettlementsId = other.otherRelatedSettlementsId == null ? null : other.otherRelatedSettlementsId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.deliveryNoteId = other.deliveryNoteId == null ? null : other.deliveryNoteId.copy();
        this.jobSheetId = other.jobSheetId == null ? null : other.jobSheetId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.designatedUsersId = other.designatedUsersId == null ? null : other.designatedUsersId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.assetWarrantyId = other.assetWarrantyId == null ? null : other.assetWarrantyId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.assetAccessoryId = other.assetAccessoryId == null ? null : other.assetAccessoryId.copy();
        this.mainServiceOutletId = other.mainServiceOutletId == null ? null : other.mainServiceOutletId.copy();
        this.acquiringTransactionId = other.acquiringTransactionId == null ? null : other.acquiringTransactionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetRegistrationCriteria copy() {
        return new AssetRegistrationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAssetNumber() {
        return assetNumber;
    }

    public StringFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new StringFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(StringFilter assetNumber) {
        this.assetNumber = assetNumber;
    }

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public StringFilter assetTag() {
        if (assetTag == null) {
            assetTag = new StringFilter();
        }
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
    }

    public StringFilter getAssetDetails() {
        return assetDetails;
    }

    public StringFilter assetDetails() {
        if (assetDetails == null) {
            assetDetails = new StringFilter();
        }
        return assetDetails;
    }

    public void setAssetDetails(StringFilter assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimalFilter getAssetCost() {
        return assetCost;
    }

    public BigDecimalFilter assetCost() {
        if (assetCost == null) {
            assetCost = new BigDecimalFilter();
        }
        return assetCost;
    }

    public void setAssetCost(BigDecimalFilter assetCost) {
        this.assetCost = assetCost;
    }

    public StringFilter getModelNumber() {
        return modelNumber;
    }

    public StringFilter modelNumber() {
        if (modelNumber == null) {
            modelNumber = new StringFilter();
        }
        return modelNumber;
    }

    public void setModelNumber(StringFilter modelNumber) {
        this.modelNumber = modelNumber;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateFilter getCapitalizationDate() {
        return capitalizationDate;
    }

    public LocalDateFilter capitalizationDate() {
        if (capitalizationDate == null) {
            capitalizationDate = new LocalDateFilter();
        }
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDateFilter capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public BigDecimalFilter getHistoricalCost() {
        return historicalCost;
    }

    public BigDecimalFilter historicalCost() {
        if (historicalCost == null) {
            historicalCost = new BigDecimalFilter();
        }
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimalFilter historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new LocalDateFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getPaymentInvoicesId() {
        return paymentInvoicesId;
    }

    public LongFilter paymentInvoicesId() {
        if (paymentInvoicesId == null) {
            paymentInvoicesId = new LongFilter();
        }
        return paymentInvoicesId;
    }

    public void setPaymentInvoicesId(LongFilter paymentInvoicesId) {
        this.paymentInvoicesId = paymentInvoicesId;
    }

    public LongFilter getOtherRelatedServiceOutletsId() {
        return otherRelatedServiceOutletsId;
    }

    public LongFilter otherRelatedServiceOutletsId() {
        if (otherRelatedServiceOutletsId == null) {
            otherRelatedServiceOutletsId = new LongFilter();
        }
        return otherRelatedServiceOutletsId;
    }

    public void setOtherRelatedServiceOutletsId(LongFilter otherRelatedServiceOutletsId) {
        this.otherRelatedServiceOutletsId = otherRelatedServiceOutletsId;
    }

    public LongFilter getOtherRelatedSettlementsId() {
        return otherRelatedSettlementsId;
    }

    public LongFilter otherRelatedSettlementsId() {
        if (otherRelatedSettlementsId == null) {
            otherRelatedSettlementsId = new LongFilter();
        }
        return otherRelatedSettlementsId;
    }

    public void setOtherRelatedSettlementsId(LongFilter otherRelatedSettlementsId) {
        this.otherRelatedSettlementsId = otherRelatedSettlementsId;
    }

    public LongFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public LongFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new LongFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(LongFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public LongFilter getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public LongFilter purchaseOrderId() {
        if (purchaseOrderId == null) {
            purchaseOrderId = new LongFilter();
        }
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(LongFilter purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public LongFilter getDeliveryNoteId() {
        return deliveryNoteId;
    }

    public LongFilter deliveryNoteId() {
        if (deliveryNoteId == null) {
            deliveryNoteId = new LongFilter();
        }
        return deliveryNoteId;
    }

    public void setDeliveryNoteId(LongFilter deliveryNoteId) {
        this.deliveryNoteId = deliveryNoteId;
    }

    public LongFilter getJobSheetId() {
        return jobSheetId;
    }

    public LongFilter jobSheetId() {
        if (jobSheetId == null) {
            jobSheetId = new LongFilter();
        }
        return jobSheetId;
    }

    public void setJobSheetId(LongFilter jobSheetId) {
        this.jobSheetId = jobSheetId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            dealerId = new LongFilter();
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
    }

    public LongFilter getDesignatedUsersId() {
        return designatedUsersId;
    }

    public LongFilter designatedUsersId() {
        if (designatedUsersId == null) {
            designatedUsersId = new LongFilter();
        }
        return designatedUsersId;
    }

    public void setDesignatedUsersId(LongFilter designatedUsersId) {
        this.designatedUsersId = designatedUsersId;
    }

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
    }

    public LongFilter getBusinessDocumentId() {
        return businessDocumentId;
    }

    public LongFilter businessDocumentId() {
        if (businessDocumentId == null) {
            businessDocumentId = new LongFilter();
        }
        return businessDocumentId;
    }

    public void setBusinessDocumentId(LongFilter businessDocumentId) {
        this.businessDocumentId = businessDocumentId;
    }

    public LongFilter getAssetWarrantyId() {
        return assetWarrantyId;
    }

    public LongFilter assetWarrantyId() {
        if (assetWarrantyId == null) {
            assetWarrantyId = new LongFilter();
        }
        return assetWarrantyId;
    }

    public void setAssetWarrantyId(LongFilter assetWarrantyId) {
        this.assetWarrantyId = assetWarrantyId;
    }

    public LongFilter getUniversallyUniqueMappingId() {
        return universallyUniqueMappingId;
    }

    public LongFilter universallyUniqueMappingId() {
        if (universallyUniqueMappingId == null) {
            universallyUniqueMappingId = new LongFilter();
        }
        return universallyUniqueMappingId;
    }

    public void setUniversallyUniqueMappingId(LongFilter universallyUniqueMappingId) {
        this.universallyUniqueMappingId = universallyUniqueMappingId;
    }

    public LongFilter getAssetAccessoryId() {
        return assetAccessoryId;
    }

    public LongFilter assetAccessoryId() {
        if (assetAccessoryId == null) {
            assetAccessoryId = new LongFilter();
        }
        return assetAccessoryId;
    }

    public void setAssetAccessoryId(LongFilter assetAccessoryId) {
        this.assetAccessoryId = assetAccessoryId;
    }

    public LongFilter getMainServiceOutletId() {
        return mainServiceOutletId;
    }

    public LongFilter mainServiceOutletId() {
        if (mainServiceOutletId == null) {
            mainServiceOutletId = new LongFilter();
        }
        return mainServiceOutletId;
    }

    public void setMainServiceOutletId(LongFilter mainServiceOutletId) {
        this.mainServiceOutletId = mainServiceOutletId;
    }

    public LongFilter getAcquiringTransactionId() {
        return acquiringTransactionId;
    }

    public LongFilter acquiringTransactionId() {
        if (acquiringTransactionId == null) {
            acquiringTransactionId = new LongFilter();
        }
        return acquiringTransactionId;
    }

    public void setAcquiringTransactionId(LongFilter acquiringTransactionId) {
        this.acquiringTransactionId = acquiringTransactionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetRegistrationCriteria that = (AssetRegistrationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(assetDetails, that.assetDetails) &&
            Objects.equals(assetCost, that.assetCost) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(capitalizationDate, that.capitalizationDate) &&
            Objects.equals(historicalCost, that.historicalCost) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentInvoicesId, that.paymentInvoicesId) &&
            Objects.equals(otherRelatedServiceOutletsId, that.otherRelatedServiceOutletsId) &&
            Objects.equals(otherRelatedSettlementsId, that.otherRelatedSettlementsId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(deliveryNoteId, that.deliveryNoteId) &&
            Objects.equals(jobSheetId, that.jobSheetId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(designatedUsersId, that.designatedUsersId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(assetWarrantyId, that.assetWarrantyId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(assetAccessoryId, that.assetAccessoryId) &&
            Objects.equals(mainServiceOutletId, that.mainServiceOutletId) &&
            Objects.equals(acquiringTransactionId, that.acquiringTransactionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetNumber,
            assetTag,
            assetDetails,
            assetCost,
            modelNumber,
            serialNumber,
            capitalizationDate,
            historicalCost,
            registrationDate,
            placeholderId,
            paymentInvoicesId,
            otherRelatedServiceOutletsId,
            otherRelatedSettlementsId,
            assetCategoryId,
            purchaseOrderId,
            deliveryNoteId,
            jobSheetId,
            dealerId,
            designatedUsersId,
            settlementCurrencyId,
            businessDocumentId,
            assetWarrantyId,
            universallyUniqueMappingId,
            assetAccessoryId,
            mainServiceOutletId,
            acquiringTransactionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegistrationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (assetDetails != null ? "assetDetails=" + assetDetails + ", " : "") +
            (assetCost != null ? "assetCost=" + assetCost + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (capitalizationDate != null ? "capitalizationDate=" + capitalizationDate + ", " : "") +
            (historicalCost != null ? "historicalCost=" + historicalCost + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentInvoicesId != null ? "paymentInvoicesId=" + paymentInvoicesId + ", " : "") +
            (otherRelatedServiceOutletsId != null ? "otherRelatedServiceOutletsId=" + otherRelatedServiceOutletsId + ", " : "") +
            (otherRelatedSettlementsId != null ? "otherRelatedSettlementsId=" + otherRelatedSettlementsId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (deliveryNoteId != null ? "deliveryNoteId=" + deliveryNoteId + ", " : "") +
            (jobSheetId != null ? "jobSheetId=" + jobSheetId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (designatedUsersId != null ? "designatedUsersId=" + designatedUsersId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (assetWarrantyId != null ? "assetWarrantyId=" + assetWarrantyId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (assetAccessoryId != null ? "assetAccessoryId=" + assetAccessoryId + ", " : "") +
            (mainServiceOutletId != null ? "mainServiceOutletId=" + mainServiceOutletId + ", " : "") +
            (acquiringTransactionId != null ? "acquiringTransactionId=" + acquiringTransactionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
