package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * Criteria class for the {@link io.github.erp.domain.FiscalMonth} entity. This class is used
 * in {@link io.github.erp.web.rest.FiscalMonthResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-months?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FiscalMonthCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter monthNumber;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter fiscalMonthCode;

    private LongFilter fiscalYearId;

    private LongFilter placeholderId;

    private LongFilter universallyUniqueMappingId;

    private LongFilter fiscalQuarterId;

    private Boolean distinct;

    public FiscalMonthCriteria() {}

    public FiscalMonthCriteria(FiscalMonthCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.monthNumber = other.monthNumber == null ? null : other.monthNumber.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.fiscalMonthCode = other.fiscalMonthCode == null ? null : other.fiscalMonthCode.copy();
        this.fiscalYearId = other.fiscalYearId == null ? null : other.fiscalYearId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.fiscalQuarterId = other.fiscalQuarterId == null ? null : other.fiscalQuarterId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FiscalMonthCriteria copy() {
        return new FiscalMonthCriteria(this);
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

    public IntegerFilter getMonthNumber() {
        return monthNumber;
    }

    public IntegerFilter monthNumber() {
        if (monthNumber == null) {
            monthNumber = new IntegerFilter();
        }
        return monthNumber;
    }

    public void setMonthNumber(IntegerFilter monthNumber) {
        this.monthNumber = monthNumber;
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

    public StringFilter getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public StringFilter fiscalMonthCode() {
        if (fiscalMonthCode == null) {
            fiscalMonthCode = new StringFilter();
        }
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(StringFilter fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public LongFilter getFiscalYearId() {
        return fiscalYearId;
    }

    public LongFilter fiscalYearId() {
        if (fiscalYearId == null) {
            fiscalYearId = new LongFilter();
        }
        return fiscalYearId;
    }

    public void setFiscalYearId(LongFilter fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
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

    public LongFilter getFiscalQuarterId() {
        return fiscalQuarterId;
    }

    public LongFilter fiscalQuarterId() {
        if (fiscalQuarterId == null) {
            fiscalQuarterId = new LongFilter();
        }
        return fiscalQuarterId;
    }

    public void setFiscalQuarterId(LongFilter fiscalQuarterId) {
        this.fiscalQuarterId = fiscalQuarterId;
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
        final FiscalMonthCriteria that = (FiscalMonthCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(monthNumber, that.monthNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalMonthCode, that.fiscalMonthCode) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(fiscalQuarterId, that.fiscalQuarterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            monthNumber,
            startDate,
            endDate,
            fiscalMonthCode,
            fiscalYearId,
            placeholderId,
            universallyUniqueMappingId,
            fiscalQuarterId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalMonthCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (monthNumber != null ? "monthNumber=" + monthNumber + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (fiscalMonthCode != null ? "fiscalMonthCode=" + fiscalMonthCode + ", " : "") +
            (fiscalYearId != null ? "fiscalYearId=" + fiscalYearId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (fiscalQuarterId != null ? "fiscalQuarterId=" + fiscalQuarterId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
