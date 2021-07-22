package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.erp.domain.enumeration.taxReferenceTypes;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.TaxReference} entity. This class is used
 * in {@link io.github.erp.web.rest.TaxReferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tax-references?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaxReferenceCriteria implements Serializable, Criteria {
    /**
     * Class for filtering taxReferenceTypes
     */
    public static class taxReferenceTypesFilter extends Filter<taxReferenceTypes> {

        public taxReferenceTypesFilter() {
        }

        public taxReferenceTypesFilter(taxReferenceTypesFilter filter) {
            super(filter);
        }

        @Override
        public taxReferenceTypesFilter copy() {
            return new taxReferenceTypesFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter taxName;

    private StringFilter taxDescription;

    private DoubleFilter taxPercentage;

    private taxReferenceTypesFilter taxReferenceType;

    public TaxReferenceCriteria() {
    }

    public TaxReferenceCriteria(TaxReferenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taxName = other.taxName == null ? null : other.taxName.copy();
        this.taxDescription = other.taxDescription == null ? null : other.taxDescription.copy();
        this.taxPercentage = other.taxPercentage == null ? null : other.taxPercentage.copy();
        this.taxReferenceType = other.taxReferenceType == null ? null : other.taxReferenceType.copy();
    }

    @Override
    public TaxReferenceCriteria copy() {
        return new TaxReferenceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTaxName() {
        return taxName;
    }

    public void setTaxName(StringFilter taxName) {
        this.taxName = taxName;
    }

    public StringFilter getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(StringFilter taxDescription) {
        this.taxDescription = taxDescription;
    }

    public DoubleFilter getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(DoubleFilter taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public taxReferenceTypesFilter getTaxReferenceType() {
        return taxReferenceType;
    }

    public void setTaxReferenceType(taxReferenceTypesFilter taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaxReferenceCriteria that = (TaxReferenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taxName, that.taxName) &&
            Objects.equals(taxDescription, that.taxDescription) &&
            Objects.equals(taxPercentage, that.taxPercentage) &&
            Objects.equals(taxReferenceType, that.taxReferenceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        taxName,
        taxDescription,
        taxPercentage,
        taxReferenceType
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxReferenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taxName != null ? "taxName=" + taxName + ", " : "") +
                (taxDescription != null ? "taxDescription=" + taxDescription + ", " : "") +
                (taxPercentage != null ? "taxPercentage=" + taxPercentage + ", " : "") +
                (taxReferenceType != null ? "taxReferenceType=" + taxReferenceType + ", " : "") +
            "}";
    }

}
