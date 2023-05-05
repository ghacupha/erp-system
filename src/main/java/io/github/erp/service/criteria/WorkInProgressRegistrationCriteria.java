package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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

    private LongFilter placeholderId;

    private LongFilter paymentInvoicesId;

    private LongFilter serviceOutletId;

    private LongFilter settlementId;

    private LongFilter purchaseOrderId;

    private LongFilter deliveryNoteId;

    private LongFilter jobSheetId;

    private LongFilter dealerId;

    private LongFilter workInProgressGroupId;

    private LongFilter settlementCurrencyId;

    private LongFilter workProjectRegisterId;

    private LongFilter businessDocumentId;

    private Boolean distinct;

    public WorkInProgressRegistrationCriteria() {}

    public WorkInProgressRegistrationCriteria(WorkInProgressRegistrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.instalmentAmount = other.instalmentAmount == null ? null : other.instalmentAmount.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentInvoicesId = other.paymentInvoicesId == null ? null : other.paymentInvoicesId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.settlementId = other.settlementId == null ? null : other.settlementId.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.deliveryNoteId = other.deliveryNoteId == null ? null : other.deliveryNoteId.copy();
        this.jobSheetId = other.jobSheetId == null ? null : other.jobSheetId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.workInProgressGroupId = other.workInProgressGroupId == null ? null : other.workInProgressGroupId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.workProjectRegisterId = other.workProjectRegisterId == null ? null : other.workProjectRegisterId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
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
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentInvoicesId, that.paymentInvoicesId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(settlementId, that.settlementId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(deliveryNoteId, that.deliveryNoteId) &&
            Objects.equals(jobSheetId, that.jobSheetId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(workInProgressGroupId, that.workInProgressGroupId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(workProjectRegisterId, that.workProjectRegisterId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
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
            placeholderId,
            paymentInvoicesId,
            serviceOutletId,
            settlementId,
            purchaseOrderId,
            deliveryNoteId,
            jobSheetId,
            dealerId,
            workInProgressGroupId,
            settlementCurrencyId,
            workProjectRegisterId,
            businessDocumentId,
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
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentInvoicesId != null ? "paymentInvoicesId=" + paymentInvoicesId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (settlementId != null ? "settlementId=" + settlementId + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (deliveryNoteId != null ? "deliveryNoteId=" + deliveryNoteId + ", " : "") +
            (jobSheetId != null ? "jobSheetId=" + jobSheetId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (workInProgressGroupId != null ? "workInProgressGroupId=" + workInProgressGroupId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (workProjectRegisterId != null ? "workProjectRegisterId=" + workProjectRegisterId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
