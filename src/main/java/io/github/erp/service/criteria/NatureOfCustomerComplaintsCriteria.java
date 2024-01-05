package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
 * Criteria class for the {@link io.github.erp.domain.NatureOfCustomerComplaints} entity. This class is used
 * in {@link io.github.erp.web.rest.NatureOfCustomerComplaintsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nature-of-customer-complaints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NatureOfCustomerComplaintsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter natureOfComplaintTypeCode;

    private StringFilter natureOfComplaintType;

    private Boolean distinct;

    public NatureOfCustomerComplaintsCriteria() {}

    public NatureOfCustomerComplaintsCriteria(NatureOfCustomerComplaintsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.natureOfComplaintTypeCode = other.natureOfComplaintTypeCode == null ? null : other.natureOfComplaintTypeCode.copy();
        this.natureOfComplaintType = other.natureOfComplaintType == null ? null : other.natureOfComplaintType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NatureOfCustomerComplaintsCriteria copy() {
        return new NatureOfCustomerComplaintsCriteria(this);
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

    public StringFilter getNatureOfComplaintTypeCode() {
        return natureOfComplaintTypeCode;
    }

    public StringFilter natureOfComplaintTypeCode() {
        if (natureOfComplaintTypeCode == null) {
            natureOfComplaintTypeCode = new StringFilter();
        }
        return natureOfComplaintTypeCode;
    }

    public void setNatureOfComplaintTypeCode(StringFilter natureOfComplaintTypeCode) {
        this.natureOfComplaintTypeCode = natureOfComplaintTypeCode;
    }

    public StringFilter getNatureOfComplaintType() {
        return natureOfComplaintType;
    }

    public StringFilter natureOfComplaintType() {
        if (natureOfComplaintType == null) {
            natureOfComplaintType = new StringFilter();
        }
        return natureOfComplaintType;
    }

    public void setNatureOfComplaintType(StringFilter natureOfComplaintType) {
        this.natureOfComplaintType = natureOfComplaintType;
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
        final NatureOfCustomerComplaintsCriteria that = (NatureOfCustomerComplaintsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(natureOfComplaintTypeCode, that.natureOfComplaintTypeCode) &&
            Objects.equals(natureOfComplaintType, that.natureOfComplaintType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, natureOfComplaintTypeCode, natureOfComplaintType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NatureOfCustomerComplaintsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (natureOfComplaintTypeCode != null ? "natureOfComplaintTypeCode=" + natureOfComplaintTypeCode + ", " : "") +
            (natureOfComplaintType != null ? "natureOfComplaintType=" + natureOfComplaintType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
