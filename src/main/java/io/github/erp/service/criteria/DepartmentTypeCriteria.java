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
 * Criteria class for the {@link io.github.erp.domain.DepartmentType} entity. This class is used
 * in {@link io.github.erp.web.rest.DepartmentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /department-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepartmentTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter departmentTypeCode;

    private StringFilter departmentType;

    private LongFilter placeholderId;

    private Boolean distinct;

    public DepartmentTypeCriteria() {}

    public DepartmentTypeCriteria(DepartmentTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.departmentTypeCode = other.departmentTypeCode == null ? null : other.departmentTypeCode.copy();
        this.departmentType = other.departmentType == null ? null : other.departmentType.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepartmentTypeCriteria copy() {
        return new DepartmentTypeCriteria(this);
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

    public StringFilter getDepartmentTypeCode() {
        return departmentTypeCode;
    }

    public StringFilter departmentTypeCode() {
        if (departmentTypeCode == null) {
            departmentTypeCode = new StringFilter();
        }
        return departmentTypeCode;
    }

    public void setDepartmentTypeCode(StringFilter departmentTypeCode) {
        this.departmentTypeCode = departmentTypeCode;
    }

    public StringFilter getDepartmentType() {
        return departmentType;
    }

    public StringFilter departmentType() {
        if (departmentType == null) {
            departmentType = new StringFilter();
        }
        return departmentType;
    }

    public void setDepartmentType(StringFilter departmentType) {
        this.departmentType = departmentType;
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
        final DepartmentTypeCriteria that = (DepartmentTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(departmentTypeCode, that.departmentTypeCode) &&
            Objects.equals(departmentType, that.departmentType) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentTypeCode, departmentType, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (departmentTypeCode != null ? "departmentTypeCode=" + departmentTypeCode + ", " : "") +
            (departmentType != null ? "departmentType=" + departmentType + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
