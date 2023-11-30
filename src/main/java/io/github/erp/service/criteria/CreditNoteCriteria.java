package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.CreditNote} entity. This class is used
 * in {@link io.github.erp.web.rest.CreditNoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /credit-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CreditNoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter creditNumber;

    private LocalDateFilter creditNoteDate;

    private BigDecimalFilter creditAmount;

    private LongFilter purchaseOrdersId;

    private LongFilter invoicesId;

    private LongFilter paymentLabelId;

    private LongFilter placeholderId;

    private LongFilter settlementCurrencyId;

    private Boolean distinct;

    public CreditNoteCriteria() {}

    public CreditNoteCriteria(CreditNoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creditNumber = other.creditNumber == null ? null : other.creditNumber.copy();
        this.creditNoteDate = other.creditNoteDate == null ? null : other.creditNoteDate.copy();
        this.creditAmount = other.creditAmount == null ? null : other.creditAmount.copy();
        this.purchaseOrdersId = other.purchaseOrdersId == null ? null : other.purchaseOrdersId.copy();
        this.invoicesId = other.invoicesId == null ? null : other.invoicesId.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CreditNoteCriteria copy() {
        return new CreditNoteCriteria(this);
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

    public StringFilter getCreditNumber() {
        return creditNumber;
    }

    public StringFilter creditNumber() {
        if (creditNumber == null) {
            creditNumber = new StringFilter();
        }
        return creditNumber;
    }

    public void setCreditNumber(StringFilter creditNumber) {
        this.creditNumber = creditNumber;
    }

    public LocalDateFilter getCreditNoteDate() {
        return creditNoteDate;
    }

    public LocalDateFilter creditNoteDate() {
        if (creditNoteDate == null) {
            creditNoteDate = new LocalDateFilter();
        }
        return creditNoteDate;
    }

    public void setCreditNoteDate(LocalDateFilter creditNoteDate) {
        this.creditNoteDate = creditNoteDate;
    }

    public BigDecimalFilter getCreditAmount() {
        return creditAmount;
    }

    public BigDecimalFilter creditAmount() {
        if (creditAmount == null) {
            creditAmount = new BigDecimalFilter();
        }
        return creditAmount;
    }

    public void setCreditAmount(BigDecimalFilter creditAmount) {
        this.creditAmount = creditAmount;
    }

    public LongFilter getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public LongFilter purchaseOrdersId() {
        if (purchaseOrdersId == null) {
            purchaseOrdersId = new LongFilter();
        }
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(LongFilter purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public LongFilter getInvoicesId() {
        return invoicesId;
    }

    public LongFilter invoicesId() {
        if (invoicesId == null) {
            invoicesId = new LongFilter();
        }
        return invoicesId;
    }

    public void setInvoicesId(LongFilter invoicesId) {
        this.invoicesId = invoicesId;
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

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
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
        final CreditNoteCriteria that = (CreditNoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creditNumber, that.creditNumber) &&
            Objects.equals(creditNoteDate, that.creditNoteDate) &&
            Objects.equals(creditAmount, that.creditAmount) &&
            Objects.equals(purchaseOrdersId, that.purchaseOrdersId) &&
            Objects.equals(invoicesId, that.invoicesId) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creditNumber,
            creditNoteDate,
            creditAmount,
            purchaseOrdersId,
            invoicesId,
            paymentLabelId,
            placeholderId,
            settlementCurrencyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditNoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creditNumber != null ? "creditNumber=" + creditNumber + ", " : "") +
            (creditNoteDate != null ? "creditNoteDate=" + creditNoteDate + ", " : "") +
            (creditAmount != null ? "creditAmount=" + creditAmount + ", " : "") +
            (purchaseOrdersId != null ? "purchaseOrdersId=" + purchaseOrdersId + ", " : "") +
            (invoicesId != null ? "invoicesId=" + invoicesId + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
