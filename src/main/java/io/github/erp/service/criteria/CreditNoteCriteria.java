package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
