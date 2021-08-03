package io.github.erp.service.criteria;

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

    private LongFilter paymentId;

    private LongFilter paymentCalculationId;

    public PaymentCategoryCriteria() {}

    public PaymentCategoryCriteria(PaymentCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.categoryDescription = other.categoryDescription == null ? null : other.categoryDescription.copy();
        this.categoryType = other.categoryType == null ? null : other.categoryType.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.paymentCalculationId = other.paymentCalculationId == null ? null : other.paymentCalculationId.copy();
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

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
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
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(paymentCalculationId, that.paymentCalculationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryName, categoryDescription, categoryType, paymentId, paymentCalculationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
            (categoryDescription != null ? "categoryDescription=" + categoryDescription + ", " : "") +
            (categoryType != null ? "categoryType=" + categoryType + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (paymentCalculationId != null ? "paymentCalculationId=" + paymentCalculationId + ", " : "") +
            "}";
    }
}
