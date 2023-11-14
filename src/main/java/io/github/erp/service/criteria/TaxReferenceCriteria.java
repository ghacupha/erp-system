package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.domain.enumeration.taxReferenceTypes;
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

        public taxReferenceTypesFilter() {}

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

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TaxReferenceCriteria() {}

    public TaxReferenceCriteria(TaxReferenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taxName = other.taxName == null ? null : other.taxName.copy();
        this.taxDescription = other.taxDescription == null ? null : other.taxDescription.copy();
        this.taxPercentage = other.taxPercentage == null ? null : other.taxPercentage.copy();
        this.taxReferenceType = other.taxReferenceType == null ? null : other.taxReferenceType.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaxReferenceCriteria copy() {
        return new TaxReferenceCriteria(this);
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

    public StringFilter getTaxName() {
        return taxName;
    }

    public StringFilter taxName() {
        if (taxName == null) {
            taxName = new StringFilter();
        }
        return taxName;
    }

    public void setTaxName(StringFilter taxName) {
        this.taxName = taxName;
    }

    public StringFilter getTaxDescription() {
        return taxDescription;
    }

    public StringFilter taxDescription() {
        if (taxDescription == null) {
            taxDescription = new StringFilter();
        }
        return taxDescription;
    }

    public void setTaxDescription(StringFilter taxDescription) {
        this.taxDescription = taxDescription;
    }

    public DoubleFilter getTaxPercentage() {
        return taxPercentage;
    }

    public DoubleFilter taxPercentage() {
        if (taxPercentage == null) {
            taxPercentage = new DoubleFilter();
        }
        return taxPercentage;
    }

    public void setTaxPercentage(DoubleFilter taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public taxReferenceTypesFilter getTaxReferenceType() {
        return taxReferenceType;
    }

    public taxReferenceTypesFilter taxReferenceType() {
        if (taxReferenceType == null) {
            taxReferenceType = new taxReferenceTypesFilter();
        }
        return taxReferenceType;
    }

    public void setTaxReferenceType(taxReferenceTypesFilter taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
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
        final TaxReferenceCriteria that = (TaxReferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(taxName, that.taxName) &&
            Objects.equals(taxDescription, that.taxDescription) &&
            Objects.equals(taxPercentage, that.taxPercentage) &&
            Objects.equals(taxReferenceType, that.taxReferenceType) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            taxName,
            taxDescription,
            taxPercentage,
            taxReferenceType,
            fileUploadToken,
            compilationToken,
            placeholderId,
            distinct
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
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
