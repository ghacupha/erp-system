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

    private LongFilter calculationResultId;

    public PaymentCalculationCriteria() {
    }

    public PaymentCalculationCriteria(PaymentCalculationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentNumber = other.paymentNumber == null ? null : other.paymentNumber.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.paymentCategory = other.paymentCategory == null ? null : other.paymentCategory.copy();
        this.paymentExpense = other.paymentExpense == null ? null : other.paymentExpense.copy();
        this.withholdingVAT = other.withholdingVAT == null ? null : other.withholdingVAT.copy();
        this.withholdingTax = other.withholdingTax == null ? null : other.withholdingTax.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.calculationResultId = other.calculationResultId == null ? null : other.calculationResultId.copy();
    }

    @Override
    public PaymentCalculationCriteria copy() {
        return new PaymentCalculationCriteria(this);
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

    public StringFilter getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(StringFilter paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public BigDecimalFilter getPaymentExpense() {
        return paymentExpense;
    }

    public void setPaymentExpense(BigDecimalFilter paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimalFilter getWithholdingVAT() {
        return withholdingVAT;
    }

    public void setWithholdingVAT(BigDecimalFilter withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimalFilter getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimalFilter withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LongFilter getCalculationResultId() {
        return calculationResultId;
    }

    public void setCalculationResultId(LongFilter calculationResultId) {
        this.calculationResultId = calculationResultId;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentNumber, that.paymentNumber) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentCategory, that.paymentCategory) &&
            Objects.equals(paymentExpense, that.paymentExpense) &&
            Objects.equals(withholdingVAT, that.withholdingVAT) &&
            Objects.equals(withholdingTax, that.withholdingTax) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(calculationResultId, that.calculationResultId);
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
        calculationResultId
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
                (calculationResultId != null ? "calculationResultId=" + calculationResultId + ", " : "") +
            "}";
    }

}
