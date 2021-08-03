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

    private LongFilter requisitionId;

    public PaymentRequisitionCriteria() {
    }

    public PaymentRequisitionCriteria(PaymentRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.invoicedAmount = other.invoicedAmount == null ? null : other.invoicedAmount.copy();
        this.disbursementCost = other.disbursementCost == null ? null : other.disbursementCost.copy();
        this.vatableAmount = other.vatableAmount == null ? null : other.vatableAmount.copy();
        this.requisitionId = other.requisitionId == null ? null : other.requisitionId.copy();
    }

    @Override
    public PaymentRequisitionCriteria copy() {
        return new PaymentRequisitionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDealerName() {
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public BigDecimalFilter getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimalFilter invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimalFilter getDisbursementCost() {
        return disbursementCost;
    }

    public void setDisbursementCost(BigDecimalFilter disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimalFilter getVatableAmount() {
        return vatableAmount;
    }

    public void setVatableAmount(BigDecimalFilter vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public LongFilter getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(LongFilter requisitionId) {
        this.requisitionId = requisitionId;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(invoicedAmount, that.invoicedAmount) &&
            Objects.equals(disbursementCost, that.disbursementCost) &&
            Objects.equals(vatableAmount, that.vatableAmount) &&
            Objects.equals(requisitionId, that.requisitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dealerName,
        invoicedAmount,
        disbursementCost,
        vatableAmount,
        requisitionId
        );
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
                (requisitionId != null ? "requisitionId=" + requisitionId + ", " : "") +
            "}";
    }

}
