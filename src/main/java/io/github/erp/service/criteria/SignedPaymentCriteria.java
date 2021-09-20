package io.github.erp.service.criteria;

import io.github.erp.domain.enumeration.CategoryTypes;
import io.github.erp.domain.enumeration.CurrencyTypes;
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
 * Criteria class for the {@link io.github.erp.domain.SignedPayment} entity. This class is used
 * in {@link io.github.erp.web.rest.SignedPaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signed-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SignedPaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CategoryTypes
     */
    public static class CategoryTypesFilter extends Filter<CategoryTypes> {

        public CategoryTypesFilter() {}

        public CategoryTypesFilter(CategoryTypesFilter filter) {
            super(filter);
        }

        @Override
        public CategoryTypesFilter copy() {
            return new CategoryTypesFilter(this);
        }
    }

    /**
     * Class for filtering CurrencyTypes
     */
    public static class CurrencyTypesFilter extends Filter<CurrencyTypes> {

        public CurrencyTypesFilter() {}

        public CurrencyTypesFilter(CurrencyTypesFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyTypesFilter copy() {
            return new CurrencyTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CategoryTypesFilter paymentCategory;

    private StringFilter transactionNumber;

    private LocalDateFilter transactionDate;

    private CurrencyTypesFilter transactionCurrency;

    private BigDecimalFilter transactionAmount;

    private StringFilter beneficiary;

    private LongFilter paymentLabelId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public SignedPaymentCriteria() {}

    public SignedPaymentCriteria(SignedPaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentCategory = other.paymentCategory == null ? null : other.paymentCategory.copy();
        this.transactionNumber = other.transactionNumber == null ? null : other.transactionNumber.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionCurrency = other.transactionCurrency == null ? null : other.transactionCurrency.copy();
        this.transactionAmount = other.transactionAmount == null ? null : other.transactionAmount.copy();
        this.beneficiary = other.beneficiary == null ? null : other.beneficiary.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SignedPaymentCriteria copy() {
        return new SignedPaymentCriteria(this);
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

    public CategoryTypesFilter getPaymentCategory() {
        return paymentCategory;
    }

    public CategoryTypesFilter paymentCategory() {
        if (paymentCategory == null) {
            paymentCategory = new CategoryTypesFilter();
        }
        return paymentCategory;
    }

    public void setPaymentCategory(CategoryTypesFilter paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public StringFilter getTransactionNumber() {
        return transactionNumber;
    }

    public StringFilter transactionNumber() {
        if (transactionNumber == null) {
            transactionNumber = new StringFilter();
        }
        return transactionNumber;
    }

    public void setTransactionNumber(StringFilter transactionNumber) {
        this.transactionNumber = transactionNumber;
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

    public CurrencyTypesFilter getTransactionCurrency() {
        return transactionCurrency;
    }

    public CurrencyTypesFilter transactionCurrency() {
        if (transactionCurrency == null) {
            transactionCurrency = new CurrencyTypesFilter();
        }
        return transactionCurrency;
    }

    public void setTransactionCurrency(CurrencyTypesFilter transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public BigDecimalFilter getTransactionAmount() {
        return transactionAmount;
    }

    public BigDecimalFilter transactionAmount() {
        if (transactionAmount == null) {
            transactionAmount = new BigDecimalFilter();
        }
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimalFilter transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public StringFilter getBeneficiary() {
        return beneficiary;
    }

    public StringFilter beneficiary() {
        if (beneficiary == null) {
            beneficiary = new StringFilter();
        }
        return beneficiary;
    }

    public void setBeneficiary(StringFilter beneficiary) {
        this.beneficiary = beneficiary;
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
        final SignedPaymentCriteria that = (SignedPaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentCategory, that.paymentCategory) &&
            Objects.equals(transactionNumber, that.transactionNumber) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionCurrency, that.transactionCurrency) &&
            Objects.equals(transactionAmount, that.transactionAmount) &&
            Objects.equals(beneficiary, that.beneficiary) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentCategory,
            transactionNumber,
            transactionDate,
            transactionCurrency,
            transactionAmount,
            beneficiary,
            paymentLabelId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignedPaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentCategory != null ? "paymentCategory=" + paymentCategory + ", " : "") +
            (transactionNumber != null ? "transactionNumber=" + transactionNumber + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (transactionCurrency != null ? "transactionCurrency=" + transactionCurrency + ", " : "") +
            (transactionAmount != null ? "transactionAmount=" + transactionAmount + ", " : "") +
            (beneficiary != null ? "beneficiary=" + beneficiary + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
