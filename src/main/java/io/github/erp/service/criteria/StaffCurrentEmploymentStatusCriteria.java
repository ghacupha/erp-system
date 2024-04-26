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
 * Criteria class for the {@link io.github.erp.domain.StaffCurrentEmploymentStatus} entity. This class is used
 * in {@link io.github.erp.web.rest.StaffCurrentEmploymentStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /staff-current-employment-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StaffCurrentEmploymentStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter staffCurrentEmploymentStatusTypeCode;

    private StringFilter staffCurrentEmploymentStatusType;

    private Boolean distinct;

    public StaffCurrentEmploymentStatusCriteria() {}

    public StaffCurrentEmploymentStatusCriteria(StaffCurrentEmploymentStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.staffCurrentEmploymentStatusTypeCode =
            other.staffCurrentEmploymentStatusTypeCode == null ? null : other.staffCurrentEmploymentStatusTypeCode.copy();
        this.staffCurrentEmploymentStatusType =
            other.staffCurrentEmploymentStatusType == null ? null : other.staffCurrentEmploymentStatusType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StaffCurrentEmploymentStatusCriteria copy() {
        return new StaffCurrentEmploymentStatusCriteria(this);
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

    public StringFilter getStaffCurrentEmploymentStatusTypeCode() {
        return staffCurrentEmploymentStatusTypeCode;
    }

    public StringFilter staffCurrentEmploymentStatusTypeCode() {
        if (staffCurrentEmploymentStatusTypeCode == null) {
            staffCurrentEmploymentStatusTypeCode = new StringFilter();
        }
        return staffCurrentEmploymentStatusTypeCode;
    }

    public void setStaffCurrentEmploymentStatusTypeCode(StringFilter staffCurrentEmploymentStatusTypeCode) {
        this.staffCurrentEmploymentStatusTypeCode = staffCurrentEmploymentStatusTypeCode;
    }

    public StringFilter getStaffCurrentEmploymentStatusType() {
        return staffCurrentEmploymentStatusType;
    }

    public StringFilter staffCurrentEmploymentStatusType() {
        if (staffCurrentEmploymentStatusType == null) {
            staffCurrentEmploymentStatusType = new StringFilter();
        }
        return staffCurrentEmploymentStatusType;
    }

    public void setStaffCurrentEmploymentStatusType(StringFilter staffCurrentEmploymentStatusType) {
        this.staffCurrentEmploymentStatusType = staffCurrentEmploymentStatusType;
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
        final StaffCurrentEmploymentStatusCriteria that = (StaffCurrentEmploymentStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(staffCurrentEmploymentStatusTypeCode, that.staffCurrentEmploymentStatusTypeCode) &&
            Objects.equals(staffCurrentEmploymentStatusType, that.staffCurrentEmploymentStatusType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, staffCurrentEmploymentStatusTypeCode, staffCurrentEmploymentStatusType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffCurrentEmploymentStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (staffCurrentEmploymentStatusTypeCode != null ? "staffCurrentEmploymentStatusTypeCode=" + staffCurrentEmploymentStatusTypeCode + ", " : "") +
            (staffCurrentEmploymentStatusType != null ? "staffCurrentEmploymentStatusType=" + staffCurrentEmploymentStatusType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
