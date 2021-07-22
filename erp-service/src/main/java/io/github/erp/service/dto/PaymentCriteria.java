package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.Payment} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentNumber;

    private LocalDateFilter paymentDate;

    private BigDecimalFilter paymentAmount;

    private StringFilter dealerName;

    private StringFilter paymentCategory;

    private LongFilter ownedInvoiceId;

    private LongFilter calculationResultId;

    private LongFilter paymentRequisitionId;

    private LongFilter dealerId;

    public PaymentCriteria() {
    }

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentNumber = other.paymentNumber == null ? null : other.paymentNumber.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.paymentCategory = other.paymentCategory == null ? null : other.paymentCategory.copy();
        this.ownedInvoiceId = other.ownedInvoiceId == null ? null : other.ownedInvoiceId.copy();
        this.calculationResultId = other.calculationResultId == null ? null : other.calculationResultId.copy();
        this.paymentRequisitionId = other.paymentRequisitionId == null ? null : other.paymentRequisitionId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(StringFilter paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public StringFilter getDealerName() {
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public StringFilter getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(StringFilter paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public LongFilter getOwnedInvoiceId() {
        return ownedInvoiceId;
    }

    public void setOwnedInvoiceId(LongFilter ownedInvoiceId) {
        this.ownedInvoiceId = ownedInvoiceId;
    }

    public LongFilter getCalculationResultId() {
        return calculationResultId;
    }

    public void setCalculationResultId(LongFilter calculationResultId) {
        this.calculationResultId = calculationResultId;
    }

    public LongFilter getPaymentRequisitionId() {
        return paymentRequisitionId;
    }

    public void setPaymentRequisitionId(LongFilter paymentRequisitionId) {
        this.paymentRequisitionId = paymentRequisitionId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentNumber, that.paymentNumber) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(paymentCategory, that.paymentCategory) &&
            Objects.equals(ownedInvoiceId, that.ownedInvoiceId) &&
            Objects.equals(calculationResultId, that.calculationResultId) &&
            Objects.equals(paymentRequisitionId, that.paymentRequisitionId) &&
            Objects.equals(dealerId, that.dealerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paymentNumber,
        paymentDate,
        paymentAmount,
        dealerName,
        paymentCategory,
        ownedInvoiceId,
        calculationResultId,
        paymentRequisitionId,
        dealerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paymentNumber != null ? "paymentNumber=" + paymentNumber + ", " : "") +
                (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
                (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
                (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
                (paymentCategory != null ? "paymentCategory=" + paymentCategory + ", " : "") +
                (ownedInvoiceId != null ? "ownedInvoiceId=" + ownedInvoiceId + ", " : "") +
                (calculationResultId != null ? "calculationResultId=" + calculationResultId + ", " : "") +
                (paymentRequisitionId != null ? "paymentRequisitionId=" + paymentRequisitionId + ", " : "") +
                (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            "}";
    }

}
