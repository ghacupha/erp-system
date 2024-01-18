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

import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
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
 * Criteria class for the {@link io.github.erp.domain.DepreciationPeriod} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationPeriodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-periods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationPeriodCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationPeriodStatusTypes
     */
    public static class DepreciationPeriodStatusTypesFilter extends Filter<DepreciationPeriodStatusTypes> {

        public DepreciationPeriodStatusTypesFilter() {}

        public DepreciationPeriodStatusTypesFilter(DepreciationPeriodStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationPeriodStatusTypesFilter copy() {
            return new DepreciationPeriodStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private DepreciationPeriodStatusTypesFilter depreciationPeriodStatus;

    private StringFilter periodCode;

    private BooleanFilter processLocked;

    private LongFilter previousPeriodId;

    private LongFilter fiscalMonthId;

    private Boolean distinct;

    public DepreciationPeriodCriteria() {}

    public DepreciationPeriodCriteria(DepreciationPeriodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.depreciationPeriodStatus = other.depreciationPeriodStatus == null ? null : other.depreciationPeriodStatus.copy();
        this.periodCode = other.periodCode == null ? null : other.periodCode.copy();
        this.processLocked = other.processLocked == null ? null : other.processLocked.copy();
        this.previousPeriodId = other.previousPeriodId == null ? null : other.previousPeriodId.copy();
        this.fiscalMonthId = other.fiscalMonthId == null ? null : other.fiscalMonthId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationPeriodCriteria copy() {
        return new DepreciationPeriodCriteria(this);
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

    public DepreciationPeriodStatusTypesFilter getDepreciationPeriodStatus() {
        return depreciationPeriodStatus;
    }

    public DepreciationPeriodStatusTypesFilter depreciationPeriodStatus() {
        if (depreciationPeriodStatus == null) {
            depreciationPeriodStatus = new DepreciationPeriodStatusTypesFilter();
        }
        return depreciationPeriodStatus;
    }

    public void setDepreciationPeriodStatus(DepreciationPeriodStatusTypesFilter depreciationPeriodStatus) {
        this.depreciationPeriodStatus = depreciationPeriodStatus;
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

    public BooleanFilter getProcessLocked() {
        return processLocked;
    }

    public BooleanFilter processLocked() {
        if (processLocked == null) {
            processLocked = new BooleanFilter();
        }
        return processLocked;
    }

    public void setProcessLocked(BooleanFilter processLocked) {
        this.processLocked = processLocked;
    }

    public LongFilter getPreviousPeriodId() {
        return previousPeriodId;
    }

    public LongFilter previousPeriodId() {
        if (previousPeriodId == null) {
            previousPeriodId = new LongFilter();
        }
        return previousPeriodId;
    }

    public void setPreviousPeriodId(LongFilter previousPeriodId) {
        this.previousPeriodId = previousPeriodId;
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
        final DepreciationPeriodCriteria that = (DepreciationPeriodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(depreciationPeriodStatus, that.depreciationPeriodStatus) &&
            Objects.equals(periodCode, that.periodCode) &&
            Objects.equals(processLocked, that.processLocked) &&
            Objects.equals(previousPeriodId, that.previousPeriodId) &&
            Objects.equals(fiscalMonthId, that.fiscalMonthId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startDate,
            endDate,
            depreciationPeriodStatus,
            periodCode,
            processLocked,
            previousPeriodId,
            fiscalMonthId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationPeriodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (depreciationPeriodStatus != null ? "depreciationPeriodStatus=" + depreciationPeriodStatus + ", " : "") +
            (periodCode != null ? "periodCode=" + periodCode + ", " : "") +
            (processLocked != null ? "processLocked=" + processLocked + ", " : "") +
            (previousPeriodId != null ? "previousPeriodId=" + previousPeriodId + ", " : "") +
            (fiscalMonthId != null ? "fiscalMonthId=" + fiscalMonthId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
