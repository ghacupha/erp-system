package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.CrbComplaintStatusType} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbComplaintStatusTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-complaint-status-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbComplaintStatusTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter complaintStatusTypeCode;

    private StringFilter complaintStatusType;

    private Boolean distinct;

    public CrbComplaintStatusTypeCriteria() {}

    public CrbComplaintStatusTypeCriteria(CrbComplaintStatusTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.complaintStatusTypeCode = other.complaintStatusTypeCode == null ? null : other.complaintStatusTypeCode.copy();
        this.complaintStatusType = other.complaintStatusType == null ? null : other.complaintStatusType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbComplaintStatusTypeCriteria copy() {
        return new CrbComplaintStatusTypeCriteria(this);
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

    public StringFilter getComplaintStatusTypeCode() {
        return complaintStatusTypeCode;
    }

    public StringFilter complaintStatusTypeCode() {
        if (complaintStatusTypeCode == null) {
            complaintStatusTypeCode = new StringFilter();
        }
        return complaintStatusTypeCode;
    }

    public void setComplaintStatusTypeCode(StringFilter complaintStatusTypeCode) {
        this.complaintStatusTypeCode = complaintStatusTypeCode;
    }

    public StringFilter getComplaintStatusType() {
        return complaintStatusType;
    }

    public StringFilter complaintStatusType() {
        if (complaintStatusType == null) {
            complaintStatusType = new StringFilter();
        }
        return complaintStatusType;
    }

    public void setComplaintStatusType(StringFilter complaintStatusType) {
        this.complaintStatusType = complaintStatusType;
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
        final CrbComplaintStatusTypeCriteria that = (CrbComplaintStatusTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(complaintStatusTypeCode, that.complaintStatusTypeCode) &&
            Objects.equals(complaintStatusType, that.complaintStatusType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, complaintStatusTypeCode, complaintStatusType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintStatusTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (complaintStatusTypeCode != null ? "complaintStatusTypeCode=" + complaintStatusTypeCode + ", " : "") +
            (complaintStatusType != null ? "complaintStatusType=" + complaintStatusType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
