package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
 * Criteria class for the {@link io.github.erp.domain.OutletType} entity. This class is used
 * in {@link io.github.erp.web.rest.OutletTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /outlet-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OutletTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter outletTypeCode;

    private StringFilter outletType;

    private StringFilter outletTypeDetails;

    private LongFilter placeholderId;

    private Boolean distinct;

    public OutletTypeCriteria() {}

    public OutletTypeCriteria(OutletTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.outletTypeCode = other.outletTypeCode == null ? null : other.outletTypeCode.copy();
        this.outletType = other.outletType == null ? null : other.outletType.copy();
        this.outletTypeDetails = other.outletTypeDetails == null ? null : other.outletTypeDetails.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OutletTypeCriteria copy() {
        return new OutletTypeCriteria(this);
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

    public StringFilter getOutletTypeCode() {
        return outletTypeCode;
    }

    public StringFilter outletTypeCode() {
        if (outletTypeCode == null) {
            outletTypeCode = new StringFilter();
        }
        return outletTypeCode;
    }

    public void setOutletTypeCode(StringFilter outletTypeCode) {
        this.outletTypeCode = outletTypeCode;
    }

    public StringFilter getOutletType() {
        return outletType;
    }

    public StringFilter outletType() {
        if (outletType == null) {
            outletType = new StringFilter();
        }
        return outletType;
    }

    public void setOutletType(StringFilter outletType) {
        this.outletType = outletType;
    }

    public StringFilter getOutletTypeDetails() {
        return outletTypeDetails;
    }

    public StringFilter outletTypeDetails() {
        if (outletTypeDetails == null) {
            outletTypeDetails = new StringFilter();
        }
        return outletTypeDetails;
    }

    public void setOutletTypeDetails(StringFilter outletTypeDetails) {
        this.outletTypeDetails = outletTypeDetails;
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
        final OutletTypeCriteria that = (OutletTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(outletTypeCode, that.outletTypeCode) &&
            Objects.equals(outletType, that.outletType) &&
            Objects.equals(outletTypeDetails, that.outletTypeDetails) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outletTypeCode, outletType, outletTypeDetails, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (outletTypeCode != null ? "outletTypeCode=" + outletTypeCode + ", " : "") +
            (outletType != null ? "outletType=" + outletType + ", " : "") +
            (outletTypeDetails != null ? "outletTypeDetails=" + outletTypeDetails + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
