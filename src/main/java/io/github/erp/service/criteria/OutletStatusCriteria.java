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
