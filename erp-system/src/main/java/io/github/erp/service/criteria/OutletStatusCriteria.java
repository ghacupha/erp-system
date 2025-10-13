package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.enumeration.BranchStatusType;
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
 * Criteria class for the {@link io.github.erp.domain.OutletStatus} entity. This class is used
 * in {@link io.github.erp.web.rest.OutletStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /outlet-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OutletStatusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BranchStatusType
     */
    public static class BranchStatusTypeFilter extends Filter<BranchStatusType> {

        public BranchStatusTypeFilter() {}

        public BranchStatusTypeFilter(BranchStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public BranchStatusTypeFilter copy() {
            return new BranchStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchStatusTypeCode;

    private BranchStatusTypeFilter branchStatusType;

    private StringFilter branchStatusTypeDescription;

    private LongFilter placeholderId;

    private Boolean distinct;

    public OutletStatusCriteria() {}

    public OutletStatusCriteria(OutletStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.branchStatusTypeCode = other.branchStatusTypeCode == null ? null : other.branchStatusTypeCode.copy();
        this.branchStatusType = other.branchStatusType == null ? null : other.branchStatusType.copy();
        this.branchStatusTypeDescription = other.branchStatusTypeDescription == null ? null : other.branchStatusTypeDescription.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OutletStatusCriteria copy() {
        return new OutletStatusCriteria(this);
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

    public StringFilter getBranchStatusTypeCode() {
        return branchStatusTypeCode;
    }

    public StringFilter branchStatusTypeCode() {
        if (branchStatusTypeCode == null) {
            branchStatusTypeCode = new StringFilter();
        }
        return branchStatusTypeCode;
    }

    public void setBranchStatusTypeCode(StringFilter branchStatusTypeCode) {
        this.branchStatusTypeCode = branchStatusTypeCode;
    }

    public BranchStatusTypeFilter getBranchStatusType() {
        return branchStatusType;
    }

    public BranchStatusTypeFilter branchStatusType() {
        if (branchStatusType == null) {
            branchStatusType = new BranchStatusTypeFilter();
        }
        return branchStatusType;
    }

    public void setBranchStatusType(BranchStatusTypeFilter branchStatusType) {
        this.branchStatusType = branchStatusType;
    }

    public StringFilter getBranchStatusTypeDescription() {
        return branchStatusTypeDescription;
    }

    public StringFilter branchStatusTypeDescription() {
        if (branchStatusTypeDescription == null) {
            branchStatusTypeDescription = new StringFilter();
        }
        return branchStatusTypeDescription;
    }

    public void setBranchStatusTypeDescription(StringFilter branchStatusTypeDescription) {
        this.branchStatusTypeDescription = branchStatusTypeDescription;
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
        final OutletStatusCriteria that = (OutletStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchStatusTypeCode, that.branchStatusTypeCode) &&
            Objects.equals(branchStatusType, that.branchStatusType) &&
            Objects.equals(branchStatusTypeDescription, that.branchStatusTypeDescription) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, branchStatusTypeCode, branchStatusType, branchStatusTypeDescription, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (branchStatusTypeCode != null ? "branchStatusTypeCode=" + branchStatusTypeCode + ", " : "") +
            (branchStatusType != null ? "branchStatusType=" + branchStatusType + ", " : "") +
            (branchStatusTypeDescription != null ? "branchStatusTypeDescription=" + branchStatusTypeDescription + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
