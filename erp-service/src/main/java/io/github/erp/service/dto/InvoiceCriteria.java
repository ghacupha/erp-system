package io.github.erp.service.dto;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Criteria class for the {@link io.github.erp.domain.Invoice} entity. This class is used
 * in {@link io.github.erp.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invoiceNumber;

    private LocalDateFilter invoiceDate;

    private BigDecimalFilter invoiceAmount;

    private StringFilter paymentCategory;

    private StringFilter dealerName;

    private LongFilter paymentId;

    private LongFilter dealerId;

    public InvoiceCriteria() {
    }

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.invoiceNumber = other.invoiceNumber == null ? null : other.invoiceNumber.copy();
        this.invoiceDate = other.invoiceDate == null ? null : other.invoiceDate.copy();
        this.invoiceAmount = other.invoiceAmount == null ? null : other.invoiceAmount.copy();
        this.paymentCategory = other.paymentCategory == null ? null : other.paymentCategory.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateFilter getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimalFilter getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimalFilter invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public StringFilter getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(StringFilter paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public StringFilter getDealerName() {
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(invoiceAmount, that.invoiceAmount) &&
            Objects.equals(paymentCategory, that.paymentCategory) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(dealerId, that.dealerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        invoiceNumber,
        invoiceDate,
        invoiceAmount,
        paymentCategory,
        dealerName,
        paymentId,
        dealerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (invoiceNumber != null ? "invoiceNumber=" + invoiceNumber + ", " : "") +
                (invoiceDate != null ? "invoiceDate=" + invoiceDate + ", " : "") +
                (invoiceAmount != null ? "invoiceAmount=" + invoiceAmount + ", " : "") +
                (paymentCategory != null ? "paymentCategory=" + paymentCategory + ", " : "") +
                (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
                (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
                (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            "}";
    }

}