package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
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
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouDepreciationRequest} entity. This class is used
 * in {@link io.github.erp.web.rest.RouDepreciationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-depreciation-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouDepreciationRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering depreciationProcessStatusTypes
     */
    public static class depreciationProcessStatusTypesFilter extends Filter<depreciationProcessStatusTypes> {

        public depreciationProcessStatusTypesFilter() {}

        public depreciationProcessStatusTypesFilter(depreciationProcessStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public depreciationProcessStatusTypesFilter copy() {
            return new depreciationProcessStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter requisitionId;

    private ZonedDateTimeFilter timeOfRequest;

    private depreciationProcessStatusTypesFilter depreciationProcessStatus;

    private IntegerFilter numberOfEnumeratedItems;

    private LongFilter initiatedById;

    private Boolean distinct;

    public RouDepreciationRequestCriteria() {}

    public RouDepreciationRequestCriteria(RouDepreciationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requisitionId = other.requisitionId == null ? null : other.requisitionId.copy();
        this.timeOfRequest = other.timeOfRequest == null ? null : other.timeOfRequest.copy();
        this.depreciationProcessStatus = other.depreciationProcessStatus == null ? null : other.depreciationProcessStatus.copy();
        this.numberOfEnumeratedItems = other.numberOfEnumeratedItems == null ? null : other.numberOfEnumeratedItems.copy();
        this.initiatedById = other.initiatedById == null ? null : other.initiatedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouDepreciationRequestCriteria copy() {
        return new RouDepreciationRequestCriteria(this);
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

    public UUIDFilter getRequisitionId() {
        return requisitionId;
    }

    public UUIDFilter requisitionId() {
        if (requisitionId == null) {
            requisitionId = new UUIDFilter();
        }
        return requisitionId;
    }

    public void setRequisitionId(UUIDFilter requisitionId) {
        this.requisitionId = requisitionId;
    }

    public ZonedDateTimeFilter getTimeOfRequest() {
        return timeOfRequest;
    }

    public ZonedDateTimeFilter timeOfRequest() {
        if (timeOfRequest == null) {
            timeOfRequest = new ZonedDateTimeFilter();
        }
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTimeFilter timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public depreciationProcessStatusTypesFilter getDepreciationProcessStatus() {
        return depreciationProcessStatus;
    }

    public depreciationProcessStatusTypesFilter depreciationProcessStatus() {
        if (depreciationProcessStatus == null) {
            depreciationProcessStatus = new depreciationProcessStatusTypesFilter();
        }
        return depreciationProcessStatus;
    }

    public void setDepreciationProcessStatus(depreciationProcessStatusTypesFilter depreciationProcessStatus) {
        this.depreciationProcessStatus = depreciationProcessStatus;
    }

    public IntegerFilter getNumberOfEnumeratedItems() {
        return numberOfEnumeratedItems;
    }

    public IntegerFilter numberOfEnumeratedItems() {
        if (numberOfEnumeratedItems == null) {
            numberOfEnumeratedItems = new IntegerFilter();
        }
        return numberOfEnumeratedItems;
    }

    public void setNumberOfEnumeratedItems(IntegerFilter numberOfEnumeratedItems) {
        this.numberOfEnumeratedItems = numberOfEnumeratedItems;
    }

    public LongFilter getInitiatedById() {
        return initiatedById;
    }

    public LongFilter initiatedById() {
        if (initiatedById == null) {
            initiatedById = new LongFilter();
        }
        return initiatedById;
    }

    public void setInitiatedById(LongFilter initiatedById) {
        this.initiatedById = initiatedById;
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
        final RouDepreciationRequestCriteria that = (RouDepreciationRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requisitionId, that.requisitionId) &&
            Objects.equals(timeOfRequest, that.timeOfRequest) &&
            Objects.equals(depreciationProcessStatus, that.depreciationProcessStatus) &&
            Objects.equals(numberOfEnumeratedItems, that.numberOfEnumeratedItems) &&
            Objects.equals(initiatedById, that.initiatedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requisitionId, timeOfRequest, depreciationProcessStatus, numberOfEnumeratedItems, initiatedById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requisitionId != null ? "requisitionId=" + requisitionId + ", " : "") +
            (timeOfRequest != null ? "timeOfRequest=" + timeOfRequest + ", " : "") +
            (depreciationProcessStatus != null ? "depreciationProcessStatus=" + depreciationProcessStatus + ", " : "") +
            (numberOfEnumeratedItems != null ? "numberOfEnumeratedItems=" + numberOfEnumeratedItems + ", " : "") +
            (initiatedById != null ? "initiatedById=" + initiatedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
