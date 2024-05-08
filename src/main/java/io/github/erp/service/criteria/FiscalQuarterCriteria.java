package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.FiscalQuarter} entity. This class is used
 * in {@link io.github.erp.web.rest.FiscalQuarterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-quarters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FiscalQuarterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quarterNumber;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter fiscalQuarterCode;

    private LongFilter fiscalYearId;

    private LongFilter placeholderId;

    private LongFilter universallyUniqueMappingId;

    private Boolean distinct;

    public FiscalQuarterCriteria() {}

    public FiscalQuarterCriteria(FiscalQuarterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quarterNumber = other.quarterNumber == null ? null : other.quarterNumber.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.fiscalQuarterCode = other.fiscalQuarterCode == null ? null : other.fiscalQuarterCode.copy();
        this.fiscalYearId = other.fiscalYearId == null ? null : other.fiscalYearId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FiscalQuarterCriteria copy() {
        return new FiscalQuarterCriteria(this);
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

    public IntegerFilter getQuarterNumber() {
        return quarterNumber;
    }

    public IntegerFilter quarterNumber() {
        if (quarterNumber == null) {
            quarterNumber = new IntegerFilter();
        }
        return quarterNumber;
    }

    public void setQuarterNumber(IntegerFilter quarterNumber) {
        this.quarterNumber = quarterNumber;
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

    public StringFilter getFiscalQuarterCode() {
        return fiscalQuarterCode;
    }

    public StringFilter fiscalQuarterCode() {
        if (fiscalQuarterCode == null) {
            fiscalQuarterCode = new StringFilter();
        }
        return fiscalQuarterCode;
    }

    public void setFiscalQuarterCode(StringFilter fiscalQuarterCode) {
        this.fiscalQuarterCode = fiscalQuarterCode;
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
        final FiscalQuarterCriteria that = (FiscalQuarterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quarterNumber, that.quarterNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalQuarterCode, that.fiscalQuarterCode) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            quarterNumber,
            startDate,
            endDate,
            fiscalQuarterCode,
            fiscalYearId,
            placeholderId,
            universallyUniqueMappingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalQuarterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (quarterNumber != null ? "quarterNumber=" + quarterNumber + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (fiscalQuarterCode != null ? "fiscalQuarterCode=" + fiscalQuarterCode + ", " : "") +
            (fiscalYearId != null ? "fiscalYearId=" + fiscalYearId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
