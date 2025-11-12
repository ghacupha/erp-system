package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
 * Criteria class for the {@link io.github.erp.domain.LeasePeriod} entity. This class is used
 * in {@link io.github.erp.web.rest.LeasePeriodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-periods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeasePeriodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sequenceNumber;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter periodCode;

    private LongFilter fiscalMonthId;

    private Boolean distinct;

    public LeasePeriodCriteria() {}

    public LeasePeriodCriteria(LeasePeriodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.periodCode = other.periodCode == null ? null : other.periodCode.copy();
        this.fiscalMonthId = other.fiscalMonthId == null ? null : other.fiscalMonthId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeasePeriodCriteria copy() {
        return new LeasePeriodCriteria(this);
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

    public LongFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public LongFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new LongFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(LongFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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

    public StringFilter getPeriodCode() {
        return periodCode;
    }

    public StringFilter periodCode() {
        if (periodCode == null) {
            periodCode = new StringFilter();
        }
        return periodCode;
    }

    public void setPeriodCode(StringFilter periodCode) {
        this.periodCode = periodCode;
    }

    public LongFilter getFiscalMonthId() {
        return fiscalMonthId;
    }

    public LongFilter fiscalMonthId() {
        if (fiscalMonthId == null) {
            fiscalMonthId = new LongFilter();
        }
        return fiscalMonthId;
    }

    public void setFiscalMonthId(LongFilter fiscalMonthId) {
        this.fiscalMonthId = fiscalMonthId;
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
        final LeasePeriodCriteria that = (LeasePeriodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(periodCode, that.periodCode) &&
            Objects.equals(fiscalMonthId, that.fiscalMonthId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceNumber, startDate, endDate, periodCode, fiscalMonthId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeasePeriodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (periodCode != null ? "periodCode=" + periodCode + ", " : "") +
            (fiscalMonthId != null ? "fiscalMonthId=" + fiscalMonthId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
