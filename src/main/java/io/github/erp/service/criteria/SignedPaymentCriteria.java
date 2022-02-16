package io.github.erp.service.criteria;

import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.util.Objects;

import io.github.erp.erp.resources.SignedPaymentResource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.SignedPayment} entity. This class is used
 * in {@link SignedPaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signed-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SignedPaymentCriteria implements Serializable, Criteria {

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

    private StringFilter transactionNumber;

    private LocalDateFilter transactionDate;

    private CurrencyTypesFilter transactionCurrency;

    private BigDecimalFilter transactionAmount;

    private StringFilter dealerName;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter paymentCategoryId;

    private LongFilter placeholderId;

    private LongFilter signedPaymentGroupId;

    private Boolean distinct;

    public SignedPaymentCriteria() {}

    public SignedPaymentCriteria(SignedPaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transactionNumber = other.transactionNumber == null ? null : other.transactionNumber.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionCurrency = other.transactionCurrency == null ? null : other.transactionCurrency.copy();
        this.transactionAmount = other.transactionAmount == null ? null : other.transactionAmount.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.signedPaymentGroupId = other.signedPaymentGroupId == null ? null : other.signedPaymentGroupId.copy();
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

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
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

    public LongFilter getPaymentCategoryId() {
        return paymentCategoryId;
    }

    public LongFilter paymentCategoryId() {
        if (paymentCategoryId == null) {
            paymentCategoryId = new LongFilter();
        }
        return paymentCategoryId;
    }

    public void setPaymentCategoryId(LongFilter paymentCategoryId) {
        this.paymentCategoryId = paymentCategoryId;
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

    public LongFilter getSignedPaymentGroupId() {
        return signedPaymentGroupId;
    }

    public LongFilter signedPaymentGroupId() {
        if (signedPaymentGroupId == null) {
            signedPaymentGroupId = new LongFilter();
        }
        return signedPaymentGroupId;
    }

    public void setSignedPaymentGroupId(LongFilter signedPaymentGroupId) {
        this.signedPaymentGroupId = signedPaymentGroupId;
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
            Objects.equals(transactionNumber, that.transactionNumber) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionCurrency, that.transactionCurrency) &&
            Objects.equals(transactionAmount, that.transactionAmount) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(signedPaymentGroupId, that.signedPaymentGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transactionNumber,
            transactionDate,
            transactionCurrency,
            transactionAmount,
            dealerName,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            paymentCategoryId,
            placeholderId,
            signedPaymentGroupId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignedPaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transactionNumber != null ? "transactionNumber=" + transactionNumber + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (transactionCurrency != null ? "transactionCurrency=" + transactionCurrency + ", " : "") +
            (transactionAmount != null ? "transactionAmount=" + transactionAmount + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (signedPaymentGroupId != null ? "signedPaymentGroupId=" + signedPaymentGroupId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
