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

    private StringFilter dealerName;

    private BigDecimalFilter invoicedAmount;

    private BigDecimalFilter disbursementCost;

    private BigDecimalFilter vatableAmount;

    public PaymentRequisitionCriteria() {}

    public PaymentRequisitionCriteria(PaymentRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.invoicedAmount = other.invoicedAmount == null ? null : other.invoicedAmount.copy();
        this.disbursementCost = other.disbursementCost == null ? null : other.disbursementCost.copy();
        this.vatableAmount = other.vatableAmount == null ? null : other.vatableAmount.copy();
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

    public StringFilter getDealerName() {
        return dealerName;
    }

    public StringFilter dealerName() {
        if (dealerName == null) {
            dealerName = new StringFilter();
        }
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
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
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(invoicedAmount, that.invoicedAmount) &&
            Objects.equals(disbursementCost, that.disbursementCost) &&
            Objects.equals(vatableAmount, that.vatableAmount)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dealerName, invoicedAmount, disbursementCost, vatableAmount);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (invoicedAmount != null ? "invoicedAmount=" + invoicedAmount + ", " : "") +
            (disbursementCost != null ? "disbursementCost=" + disbursementCost + ", " : "") +
            (vatableAmount != null ? "vatableAmount=" + vatableAmount + ", " : "") +
            "}";
    }
}
