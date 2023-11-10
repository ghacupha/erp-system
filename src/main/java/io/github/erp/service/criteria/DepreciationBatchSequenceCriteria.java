package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationBatchSequence} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationBatchSequenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-batch-sequences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationBatchSequenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationBatchStatusType
     */
    public static class DepreciationBatchStatusTypeFilter extends Filter<DepreciationBatchStatusType> {

        public DepreciationBatchStatusTypeFilter() {}

        public DepreciationBatchStatusTypeFilter(DepreciationBatchStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationBatchStatusTypeFilter copy() {
            return new DepreciationBatchStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter startIndex;

    private IntegerFilter endIndex;

    private ZonedDateTimeFilter createdAt;

    private DepreciationBatchStatusTypeFilter depreciationBatchStatus;

    private LongFilter depreciationJobId;

    private Boolean distinct;

    public DepreciationBatchSequenceCriteria() {}

    public DepreciationBatchSequenceCriteria(DepreciationBatchSequenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startIndex = other.startIndex == null ? null : other.startIndex.copy();
        this.endIndex = other.endIndex == null ? null : other.endIndex.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.depreciationBatchStatus = other.depreciationBatchStatus == null ? null : other.depreciationBatchStatus.copy();
        this.depreciationJobId = other.depreciationJobId == null ? null : other.depreciationJobId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationBatchSequenceCriteria copy() {
        return new DepreciationBatchSequenceCriteria(this);
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

    public IntegerFilter getStartIndex() {
        return startIndex;
    }

    public IntegerFilter startIndex() {
        if (startIndex == null) {
            startIndex = new IntegerFilter();
        }
        return startIndex;
    }

    public void setStartIndex(IntegerFilter startIndex) {
        this.startIndex = startIndex;
    }

    public IntegerFilter getEndIndex() {
        return endIndex;
    }

    public IntegerFilter endIndex() {
        if (endIndex == null) {
            endIndex = new IntegerFilter();
        }
        return endIndex;
    }

    public void setEndIndex(IntegerFilter endIndex) {
        this.endIndex = endIndex;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public DepreciationBatchStatusTypeFilter getDepreciationBatchStatus() {
        return depreciationBatchStatus;
    }

    public DepreciationBatchStatusTypeFilter depreciationBatchStatus() {
        if (depreciationBatchStatus == null) {
            depreciationBatchStatus = new DepreciationBatchStatusTypeFilter();
        }
        return depreciationBatchStatus;
    }

    public void setDepreciationBatchStatus(DepreciationBatchStatusTypeFilter depreciationBatchStatus) {
        this.depreciationBatchStatus = depreciationBatchStatus;
    }

    public LongFilter getDepreciationJobId() {
        return depreciationJobId;
    }

    public LongFilter depreciationJobId() {
        if (depreciationJobId == null) {
            depreciationJobId = new LongFilter();
        }
        return depreciationJobId;
    }

    public void setDepreciationJobId(LongFilter depreciationJobId) {
        this.depreciationJobId = depreciationJobId;
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
        final DepreciationBatchSequenceCriteria that = (DepreciationBatchSequenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startIndex, that.startIndex) &&
            Objects.equals(endIndex, that.endIndex) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(depreciationBatchStatus, that.depreciationBatchStatus) &&
            Objects.equals(depreciationJobId, that.depreciationJobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startIndex, endIndex, createdAt, depreciationBatchStatus, depreciationJobId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationBatchSequenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startIndex != null ? "startIndex=" + startIndex + ", " : "") +
            (endIndex != null ? "endIndex=" + endIndex + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (depreciationBatchStatus != null ? "depreciationBatchStatus=" + depreciationBatchStatus + ", " : "") +
            (depreciationJobId != null ? "depreciationJobId=" + depreciationJobId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
