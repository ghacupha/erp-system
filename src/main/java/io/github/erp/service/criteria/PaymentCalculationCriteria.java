package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

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

    private BigDecimalFilter paymentExpense;

    private BigDecimalFilter withholdingVAT;

    private BigDecimalFilter withholdingTax;

    private BigDecimalFilter paymentAmount;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter paymentCategoryId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentCalculationCriteria() {}

    public PaymentCalculationCriteria(PaymentCalculationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentExpense = other.paymentExpense == null ? null : other.paymentExpense.copy();
        this.withholdingVAT = other.withholdingVAT == null ? null : other.withholdingVAT.copy();
        this.withholdingTax = other.withholdingTax == null ? null : other.withholdingTax.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCalculationCriteria copy() {
        return new PaymentCalculationCriteria(this);
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

    public BigDecimalFilter getPaymentExpense() {
        return paymentExpense;
    }

    public BigDecimalFilter paymentExpense() {
        if (paymentExpense == null) {
            paymentExpense = new BigDecimalFilter();
        }
        return paymentExpense;
    }

    public void setPaymentExpense(BigDecimalFilter paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimalFilter getWithholdingVAT() {
        return withholdingVAT;
    }

    public BigDecimalFilter withholdingVAT() {
        if (withholdingVAT == null) {
            withholdingVAT = new BigDecimalFilter();
        }
        return withholdingVAT;
    }

    public void setWithholdingVAT(BigDecimalFilter withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimalFilter getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimalFilter withholdingTax() {
        if (withholdingTax == null) {
            withholdingTax = new BigDecimalFilter();
        }
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimalFilter withholdingTax) {
        this.withholdingTax = withholdingTax;
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
        final PaymentCalculationCriteria that = (PaymentCalculationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentExpense, that.paymentExpense) &&
            Objects.equals(withholdingVAT, that.withholdingVAT) &&
            Objects.equals(withholdingTax, that.withholdingTax) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentExpense,
            withholdingVAT,
            withholdingTax,
            paymentAmount,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            paymentCategoryId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentExpense != null ? "paymentExpense=" + paymentExpense + ", " : "") +
            (withholdingVAT != null ? "withholdingVAT=" + withholdingVAT + ", " : "") +
            (withholdingTax != null ? "withholdingTax=" + withholdingTax + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
