package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.Payment} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

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

    private StringFilter paymentNumber;

    private LocalDateFilter paymentDate;

    private BigDecimalFilter paymentAmount;

    private StringFilter description;

    private CurrencyTypesFilter currency;

    private DoubleFilter conversionRate;

    private LongFilter ownedInvoiceId;

    private LongFilter dealerId;

    private LongFilter paymentCategoryId;

    private LongFilter taxRuleId;

    private LongFilter paymentCalculationId;

    private LongFilter paymentRequisitionId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentNumber = other.paymentNumber == null ? null : other.paymentNumber.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.conversionRate = other.conversionRate == null ? null : other.conversionRate.copy();
        this.ownedInvoiceId = other.ownedInvoiceId == null ? null : other.ownedInvoiceId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.taxRuleId = other.taxRuleId == null ? null : other.taxRuleId.copy();
        this.paymentCalculationId = other.paymentCalculationId == null ? null : other.paymentCalculationId.copy();
        this.paymentRequisitionId = other.paymentRequisitionId == null ? null : other.paymentRequisitionId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public CurrencyTypesFilter getCurrency() {
        return currency;
    }

    public CurrencyTypesFilter currency() {
        if (currency == null) {
            currency = new CurrencyTypesFilter();
        }
        return currency;
    }

    public void setCurrency(CurrencyTypesFilter currency) {
        this.currency = currency;
    }

    public DoubleFilter getConversionRate() {
        return conversionRate;
    }

    public DoubleFilter conversionRate() {
        if (conversionRate == null) {
            conversionRate = new DoubleFilter();
        }
        return conversionRate;
    }

    public void setConversionRate(DoubleFilter conversionRate) {
        this.conversionRate = conversionRate;
    }

    public LongFilter getOwnedInvoiceId() {
        return ownedInvoiceId;
    }

    public LongFilter ownedInvoiceId() {
        if (ownedInvoiceId == null) {
            ownedInvoiceId = new LongFilter();
        }
        return ownedInvoiceId;
    }

    public void setOwnedInvoiceId(LongFilter ownedInvoiceId) {
        this.ownedInvoiceId = ownedInvoiceId;
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

    public LongFilter getTaxRuleId() {
        return taxRuleId;
    }

    public LongFilter taxRuleId() {
        if (taxRuleId == null) {
            taxRuleId = new LongFilter();
        }
        return taxRuleId;
    }

    public void setTaxRuleId(LongFilter taxRuleId) {
        this.taxRuleId = taxRuleId;
    }

    public LongFilter getPaymentCalculationId() {
        return paymentCalculationId;
    }

    public LongFilter paymentCalculationId() {
        if (paymentCalculationId == null) {
            paymentCalculationId = new LongFilter();
        }
        return paymentCalculationId;
    }

    public void setPaymentCalculationId(LongFilter paymentCalculationId) {
        this.paymentCalculationId = paymentCalculationId;
    }

    public LongFilter getPaymentRequisitionId() {
        return paymentRequisitionId;
    }

    public LongFilter paymentRequisitionId() {
        if (paymentRequisitionId == null) {
            paymentRequisitionId = new LongFilter();
        }
        return paymentRequisitionId;
    }

    public void setPaymentRequisitionId(LongFilter paymentRequisitionId) {
        this.paymentRequisitionId = paymentRequisitionId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentNumber, that.paymentNumber) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(conversionRate, that.conversionRate) &&
            Objects.equals(ownedInvoiceId, that.ownedInvoiceId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(taxRuleId, that.taxRuleId) &&
            Objects.equals(paymentCalculationId, that.paymentCalculationId) &&
            Objects.equals(paymentRequisitionId, that.paymentRequisitionId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentNumber,
            paymentDate,
            paymentAmount,
            description,
            currency,
            conversionRate,
            ownedInvoiceId,
            dealerId,
            paymentCategoryId,
            taxRuleId,
            paymentCalculationId,
            paymentRequisitionId,
            placeholderId,
            distinct
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
            (description != null ? "description=" + description + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (conversionRate != null ? "conversionRate=" + conversionRate + ", " : "") +
            (ownedInvoiceId != null ? "ownedInvoiceId=" + ownedInvoiceId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (taxRuleId != null ? "taxRuleId=" + taxRuleId + ", " : "") +
            (paymentCalculationId != null ? "paymentCalculationId=" + paymentCalculationId + ", " : "") +
            (paymentRequisitionId != null ? "paymentRequisitionId=" + paymentRequisitionId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
