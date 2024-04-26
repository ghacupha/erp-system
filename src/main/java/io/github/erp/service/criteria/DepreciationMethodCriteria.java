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
import io.github.erp.domain.enumeration.DepreciationTypes;
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
 * Criteria class for the {@link io.github.erp.domain.DepreciationMethod} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationMethodCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationTypes
     */
    public static class DepreciationTypesFilter extends Filter<DepreciationTypes> {

        public DepreciationTypesFilter() {}

        public DepreciationTypesFilter(DepreciationTypesFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationTypesFilter copy() {
            return new DepreciationTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter depreciationMethodName;

    private StringFilter description;

    private DepreciationTypesFilter depreciationType;

    private LongFilter placeholderId;

    private Boolean distinct;

    public DepreciationMethodCriteria() {}

    public DepreciationMethodCriteria(DepreciationMethodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.depreciationMethodName = other.depreciationMethodName == null ? null : other.depreciationMethodName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.depreciationType = other.depreciationType == null ? null : other.depreciationType.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationMethodCriteria copy() {
        return new DepreciationMethodCriteria(this);
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

    public StringFilter getDepreciationMethodName() {
        return depreciationMethodName;
    }

    public StringFilter depreciationMethodName() {
        if (depreciationMethodName == null) {
            depreciationMethodName = new StringFilter();
        }
        return depreciationMethodName;
    }

    public void setDepreciationMethodName(StringFilter depreciationMethodName) {
        this.depreciationMethodName = depreciationMethodName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DepreciationTypesFilter getDepreciationType() {
        return depreciationType;
    }

    public DepreciationTypesFilter depreciationType() {
        if (depreciationType == null) {
            depreciationType = new DepreciationTypesFilter();
        }
        return depreciationType;
    }

    public void setDepreciationType(DepreciationTypesFilter depreciationType) {
        this.depreciationType = depreciationType;
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
        final DepreciationMethodCriteria that = (DepreciationMethodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(depreciationMethodName, that.depreciationMethodName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(depreciationType, that.depreciationType) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, depreciationMethodName, description, depreciationType, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationMethodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (depreciationMethodName != null ? "depreciationMethodName=" + depreciationMethodName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (depreciationType != null ? "depreciationType=" + depreciationType + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
