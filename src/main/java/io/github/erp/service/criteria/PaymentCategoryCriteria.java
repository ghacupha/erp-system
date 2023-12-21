package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.CategoryTypes;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PaymentCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCategoryCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter categoryName;

    private StringFilter categoryDescription;

    private CategoryTypesFilter categoryType;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter paymentCalculationId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentCategoryCriteria() {}

    public PaymentCategoryCriteria(PaymentCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.categoryDescription = other.categoryDescription == null ? null : other.categoryDescription.copy();
        this.categoryType = other.categoryType == null ? null : other.categoryType.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.paymentCalculationId = other.paymentCalculationId == null ? null : other.paymentCalculationId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCategoryCriteria copy() {
        return new PaymentCategoryCriteria(this);
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

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            categoryName = new StringFilter();
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public StringFilter getCategoryDescription() {
        return categoryDescription;
    }

    public StringFilter categoryDescription() {
        if (categoryDescription == null) {
            categoryDescription = new StringFilter();
        }
        return categoryDescription;
    }

    public void setCategoryDescription(StringFilter categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public CategoryTypesFilter getCategoryType() {
        return categoryType;
    }

    public CategoryTypesFilter categoryType() {
        if (categoryType == null) {
            categoryType = new CategoryTypesFilter();
        }
        return categoryType;
    }

    public void setCategoryType(CategoryTypesFilter categoryType) {
        this.categoryType = categoryType;
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
        final PaymentCategoryCriteria that = (PaymentCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(categoryDescription, that.categoryDescription) &&
            Objects.equals(categoryType, that.categoryType) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(paymentCalculationId, that.paymentCalculationId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            categoryName,
            categoryDescription,
            categoryType,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            paymentCalculationId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
            (categoryDescription != null ? "categoryDescription=" + categoryDescription + ", " : "") +
            (categoryType != null ? "categoryType=" + categoryType + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (paymentCalculationId != null ? "paymentCalculationId=" + paymentCalculationId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
