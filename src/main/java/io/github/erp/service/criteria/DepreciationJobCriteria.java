package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.domain.enumeration.DepreciationJobStatusType;
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
 * Criteria class for the {@link io.github.erp.domain.DepreciationJob} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationJobStatusType
     */
    public static class DepreciationJobStatusTypeFilter extends Filter<DepreciationJobStatusType> {

        public DepreciationJobStatusTypeFilter() {}

        public DepreciationJobStatusTypeFilter(DepreciationJobStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationJobStatusTypeFilter copy() {
            return new DepreciationJobStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter timeOfCommencement;

    private DepreciationJobStatusTypeFilter depreciationJobStatus;

    private StringFilter description;

    private LongFilter createdById;

    private LongFilter depreciationPeriodId;

    private Boolean distinct;

    public DepreciationJobCriteria() {}

    public DepreciationJobCriteria(DepreciationJobCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeOfCommencement = other.timeOfCommencement == null ? null : other.timeOfCommencement.copy();
        this.depreciationJobStatus = other.depreciationJobStatus == null ? null : other.depreciationJobStatus.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.depreciationPeriodId = other.depreciationPeriodId == null ? null : other.depreciationPeriodId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationJobCriteria copy() {
        return new DepreciationJobCriteria(this);
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

    public ZonedDateTimeFilter getTimeOfCommencement() {
        return timeOfCommencement;
    }

    public ZonedDateTimeFilter timeOfCommencement() {
        if (timeOfCommencement == null) {
            timeOfCommencement = new ZonedDateTimeFilter();
        }
        return timeOfCommencement;
    }

    public void setTimeOfCommencement(ZonedDateTimeFilter timeOfCommencement) {
        this.timeOfCommencement = timeOfCommencement;
    }

    public DepreciationJobStatusTypeFilter getDepreciationJobStatus() {
        return depreciationJobStatus;
    }

    public DepreciationJobStatusTypeFilter depreciationJobStatus() {
        if (depreciationJobStatus == null) {
            depreciationJobStatus = new DepreciationJobStatusTypeFilter();
        }
        return depreciationJobStatus;
    }

    public void setDepreciationJobStatus(DepreciationJobStatusTypeFilter depreciationJobStatus) {
        this.depreciationJobStatus = depreciationJobStatus;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getDepreciationPeriodId() {
        return depreciationPeriodId;
    }

    public LongFilter depreciationPeriodId() {
        if (depreciationPeriodId == null) {
            depreciationPeriodId = new LongFilter();
        }
        return depreciationPeriodId;
    }

    public void setDepreciationPeriodId(LongFilter depreciationPeriodId) {
        this.depreciationPeriodId = depreciationPeriodId;
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
        final DepreciationJobCriteria that = (DepreciationJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeOfCommencement, that.timeOfCommencement) &&
            Objects.equals(depreciationJobStatus, that.depreciationJobStatus) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(depreciationPeriodId, that.depreciationPeriodId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfCommencement, depreciationJobStatus, description, createdById, depreciationPeriodId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJobCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (timeOfCommencement != null ? "timeOfCommencement=" + timeOfCommencement + ", " : "") +
            (depreciationJobStatus != null ? "depreciationJobStatus=" + depreciationJobStatus + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (depreciationPeriodId != null ? "depreciationPeriodId=" + depreciationPeriodId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
