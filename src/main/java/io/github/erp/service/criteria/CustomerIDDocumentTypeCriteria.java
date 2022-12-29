package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.CustomerIDDocumentType} entity. This class is used
 * in {@link io.github.erp.web.rest.CustomerIDDocumentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-id-document-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerIDDocumentTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentCode;

    private StringFilter documentType;

    private StringFilter documentTypeDescription;

    private LongFilter placeholderId;

    private Boolean distinct;

    public CustomerIDDocumentTypeCriteria() {}

    public CustomerIDDocumentTypeCriteria(CustomerIDDocumentTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.documentCode = other.documentCode == null ? null : other.documentCode.copy();
        this.documentType = other.documentType == null ? null : other.documentType.copy();
        this.documentTypeDescription = other.documentTypeDescription == null ? null : other.documentTypeDescription.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerIDDocumentTypeCriteria copy() {
        return new CustomerIDDocumentTypeCriteria(this);
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

    public StringFilter getDocumentCode() {
        return documentCode;
    }

    public StringFilter documentCode() {
        if (documentCode == null) {
            documentCode = new StringFilter();
        }
        return documentCode;
    }

    public void setDocumentCode(StringFilter documentCode) {
        this.documentCode = documentCode;
    }

    public StringFilter getDocumentType() {
        return documentType;
    }

    public StringFilter documentType() {
        if (documentType == null) {
            documentType = new StringFilter();
        }
        return documentType;
    }

    public void setDocumentType(StringFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getDocumentTypeDescription() {
        return documentTypeDescription;
    }

    public StringFilter documentTypeDescription() {
        if (documentTypeDescription == null) {
            documentTypeDescription = new StringFilter();
        }
        return documentTypeDescription;
    }

    public void setDocumentTypeDescription(StringFilter documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
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
        final CustomerIDDocumentTypeCriteria that = (CustomerIDDocumentTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentCode, that.documentCode) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(documentTypeDescription, that.documentTypeDescription) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentCode, documentType, documentTypeDescription, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerIDDocumentTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (documentCode != null ? "documentCode=" + documentCode + ", " : "") +
            (documentType != null ? "documentType=" + documentType + ", " : "") +
            (documentTypeDescription != null ? "documentTypeDescription=" + documentTypeDescription + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
