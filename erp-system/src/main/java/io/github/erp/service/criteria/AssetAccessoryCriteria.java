package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AssetAccessory} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetAccessoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-accessories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetAccessoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetTag;

    private StringFilter assetDetails;

    private StringFilter modelNumber;

    private StringFilter serialNumber;

    private LongFilter assetWarrantyId;

    private LongFilter placeholderId;

    private LongFilter paymentInvoicesId;

    private LongFilter serviceOutletId;

    private LongFilter settlementId;

    private LongFilter assetCategoryId;

    private LongFilter purchaseOrderId;

    private LongFilter deliveryNoteId;

    private LongFilter jobSheetId;

    private LongFilter dealerId;

    private LongFilter designatedUsersId;

    private LongFilter businessDocumentId;

    private LongFilter universallyUniqueMappingId;

    private LongFilter mainServiceOutletId;

    private Boolean distinct;

    public AssetAccessoryCriteria() {}

    public AssetAccessoryCriteria(AssetAccessoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.assetDetails = other.assetDetails == null ? null : other.assetDetails.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.assetWarrantyId = other.assetWarrantyId == null ? null : other.assetWarrantyId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentInvoicesId = other.paymentInvoicesId == null ? null : other.paymentInvoicesId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.settlementId = other.settlementId == null ? null : other.settlementId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.deliveryNoteId = other.deliveryNoteId == null ? null : other.deliveryNoteId.copy();
        this.jobSheetId = other.jobSheetId == null ? null : other.jobSheetId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.designatedUsersId = other.designatedUsersId == null ? null : other.designatedUsersId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.mainServiceOutletId = other.mainServiceOutletId == null ? null : other.mainServiceOutletId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetAccessoryCriteria copy() {
        return new AssetAccessoryCriteria(this);
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

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public LongFilter serviceOutletId() {
        if (serviceOutletId == null) {
            serviceOutletId = new LongFilter();
        }
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public LongFilter getSettlementId() {
        return settlementId;
    }

    public LongFilter settlementId() {
        if (settlementId == null) {
            settlementId = new LongFilter();
        }
        return settlementId;
    }

    public void setSettlementId(LongFilter settlementId) {
        this.settlementId = settlementId;
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
        final AssetAccessoryCriteria that = (AssetAccessoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(assetDetails, that.assetDetails) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(assetWarrantyId, that.assetWarrantyId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentInvoicesId, that.paymentInvoicesId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(settlementId, that.settlementId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(deliveryNoteId, that.deliveryNoteId) &&
            Objects.equals(jobSheetId, that.jobSheetId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(designatedUsersId, that.designatedUsersId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(mainServiceOutletId, that.mainServiceOutletId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetTag,
            assetDetails,
            modelNumber,
            serialNumber,
            assetWarrantyId,
            placeholderId,
            paymentInvoicesId,
            serviceOutletId,
            settlementId,
            assetCategoryId,
            purchaseOrderId,
            deliveryNoteId,
            jobSheetId,
            dealerId,
            designatedUsersId,
            businessDocumentId,
            universallyUniqueMappingId,
            mainServiceOutletId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAccessoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (assetDetails != null ? "assetDetails=" + assetDetails + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (assetWarrantyId != null ? "assetWarrantyId=" + assetWarrantyId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentInvoicesId != null ? "paymentInvoicesId=" + paymentInvoicesId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (settlementId != null ? "settlementId=" + settlementId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (deliveryNoteId != null ? "deliveryNoteId=" + deliveryNoteId + ", " : "") +
            (jobSheetId != null ? "jobSheetId=" + jobSheetId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (designatedUsersId != null ? "designatedUsersId=" + designatedUsersId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (mainServiceOutletId != null ? "mainServiceOutletId=" + mainServiceOutletId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
