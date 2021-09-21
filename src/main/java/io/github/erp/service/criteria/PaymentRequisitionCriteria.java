package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.PaymentRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentRequisitionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter invoicedAmount;

    private BigDecimalFilter disbursementCost;

    private BigDecimalFilter vatableAmount;

    private LongFilter paymentLabelId;

    private LongFilter dealerId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentRequisitionCriteria() {}

    public PaymentRequisitionCriteria(PaymentRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.invoicedAmount = other.invoicedAmount == null ? null : other.invoicedAmount.copy();
        this.disbursementCost = other.disbursementCost == null ? null : other.disbursementCost.copy();
        this.vatableAmount = other.vatableAmount == null ? null : other.vatableAmount.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentRequisitionCriteria copy() {
        return new PaymentRequisitionCriteria(this);
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

    public BigDecimalFilter getInvoicedAmount() {
        return invoicedAmount;
    }

    public BigDecimalFilter invoicedAmount() {
        if (invoicedAmount == null) {
            invoicedAmount = new BigDecimalFilter();
        }
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimalFilter invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimalFilter getDisbursementCost() {
        return disbursementCost;
    }

    public BigDecimalFilter disbursementCost() {
        if (disbursementCost == null) {
            disbursementCost = new BigDecimalFilter();
        }
        return disbursementCost;
    }

    public void setDisbursementCost(BigDecimalFilter disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimalFilter getVatableAmount() {
        return vatableAmount;
    }

    public BigDecimalFilter vatableAmount() {
        if (vatableAmount == null) {
            vatableAmount = new BigDecimalFilter();
        }
        return vatableAmount;
    }

    public void setVatableAmount(BigDecimalFilter vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public LongFilter getPaymentLabelId() {
        return paymentLabelId;
    }

    public LongFilter paymentLabelId() {
        if (paymentLabelId == null) {
            paymentLabelId = new LongFilter();
        }
        return paymentLabelId;
    }

    public void setPaymentLabelId(LongFilter paymentLabelId) {
        this.paymentLabelId = paymentLabelId;
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
        final PaymentRequisitionCriteria that = (PaymentRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoicedAmount, that.invoicedAmount) &&
            Objects.equals(disbursementCost, that.disbursementCost) &&
            Objects.equals(vatableAmount, that.vatableAmount) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoicedAmount, disbursementCost, vatableAmount, paymentLabelId, dealerId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (invoicedAmount != null ? "invoicedAmount=" + invoicedAmount + ", " : "") +
            (disbursementCost != null ? "disbursementCost=" + disbursementCost + ", " : "") +
            (vatableAmount != null ? "vatableAmount=" + vatableAmount + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
