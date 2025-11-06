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
 * Criteria class for the {@link io.github.erp.domain.LeaseLiabilityCompilation} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseLiabilityCompilationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-liability-compilations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseLiabilityCompilationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter requestId;

    private ZonedDateTimeFilter timeOfRequest;

    private LongFilter requestedById;

    private BooleanFilter active;

    private Boolean distinct;

    public LeaseLiabilityCompilationCriteria() {}

    public LeaseLiabilityCompilationCriteria(LeaseLiabilityCompilationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.timeOfRequest = other.timeOfRequest == null ? null : other.timeOfRequest.copy();
        this.requestedById = other.requestedById == null ? null : other.requestedById.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseLiabilityCompilationCriteria copy() {
        return new LeaseLiabilityCompilationCriteria(this);
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

    public UUIDFilter getRequestId() {
        return requestId;
    }

    public UUIDFilter requestId() {
        if (requestId == null) {
            requestId = new UUIDFilter();
        }
        return requestId;
    }

    public void setRequestId(UUIDFilter requestId) {
        this.requestId = requestId;
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

    public LongFilter getRequestedById() {
        return requestedById;
    }

    public LongFilter requestedById() {
        if (requestedById == null) {
            requestedById = new LongFilter();
        }
        return requestedById;
    }

    public void setRequestedById(LongFilter requestedById) {
        this.requestedById = requestedById;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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
        final LeaseLiabilityCompilationCriteria that = (LeaseLiabilityCompilationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(timeOfRequest, that.timeOfRequest) &&
            Objects.equals(requestedById, that.requestedById) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestId, timeOfRequest, requestedById, active, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityCompilationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (timeOfRequest != null ? "timeOfRequest=" + timeOfRequest + ", " : "") +
            (requestedById != null ? "requestedById=" + requestedById + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
