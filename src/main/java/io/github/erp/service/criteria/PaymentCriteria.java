package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

    private BigDecimalFilter invoicedAmount;

    private BigDecimalFilter paymentAmount;

    private StringFilter description;

    private CurrencyTypesFilter settlementCurrency;

    private StringFilter dealerName;

    private StringFilter purchaseOrderNumber;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter paymentCategoryId;

    private LongFilter placeholderId;

    private LongFilter paymentGroupId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentNumber = other.paymentNumber == null ? null : other.paymentNumber.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.invoicedAmount = other.invoicedAmount == null ? null : other.invoicedAmount.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.settlementCurrency = other.settlementCurrency == null ? null : other.settlementCurrency.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.purchaseOrderNumber = other.purchaseOrderNumber == null ? null : other.purchaseOrderNumber.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentGroupId = other.paymentGroupId == null ? null : other.paymentGroupId.copy();
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

    public CurrencyTypesFilter getSettlementCurrency() {
        return settlementCurrency;
    }

    public CurrencyTypesFilter settlementCurrency() {
        if (settlementCurrency == null) {
            settlementCurrency = new CurrencyTypesFilter();
        }
        return settlementCurrency;
    }

    public void setSettlementCurrency(CurrencyTypesFilter settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
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

    public StringFilter getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public StringFilter purchaseOrderNumber() {
        if (purchaseOrderNumber == null) {
            purchaseOrderNumber = new StringFilter();
        }
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(StringFilter purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
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

    public LongFilter getPaymentGroupId() {
        return paymentGroupId;
    }

    public LongFilter paymentGroupId() {
        if (paymentGroupId == null) {
            paymentGroupId = new LongFilter();
        }
        return paymentGroupId;
    }

    public void setPaymentGroupId(LongFilter paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
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
            Objects.equals(invoicedAmount, that.invoicedAmount) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(settlementCurrency, that.settlementCurrency) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(purchaseOrderNumber, that.purchaseOrderNumber) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentGroupId, that.paymentGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentNumber,
            paymentDate,
            invoicedAmount,
            paymentAmount,
            description,
            settlementCurrency,
            dealerName,
            purchaseOrderNumber,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            paymentCategoryId,
            placeholderId,
            paymentGroupId,
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
            (invoicedAmount != null ? "invoicedAmount=" + invoicedAmount + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (settlementCurrency != null ? "settlementCurrency=" + settlementCurrency + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (purchaseOrderNumber != null ? "purchaseOrderNumber=" + purchaseOrderNumber + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentGroupId != null ? "paymentGroupId=" + paymentGroupId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
