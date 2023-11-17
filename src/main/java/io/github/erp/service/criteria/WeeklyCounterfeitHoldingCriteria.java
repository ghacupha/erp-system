package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * Criteria class for the {@link io.github.erp.domain.WeeklyCounterfeitHolding} entity. This class is used
 * in {@link io.github.erp.web.rest.WeeklyCounterfeitHoldingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /weekly-counterfeit-holdings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WeeklyCounterfeitHoldingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private LocalDateFilter dateConfiscated;

    private StringFilter serialNumber;

    private StringFilter depositorsNames;

    private StringFilter tellersNames;

    private LocalDateFilter dateSubmittedToCBK;

    private Boolean distinct;

    public WeeklyCounterfeitHoldingCriteria() {}

    public WeeklyCounterfeitHoldingCriteria(WeeklyCounterfeitHoldingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.dateConfiscated = other.dateConfiscated == null ? null : other.dateConfiscated.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.depositorsNames = other.depositorsNames == null ? null : other.depositorsNames.copy();
        this.tellersNames = other.tellersNames == null ? null : other.tellersNames.copy();
        this.dateSubmittedToCBK = other.dateSubmittedToCBK == null ? null : other.dateSubmittedToCBK.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WeeklyCounterfeitHoldingCriteria copy() {
        return new WeeklyCounterfeitHoldingCriteria(this);
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

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public LocalDateFilter getDateConfiscated() {
        return dateConfiscated;
    }

    public LocalDateFilter dateConfiscated() {
        if (dateConfiscated == null) {
            dateConfiscated = new LocalDateFilter();
        }
        return dateConfiscated;
    }

    public void setDateConfiscated(LocalDateFilter dateConfiscated) {
        this.dateConfiscated = dateConfiscated;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getDepositorsNames() {
        return depositorsNames;
    }

    public StringFilter depositorsNames() {
        if (depositorsNames == null) {
            depositorsNames = new StringFilter();
        }
        return depositorsNames;
    }

    public void setDepositorsNames(StringFilter depositorsNames) {
        this.depositorsNames = depositorsNames;
    }

    public StringFilter getTellersNames() {
        return tellersNames;
    }

    public StringFilter tellersNames() {
        if (tellersNames == null) {
            tellersNames = new StringFilter();
        }
        return tellersNames;
    }

    public void setTellersNames(StringFilter tellersNames) {
        this.tellersNames = tellersNames;
    }

    public LocalDateFilter getDateSubmittedToCBK() {
        return dateSubmittedToCBK;
    }

    public LocalDateFilter dateSubmittedToCBK() {
        if (dateSubmittedToCBK == null) {
            dateSubmittedToCBK = new LocalDateFilter();
        }
        return dateSubmittedToCBK;
    }

    public void setDateSubmittedToCBK(LocalDateFilter dateSubmittedToCBK) {
        this.dateSubmittedToCBK = dateSubmittedToCBK;
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
        final WeeklyCounterfeitHoldingCriteria that = (WeeklyCounterfeitHoldingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(dateConfiscated, that.dateConfiscated) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(depositorsNames, that.depositorsNames) &&
            Objects.equals(tellersNames, that.tellersNames) &&
            Objects.equals(dateSubmittedToCBK, that.dateSubmittedToCBK) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportingDate, dateConfiscated, serialNumber, depositorsNames, tellersNames, dateSubmittedToCBK, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCounterfeitHoldingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (dateConfiscated != null ? "dateConfiscated=" + dateConfiscated + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (depositorsNames != null ? "depositorsNames=" + depositorsNames + ", " : "") +
            (tellersNames != null ? "tellersNames=" + tellersNames + ", " : "") +
            (dateSubmittedToCBK != null ? "dateSubmittedToCBK=" + dateSubmittedToCBK + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
