package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.domain.enumeration.PaymentStatus;
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
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.SettlementRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.SettlementRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /settlement-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SettlementRequisitionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {

        public PaymentStatusFilter() {}

        public PaymentStatusFilter(PaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaymentStatusFilter copy() {
            return new PaymentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private UUIDFilter serialNumber;

    private ZonedDateTimeFilter timeOfRequisition;

    private StringFilter requisitionNumber;

    private BigDecimalFilter paymentAmount;

    private PaymentStatusFilter paymentStatus;

    private StringFilter transactionId;

    private LocalDateFilter transactionDate;

    private LongFilter settlementCurrencyId;

    private LongFilter currentOwnerId;

    private LongFilter nativeOwnerId;

    private LongFilter nativeDepartmentId;

    private LongFilter billerId;

    private LongFilter paymentInvoiceId;

    private LongFilter deliveryNoteId;

    private LongFilter jobSheetId;

    private LongFilter signaturesId;

    private LongFilter businessDocumentId;

    private LongFilter applicationMappingId;

    private LongFilter placeholderId;

    private LongFilter settlementId;

    private Boolean distinct;

    public SettlementRequisitionCriteria() {}

    public SettlementRequisitionCriteria(SettlementRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.timeOfRequisition = other.timeOfRequisition == null ? null : other.timeOfRequisition.copy();
        this.requisitionNumber = other.requisitionNumber == null ? null : other.requisitionNumber.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.paymentStatus = other.paymentStatus == null ? null : other.paymentStatus.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.currentOwnerId = other.currentOwnerId == null ? null : other.currentOwnerId.copy();
        this.nativeOwnerId = other.nativeOwnerId == null ? null : other.nativeOwnerId.copy();
        this.nativeDepartmentId = other.nativeDepartmentId == null ? null : other.nativeDepartmentId.copy();
        this.billerId = other.billerId == null ? null : other.billerId.copy();
        this.paymentInvoiceId = other.paymentInvoiceId == null ? null : other.paymentInvoiceId.copy();
        this.deliveryNoteId = other.deliveryNoteId == null ? null : other.deliveryNoteId.copy();
        this.jobSheetId = other.jobSheetId == null ? null : other.jobSheetId.copy();
        this.signaturesId = other.signaturesId == null ? null : other.signaturesId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.applicationMappingId = other.applicationMappingId == null ? null : other.applicationMappingId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.settlementId = other.settlementId == null ? null : other.settlementId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SettlementRequisitionCriteria copy() {
        return new SettlementRequisitionCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public UUIDFilter getSerialNumber() {
        return serialNumber;
    }

    public UUIDFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new UUIDFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(UUIDFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ZonedDateTimeFilter getTimeOfRequisition() {
        return timeOfRequisition;
    }

    public ZonedDateTimeFilter timeOfRequisition() {
        if (timeOfRequisition == null) {
            timeOfRequisition = new ZonedDateTimeFilter();
        }
        return timeOfRequisition;
    }

    public void setTimeOfRequisition(ZonedDateTimeFilter timeOfRequisition) {
        this.timeOfRequisition = timeOfRequisition;
    }

    public StringFilter getRequisitionNumber() {
        return requisitionNumber;
    }

    public StringFilter requisitionNumber() {
        if (requisitionNumber == null) {
            requisitionNumber = new StringFilter();
        }
        return requisitionNumber;
    }

    public void setRequisitionNumber(StringFilter requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimalFilter paymentAmount() {
        if (paymentAmount == null) {
            paymentAmount = new BigDecimalFilter();
        }
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentStatusFilter paymentStatus() {
        if (paymentStatus == null) {
            paymentStatus = new PaymentStatusFilter();
        }
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public StringFilter transactionId() {
        if (transactionId == null) {
            transactionId = new StringFilter();
        }
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
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

    public LongFilter getCurrentOwnerId() {
        return currentOwnerId;
    }

    public LongFilter currentOwnerId() {
        if (currentOwnerId == null) {
            currentOwnerId = new LongFilter();
        }
        return currentOwnerId;
    }

    public void setCurrentOwnerId(LongFilter currentOwnerId) {
        this.currentOwnerId = currentOwnerId;
    }

    public LongFilter getNativeOwnerId() {
        return nativeOwnerId;
    }

    public LongFilter nativeOwnerId() {
        if (nativeOwnerId == null) {
            nativeOwnerId = new LongFilter();
        }
        return nativeOwnerId;
    }

    public void setNativeOwnerId(LongFilter nativeOwnerId) {
        this.nativeOwnerId = nativeOwnerId;
    }

    public LongFilter getNativeDepartmentId() {
        return nativeDepartmentId;
    }

    public LongFilter nativeDepartmentId() {
        if (nativeDepartmentId == null) {
            nativeDepartmentId = new LongFilter();
        }
        return nativeDepartmentId;
    }

    public void setNativeDepartmentId(LongFilter nativeDepartmentId) {
        this.nativeDepartmentId = nativeDepartmentId;
    }

    public LongFilter getBillerId() {
        return billerId;
    }

    public LongFilter billerId() {
        if (billerId == null) {
            billerId = new LongFilter();
        }
        return billerId;
    }

    public void setBillerId(LongFilter billerId) {
        this.billerId = billerId;
    }

    public LongFilter getPaymentInvoiceId() {
        return paymentInvoiceId;
    }

    public LongFilter paymentInvoiceId() {
        if (paymentInvoiceId == null) {
            paymentInvoiceId = new LongFilter();
        }
        return paymentInvoiceId;
    }

    public void setPaymentInvoiceId(LongFilter paymentInvoiceId) {
        this.paymentInvoiceId = paymentInvoiceId;
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

    public LongFilter getSignaturesId() {
        return signaturesId;
    }

    public LongFilter signaturesId() {
        if (signaturesId == null) {
            signaturesId = new LongFilter();
        }
        return signaturesId;
    }

    public void setSignaturesId(LongFilter signaturesId) {
        this.signaturesId = signaturesId;
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

    public LongFilter getApplicationMappingId() {
        return applicationMappingId;
    }

    public LongFilter applicationMappingId() {
        if (applicationMappingId == null) {
            applicationMappingId = new LongFilter();
        }
        return applicationMappingId;
    }

    public void setApplicationMappingId(LongFilter applicationMappingId) {
        this.applicationMappingId = applicationMappingId;
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
        final SettlementRequisitionCriteria that = (SettlementRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(timeOfRequisition, that.timeOfRequisition) &&
            Objects.equals(requisitionNumber, that.requisitionNumber) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(currentOwnerId, that.currentOwnerId) &&
            Objects.equals(nativeOwnerId, that.nativeOwnerId) &&
            Objects.equals(nativeDepartmentId, that.nativeDepartmentId) &&
            Objects.equals(billerId, that.billerId) &&
            Objects.equals(paymentInvoiceId, that.paymentInvoiceId) &&
            Objects.equals(deliveryNoteId, that.deliveryNoteId) &&
            Objects.equals(jobSheetId, that.jobSheetId) &&
            Objects.equals(signaturesId, that.signaturesId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(applicationMappingId, that.applicationMappingId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(settlementId, that.settlementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            serialNumber,
            timeOfRequisition,
            requisitionNumber,
            paymentAmount,
            paymentStatus,
            transactionId,
            transactionDate,
            settlementCurrencyId,
            currentOwnerId,
            nativeOwnerId,
            nativeDepartmentId,
            billerId,
            paymentInvoiceId,
            deliveryNoteId,
            jobSheetId,
            signaturesId,
            businessDocumentId,
            applicationMappingId,
            placeholderId,
            settlementId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (timeOfRequisition != null ? "timeOfRequisition=" + timeOfRequisition + ", " : "") +
            (requisitionNumber != null ? "requisitionNumber=" + requisitionNumber + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (paymentStatus != null ? "paymentStatus=" + paymentStatus + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (currentOwnerId != null ? "currentOwnerId=" + currentOwnerId + ", " : "") +
            (nativeOwnerId != null ? "nativeOwnerId=" + nativeOwnerId + ", " : "") +
            (nativeDepartmentId != null ? "nativeDepartmentId=" + nativeDepartmentId + ", " : "") +
            (billerId != null ? "billerId=" + billerId + ", " : "") +
            (paymentInvoiceId != null ? "paymentInvoiceId=" + paymentInvoiceId + ", " : "") +
            (deliveryNoteId != null ? "deliveryNoteId=" + deliveryNoteId + ", " : "") +
            (jobSheetId != null ? "jobSheetId=" + jobSheetId + ", " : "") +
            (signaturesId != null ? "signaturesId=" + signaturesId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (applicationMappingId != null ? "applicationMappingId=" + applicationMappingId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (settlementId != null ? "settlementId=" + settlementId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
