package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.WorkInProgressRegistration} entity. This class is used
 * in {@link io.github.erp.web.rest.WorkInProgressRegistrationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /work-in-progress-registrations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkInProgressRegistrationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sequenceNumber;

    private StringFilter particulars;

    private BigDecimalFilter instalmentAmount;

    private DoubleFilter levelOfCompletion;

    private BooleanFilter completed;

    private LongFilter placeholderId;

    private LongFilter workInProgressGroupId;

    private LongFilter settlementCurrencyId;

    private LongFilter workProjectRegisterId;

    private LongFilter businessDocumentId;

    private LongFilter assetAccessoryId;

    private LongFilter assetWarrantyId;

    private LongFilter invoiceId;

    private LongFilter outletCodeId;

    private LongFilter settlementTransactionId;

    private LongFilter purchaseOrderId;

    private LongFilter deliveryNoteId;

    private LongFilter jobSheetId;

    private LongFilter dealerId;

    private Boolean distinct;

    public WorkInProgressRegistrationCriteria() {}

    public WorkInProgressRegistrationCriteria(WorkInProgressRegistrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.instalmentAmount = other.instalmentAmount == null ? null : other.instalmentAmount.copy();
        this.levelOfCompletion = other.levelOfCompletion == null ? null : other.levelOfCompletion.copy();
        this.completed = other.completed == null ? null : other.completed.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.workInProgressGroupId = other.workInProgressGroupId == null ? null : other.workInProgressGroupId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.workProjectRegisterId = other.workProjectRegisterId == null ? null : other.workProjectRegisterId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.assetAccessoryId = other.assetAccessoryId == null ? null : other.assetAccessoryId.copy();
        this.assetWarrantyId = other.assetWarrantyId == null ? null : other.assetWarrantyId.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
        this.outletCodeId = other.outletCodeId == null ? null : other.outletCodeId.copy();
        this.settlementTransactionId = other.settlementTransactionId == null ? null : other.settlementTransactionId.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.deliveryNoteId = other.deliveryNoteId == null ? null : other.deliveryNoteId.copy();
        this.jobSheetId = other.jobSheetId == null ? null : other.jobSheetId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkInProgressRegistrationCriteria copy() {
        return new WorkInProgressRegistrationCriteria(this);
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

    public StringFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public StringFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new StringFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(StringFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public StringFilter getParticulars() {
        return particulars;
    }

    public StringFilter particulars() {
        if (particulars == null) {
            particulars = new StringFilter();
        }
        return particulars;
    }

    public void setParticulars(StringFilter particulars) {
        this.particulars = particulars;
    }

    public BigDecimalFilter getInstalmentAmount() {
        return instalmentAmount;
    }

    public BigDecimalFilter instalmentAmount() {
        if (instalmentAmount == null) {
            instalmentAmount = new BigDecimalFilter();
        }
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimalFilter instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public DoubleFilter getLevelOfCompletion() {
        return levelOfCompletion;
    }

    public DoubleFilter levelOfCompletion() {
        if (levelOfCompletion == null) {
            levelOfCompletion = new DoubleFilter();
        }
        return levelOfCompletion;
    }

    public void setLevelOfCompletion(DoubleFilter levelOfCompletion) {
        this.levelOfCompletion = levelOfCompletion;
    }

    public BooleanFilter getCompleted() {
        return completed;
    }

    public BooleanFilter completed() {
        if (completed == null) {
            completed = new BooleanFilter();
        }
        return completed;
    }

    public void setCompleted(BooleanFilter completed) {
        this.completed = completed;
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

    public LongFilter getWorkInProgressGroupId() {
        return workInProgressGroupId;
    }

    public LongFilter workInProgressGroupId() {
        if (workInProgressGroupId == null) {
            workInProgressGroupId = new LongFilter();
        }
        return workInProgressGroupId;
    }

    public void setWorkInProgressGroupId(LongFilter workInProgressGroupId) {
        this.workInProgressGroupId = workInProgressGroupId;
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

    public LongFilter getWorkProjectRegisterId() {
        return workProjectRegisterId;
    }

    public LongFilter workProjectRegisterId() {
        if (workProjectRegisterId == null) {
            workProjectRegisterId = new LongFilter();
        }
        return workProjectRegisterId;
    }

    public void setWorkProjectRegisterId(LongFilter workProjectRegisterId) {
        this.workProjectRegisterId = workProjectRegisterId;
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

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            invoiceId = new LongFilter();
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getOutletCodeId() {
        return outletCodeId;
    }

    public LongFilter outletCodeId() {
        if (outletCodeId == null) {
            outletCodeId = new LongFilter();
        }
        return outletCodeId;
    }

    public void setOutletCodeId(LongFilter outletCodeId) {
        this.outletCodeId = outletCodeId;
    }

    public LongFilter getSettlementTransactionId() {
        return settlementTransactionId;
    }

    public LongFilter settlementTransactionId() {
        if (settlementTransactionId == null) {
            settlementTransactionId = new LongFilter();
        }
        return settlementTransactionId;
    }

    public void setSettlementTransactionId(LongFilter settlementTransactionId) {
        this.settlementTransactionId = settlementTransactionId;
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
        final WorkInProgressRegistrationCriteria that = (WorkInProgressRegistrationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(instalmentAmount, that.instalmentAmount) &&
            Objects.equals(levelOfCompletion, that.levelOfCompletion) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(workInProgressGroupId, that.workInProgressGroupId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(workProjectRegisterId, that.workProjectRegisterId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(assetAccessoryId, that.assetAccessoryId) &&
            Objects.equals(assetWarrantyId, that.assetWarrantyId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(outletCodeId, that.outletCodeId) &&
            Objects.equals(settlementTransactionId, that.settlementTransactionId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(deliveryNoteId, that.deliveryNoteId) &&
            Objects.equals(jobSheetId, that.jobSheetId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNumber,
            particulars,
            instalmentAmount,
            levelOfCompletion,
            completed,
            placeholderId,
            workInProgressGroupId,
            settlementCurrencyId,
            workProjectRegisterId,
            businessDocumentId,
            assetAccessoryId,
            assetWarrantyId,
            invoiceId,
            outletCodeId,
            settlementTransactionId,
            purchaseOrderId,
            deliveryNoteId,
            jobSheetId,
            dealerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressRegistrationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (instalmentAmount != null ? "instalmentAmount=" + instalmentAmount + ", " : "") +
            (levelOfCompletion != null ? "levelOfCompletion=" + levelOfCompletion + ", " : "") +
            (completed != null ? "completed=" + completed + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (workInProgressGroupId != null ? "workInProgressGroupId=" + workInProgressGroupId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (workProjectRegisterId != null ? "workProjectRegisterId=" + workProjectRegisterId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (assetAccessoryId != null ? "assetAccessoryId=" + assetAccessoryId + ", " : "") +
            (assetWarrantyId != null ? "assetWarrantyId=" + assetWarrantyId + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            (outletCodeId != null ? "outletCodeId=" + outletCodeId + ", " : "") +
            (settlementTransactionId != null ? "settlementTransactionId=" + settlementTransactionId + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (deliveryNoteId != null ? "deliveryNoteId=" + deliveryNoteId + ", " : "") +
            (jobSheetId != null ? "jobSheetId=" + jobSheetId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
