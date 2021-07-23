package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.erp.domain.enumeration.CategoryTypes;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

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

        public CategoryTypesFilter() {
        }

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

    public PaymentCategoryCriteria() {
    }

    public PaymentCategoryCriteria(PaymentCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.categoryDescription = other.categoryDescription == null ? null : other.categoryDescription.copy();
        this.categoryType = other.categoryType == null ? null : other.categoryType.copy();
    }

    @Override
    public PaymentCategoryCriteria copy() {
        return new PaymentCategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public StringFilter getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(StringFilter categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public CategoryTypesFilter getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryTypesFilter categoryType) {
        this.categoryType = categoryType;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(categoryDescription, that.categoryDescription) &&
            Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        categoryName,
        categoryDescription,
        categoryType
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
            "}";
    }

}
