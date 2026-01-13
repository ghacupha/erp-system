package io.github.erp.service.criteria;

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
import io.github.erp.domain.enumeration.ReconciliationStatusType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseSettlement} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseSettlementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-settlements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseSettlementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReconciliationStatusType
     */
    public static class ReconciliationStatusTypeFilter extends Filter<ReconciliationStatusType> {

        public ReconciliationStatusTypeFilter() {}

        public ReconciliationStatusTypeFilter(ReconciliationStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReconciliationStatusTypeFilter copy() {
            return new ReconciliationStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter settlementDate;

    private BigDecimalFilter invoiceAmount;

    private StringFilter invoiceReference;

    private BigDecimalFilter varianceAmount;

    private StringFilter varianceReason;

    private UUIDFilter postingId;

    private ReconciliationStatusTypeFilter reconciliationStatus;

    private LongFilter leaseContractId;

    private LongFilter periodId;

    private LongFilter leasePaymentId;

    private Boolean distinct;

    public LeaseSettlementCriteria() {}

    public LeaseSettlementCriteria(LeaseSettlementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.settlementDate = other.settlementDate == null ? null : other.settlementDate.copy();
        this.invoiceAmount = other.invoiceAmount == null ? null : other.invoiceAmount.copy();
        this.invoiceReference = other.invoiceReference == null ? null : other.invoiceReference.copy();
        this.varianceAmount = other.varianceAmount == null ? null : other.varianceAmount.copy();
        this.varianceReason = other.varianceReason == null ? null : other.varianceReason.copy();
        this.postingId = other.postingId == null ? null : other.postingId.copy();
        this.reconciliationStatus = other.reconciliationStatus == null ? null : other.reconciliationStatus.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.periodId = other.periodId == null ? null : other.periodId.copy();
        this.leasePaymentId = other.leasePaymentId == null ? null : other.leasePaymentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseSettlementCriteria copy() {
        return new LeaseSettlementCriteria(this);
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

    public LocalDateFilter getSettlementDate() {
        return settlementDate;
    }

    public LocalDateFilter settlementDate() {
        if (settlementDate == null) {
            settlementDate = new LocalDateFilter();
        }
        return settlementDate;
    }

    public void setSettlementDate(LocalDateFilter settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimalFilter getInvoiceAmount() {
        return invoiceAmount;
    }

    public BigDecimalFilter invoiceAmount() {
        if (invoiceAmount == null) {
            invoiceAmount = new BigDecimalFilter();
        }
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimalFilter invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public StringFilter getInvoiceReference() {
        return invoiceReference;
    }

    public StringFilter invoiceReference() {
        if (invoiceReference == null) {
            invoiceReference = new StringFilter();
        }
        return invoiceReference;
    }

    public void setInvoiceReference(StringFilter invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public BigDecimalFilter getVarianceAmount() {
        return varianceAmount;
    }

    public BigDecimalFilter varianceAmount() {
        if (varianceAmount == null) {
            varianceAmount = new BigDecimalFilter();
        }
        return varianceAmount;
    }

    public void setVarianceAmount(BigDecimalFilter varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public StringFilter getVarianceReason() {
        return varianceReason;
    }

    public StringFilter varianceReason() {
        if (varianceReason == null) {
            varianceReason = new StringFilter();
        }
        return varianceReason;
    }

    public void setVarianceReason(StringFilter varianceReason) {
        this.varianceReason = varianceReason;
    }

    public UUIDFilter getPostingId() {
        return postingId;
    }

    public UUIDFilter postingId() {
        if (postingId == null) {
            postingId = new UUIDFilter();
        }
        return postingId;
    }

    public void setPostingId(UUIDFilter postingId) {
        this.postingId = postingId;
    }

    public ReconciliationStatusTypeFilter getReconciliationStatus() {
        return reconciliationStatus;
    }

    public ReconciliationStatusTypeFilter reconciliationStatus() {
        if (reconciliationStatus == null) {
            reconciliationStatus = new ReconciliationStatusTypeFilter();
        }
        return reconciliationStatus;
    }

    public void setReconciliationStatus(ReconciliationStatusTypeFilter reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public LongFilter getPeriodId() {
        return periodId;
    }

    public LongFilter periodId() {
        if (periodId == null) {
            periodId = new LongFilter();
        }
        return periodId;
    }

    public void setPeriodId(LongFilter periodId) {
        this.periodId = periodId;
    }

    public LongFilter getLeasePaymentId() {
        return leasePaymentId;
    }

    public LongFilter leasePaymentId() {
        if (leasePaymentId == null) {
            leasePaymentId = new LongFilter();
        }
        return leasePaymentId;
    }

    public void setLeasePaymentId(LongFilter leasePaymentId) {
        this.leasePaymentId = leasePaymentId;
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
        final LeaseSettlementCriteria that = (LeaseSettlementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(settlementDate, that.settlementDate) &&
            Objects.equals(invoiceAmount, that.invoiceAmount) &&
            Objects.equals(invoiceReference, that.invoiceReference) &&
            Objects.equals(varianceAmount, that.varianceAmount) &&
            Objects.equals(varianceReason, that.varianceReason) &&
            Objects.equals(postingId, that.postingId) &&
            Objects.equals(reconciliationStatus, that.reconciliationStatus) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(periodId, that.periodId) &&
            Objects.equals(leasePaymentId, that.leasePaymentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            settlementDate,
            invoiceAmount,
            invoiceReference,
            varianceAmount,
            varianceReason,
            postingId,
            reconciliationStatus,
            leaseContractId,
            periodId,
            leasePaymentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseSettlementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (settlementDate != null ? "settlementDate=" + settlementDate + ", " : "") +
            (invoiceAmount != null ? "invoiceAmount=" + invoiceAmount + ", " : "") +
            (invoiceReference != null ? "invoiceReference=" + invoiceReference + ", " : "") +
            (varianceAmount != null ? "varianceAmount=" + varianceAmount + ", " : "") +
            (varianceReason != null ? "varianceReason=" + varianceReason + ", " : "") +
            (postingId != null ? "postingId=" + postingId + ", " : "") +
            (reconciliationStatus != null ? "reconciliationStatus=" + reconciliationStatus + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (periodId != null ? "periodId=" + periodId + ", " : "") +
            (leasePaymentId != null ? "leasePaymentId=" + leasePaymentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
