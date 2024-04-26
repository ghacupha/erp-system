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
