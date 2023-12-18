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

import io.github.erp.domain.enumeration.FiscalYearStatusType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.FiscalYear} entity. This class is used
 * in {@link io.github.erp.web.rest.FiscalYearResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-years?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FiscalYearCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FiscalYearStatusType
     */
    public static class FiscalYearStatusTypeFilter extends Filter<FiscalYearStatusType> {

        public FiscalYearStatusTypeFilter() {}

        public FiscalYearStatusTypeFilter(FiscalYearStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public FiscalYearStatusTypeFilter copy() {
            return new FiscalYearStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fiscalYearCode;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private FiscalYearStatusTypeFilter fiscalYearStatus;

    private LongFilter placeholderId;

    private LongFilter universallyUniqueMappingId;

    private LongFilter createdById;

    private LongFilter lastUpdatedById;

    private Boolean distinct;

    public FiscalYearCriteria() {}

    public FiscalYearCriteria(FiscalYearCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fiscalYearCode = other.fiscalYearCode == null ? null : other.fiscalYearCode.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.fiscalYearStatus = other.fiscalYearStatus == null ? null : other.fiscalYearStatus.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.lastUpdatedById = other.lastUpdatedById == null ? null : other.lastUpdatedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FiscalYearCriteria copy() {
        return new FiscalYearCriteria(this);
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

    public StringFilter getFiscalYearCode() {
        return fiscalYearCode;
    }

    public StringFilter fiscalYearCode() {
        if (fiscalYearCode == null) {
            fiscalYearCode = new StringFilter();
        }
        return fiscalYearCode;
    }

    public void setFiscalYearCode(StringFilter fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public FiscalYearStatusTypeFilter getFiscalYearStatus() {
        return fiscalYearStatus;
    }

    public FiscalYearStatusTypeFilter fiscalYearStatus() {
        if (fiscalYearStatus == null) {
            fiscalYearStatus = new FiscalYearStatusTypeFilter();
        }
        return fiscalYearStatus;
    }

    public void setFiscalYearStatus(FiscalYearStatusTypeFilter fiscalYearStatus) {
        this.fiscalYearStatus = fiscalYearStatus;
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

    public LongFilter getUniversallyUniqueMappingId() {
        return universallyUniqueMappingId;
    }

    public LongFilter universallyUniqueMappingId() {
        if (universallyUniqueMappingId == null) {
            universallyUniqueMappingId = new LongFilter();
        }
        return universallyUniqueMappingId;
    }

    public void setUniversallyUniqueMappingId(LongFilter universallyUniqueMappingId) {
        this.universallyUniqueMappingId = universallyUniqueMappingId;
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

    public LongFilter getLastUpdatedById() {
        return lastUpdatedById;
    }

    public LongFilter lastUpdatedById() {
        if (lastUpdatedById == null) {
            lastUpdatedById = new LongFilter();
        }
        return lastUpdatedById;
    }

    public void setLastUpdatedById(LongFilter lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
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
        final FiscalYearCriteria that = (FiscalYearCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fiscalYearCode, that.fiscalYearCode) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalYearStatus, that.fiscalYearStatus) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastUpdatedById, that.lastUpdatedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fiscalYearCode,
            startDate,
            endDate,
            fiscalYearStatus,
            placeholderId,
            universallyUniqueMappingId,
            createdById,
            lastUpdatedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalYearCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fiscalYearCode != null ? "fiscalYearCode=" + fiscalYearCode + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (fiscalYearStatus != null ? "fiscalYearStatus=" + fiscalYearStatus + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (lastUpdatedById != null ? "lastUpdatedById=" + lastUpdatedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
