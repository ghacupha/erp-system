package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.enumeration.CompilationStatusTypes;
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
 * Criteria class for the {@link io.github.erp.domain.PrepaymentCompilationRequest} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentCompilationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-compilation-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentCompilationRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CompilationStatusTypes
     */
    public static class CompilationStatusTypesFilter extends Filter<CompilationStatusTypes> {

        public CompilationStatusTypesFilter() {}

        public CompilationStatusTypesFilter(CompilationStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public CompilationStatusTypesFilter copy() {
            return new CompilationStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter timeOfRequest;

    private CompilationStatusTypesFilter compilationStatus;

    private IntegerFilter itemsProcessed;

    private UUIDFilter compilationToken;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PrepaymentCompilationRequestCriteria() {}

    public PrepaymentCompilationRequestCriteria(PrepaymentCompilationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeOfRequest = other.timeOfRequest == null ? null : other.timeOfRequest.copy();
        this.compilationStatus = other.compilationStatus == null ? null : other.compilationStatus.copy();
        this.itemsProcessed = other.itemsProcessed == null ? null : other.itemsProcessed.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentCompilationRequestCriteria copy() {
        return new PrepaymentCompilationRequestCriteria(this);
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

    public CompilationStatusTypesFilter getCompilationStatus() {
        return compilationStatus;
    }

    public CompilationStatusTypesFilter compilationStatus() {
        if (compilationStatus == null) {
            compilationStatus = new CompilationStatusTypesFilter();
        }
        return compilationStatus;
    }

    public void setCompilationStatus(CompilationStatusTypesFilter compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public IntegerFilter getItemsProcessed() {
        return itemsProcessed;
    }

    public IntegerFilter itemsProcessed() {
        if (itemsProcessed == null) {
            itemsProcessed = new IntegerFilter();
        }
        return itemsProcessed;
    }

    public void setItemsProcessed(IntegerFilter itemsProcessed) {
        this.itemsProcessed = itemsProcessed;
    }

    public UUIDFilter getCompilationToken() {
        return compilationToken;
    }

    public UUIDFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new UUIDFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(UUIDFilter compilationToken) {
        this.compilationToken = compilationToken;
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
        final PrepaymentCompilationRequestCriteria that = (PrepaymentCompilationRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeOfRequest, that.timeOfRequest) &&
            Objects.equals(compilationStatus, that.compilationStatus) &&
            Objects.equals(itemsProcessed, that.itemsProcessed) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfRequest, compilationStatus, itemsProcessed, compilationToken, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentCompilationRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (timeOfRequest != null ? "timeOfRequest=" + timeOfRequest + ", " : "") +
            (compilationStatus != null ? "compilationStatus=" + compilationStatus + ", " : "") +
            (itemsProcessed != null ? "itemsProcessed=" + itemsProcessed + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
