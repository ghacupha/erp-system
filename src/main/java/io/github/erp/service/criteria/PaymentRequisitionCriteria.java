package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 11 (Caleb Series) Server ver 0.7.0
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

    private LocalDateFilter receptionDate;

    private StringFilter dealerName;

    private StringFilter briefDescription;

    private StringFilter requisitionNumber;

    private BigDecimalFilter invoicedAmount;

    private BigDecimalFilter disbursementCost;

    private BigDecimalFilter taxableAmount;

    private BooleanFilter requisitionProcessed;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentRequisitionCriteria() {}

    public PaymentRequisitionCriteria(PaymentRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.receptionDate = other.receptionDate == null ? null : other.receptionDate.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.briefDescription = other.briefDescription == null ? null : other.briefDescription.copy();
        this.requisitionNumber = other.requisitionNumber == null ? null : other.requisitionNumber.copy();
        this.invoicedAmount = other.invoicedAmount == null ? null : other.invoicedAmount.copy();
        this.disbursementCost = other.disbursementCost == null ? null : other.disbursementCost.copy();
        this.taxableAmount = other.taxableAmount == null ? null : other.taxableAmount.copy();
        this.requisitionProcessed = other.requisitionProcessed == null ? null : other.requisitionProcessed.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentRequisitionCriteria copy() {
        return new PaymentRequisitionCriteria(this);
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

    public LocalDateFilter getReceptionDate() {
        return receptionDate;
    }

    public LocalDateFilter receptionDate() {
        if (receptionDate == null) {
            receptionDate = new LocalDateFilter();
        }
        return receptionDate;
    }

    public void setReceptionDate(LocalDateFilter receptionDate) {
        this.receptionDate = receptionDate;
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

    public StringFilter getBriefDescription() {
        return briefDescription;
    }

    public StringFilter briefDescription() {
        if (briefDescription == null) {
            briefDescription = new StringFilter();
        }
        return briefDescription;
    }

    public void setBriefDescription(StringFilter briefDescription) {
        this.briefDescription = briefDescription;
    }

    public StringFilter getRequisitionNumber() {
        return requisitionNumber;
    }

    public StringFilter requisitionNumber() {
        if (requisitionNumber == null) {
            requisitionNumber = new StringFilter();
        }
        return requisitionNumber;
    }

    public void setRequisitionNumber(StringFilter requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
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

    public BigDecimalFilter getDisbursementCost() {
        return disbursementCost;
    }

    public BigDecimalFilter disbursementCost() {
        if (disbursementCost == null) {
            disbursementCost = new BigDecimalFilter();
        }
        return disbursementCost;
    }

    public void setDisbursementCost(BigDecimalFilter disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimalFilter getTaxableAmount() {
        return taxableAmount;
    }

    public BigDecimalFilter taxableAmount() {
        if (taxableAmount == null) {
            taxableAmount = new BigDecimalFilter();
        }
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimalFilter taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BooleanFilter getRequisitionProcessed() {
        return requisitionProcessed;
    }

    public BooleanFilter requisitionProcessed() {
        if (requisitionProcessed == null) {
            requisitionProcessed = new BooleanFilter();
        }
        return requisitionProcessed;
    }

    public void setRequisitionProcessed(BooleanFilter requisitionProcessed) {
        this.requisitionProcessed = requisitionProcessed;
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
        final PaymentRequisitionCriteria that = (PaymentRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(receptionDate, that.receptionDate) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(briefDescription, that.briefDescription) &&
            Objects.equals(requisitionNumber, that.requisitionNumber) &&
            Objects.equals(invoicedAmount, that.invoicedAmount) &&
            Objects.equals(disbursementCost, that.disbursementCost) &&
            Objects.equals(taxableAmount, that.taxableAmount) &&
            Objects.equals(requisitionProcessed, that.requisitionProcessed) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            receptionDate,
            dealerName,
            briefDescription,
            requisitionNumber,
            invoicedAmount,
            disbursementCost,
            taxableAmount,
            requisitionProcessed,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (receptionDate != null ? "receptionDate=" + receptionDate + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (briefDescription != null ? "briefDescription=" + briefDescription + ", " : "") +
            (requisitionNumber != null ? "requisitionNumber=" + requisitionNumber + ", " : "") +
            (invoicedAmount != null ? "invoicedAmount=" + invoicedAmount + ", " : "") +
            (disbursementCost != null ? "disbursementCost=" + disbursementCost + ", " : "") +
            (taxableAmount != null ? "taxableAmount=" + taxableAmount + ", " : "") +
            (requisitionProcessed != null ? "requisitionProcessed=" + requisitionProcessed + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
