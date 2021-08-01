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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PaymentCalculation} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentCalculationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-calculations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCalculationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentNumber;

    private LocalDateFilter paymentDate;

    private StringFilter paymentCategory;

    private BigDecimalFilter paymentExpense;

    private BigDecimalFilter withholdingVAT;

    private BigDecimalFilter withholdingTax;

    private BigDecimalFilter paymentAmount;

    private LongFilter paymentId;

    public PaymentCalculationCriteria() {}

    public PaymentCalculationCriteria(PaymentCalculationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentNumber = other.paymentNumber == null ? null : other.paymentNumber.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.paymentCategory = other.paymentCategory == null ? null : other.paymentCategory.copy();
        this.paymentExpense = other.paymentExpense == null ? null : other.paymentExpense.copy();
        this.withholdingVAT = other.withholdingVAT == null ? null : other.withholdingVAT.copy();
        this.withholdingTax = other.withholdingTax == null ? null : other.withholdingTax.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
    }

    @Override
    public PaymentCalculationCriteria copy() {
        return new PaymentCalculationCriteria(this);
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

    public StringFilter getPaymentNumber() {
        return paymentNumber;
    }

    public StringFilter paymentNumber() {
        if (paymentNumber == null) {
            paymentNumber = new StringFilter();
        }
        return paymentNumber;
    }

    public void setPaymentNumber(StringFilter paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public LocalDateFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new LocalDateFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public StringFilter getPaymentCategory() {
        return paymentCategory;
    }

    public StringFilter paymentCategory() {
        if (paymentCategory == null) {
            paymentCategory = new StringFilter();
        }
        return paymentCategory;
    }

    public void setPaymentCategory(StringFilter paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public BigDecimalFilter getPaymentExpense() {
        return paymentExpense;
    }

    public BigDecimalFilter paymentExpense() {
        if (paymentExpense == null) {
            paymentExpense = new BigDecimalFilter();
        }
        return paymentExpense;
    }

    public void setPaymentExpense(BigDecimalFilter paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimalFilter getWithholdingVAT() {
        return withholdingVAT;
    }

    public BigDecimalFilter withholdingVAT() {
        if (withholdingVAT == null) {
            withholdingVAT = new BigDecimalFilter();
        }
        return withholdingVAT;
    }

    public void setWithholdingVAT(BigDecimalFilter withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimalFilter getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimalFilter withholdingTax() {
        if (withholdingTax == null) {
            withholdingTax = new BigDecimalFilter();
        }
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimalFilter withholdingTax) {
        this.withholdingTax = withholdingTax;
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

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCalculationCriteria that = (PaymentCalculationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentNumber, that.paymentNumber) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentCategory, that.paymentCategory) &&
            Objects.equals(paymentExpense, that.paymentExpense) &&
            Objects.equals(withholdingVAT, that.withholdingVAT) &&
            Objects.equals(withholdingTax, that.withholdingTax) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(paymentId, that.paymentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentNumber,
            paymentDate,
            paymentCategory,
            paymentExpense,
            withholdingVAT,
            withholdingTax,
            paymentAmount,
            paymentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentNumber != null ? "paymentNumber=" + paymentNumber + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (paymentCategory != null ? "paymentCategory=" + paymentCategory + ", " : "") +
            (paymentExpense != null ? "paymentExpense=" + paymentExpense + ", " : "") +
            (withholdingVAT != null ? "withholdingVAT=" + withholdingVAT + ", " : "") +
            (withholdingTax != null ? "withholdingTax=" + withholdingTax + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            "}";
    }
}
