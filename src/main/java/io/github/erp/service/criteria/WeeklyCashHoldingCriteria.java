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
 * Criteria class for the {@link io.github.erp.domain.WeeklyCashHolding} entity. This class is used
 * in {@link io.github.erp.web.rest.WeeklyCashHoldingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /weekly-cash-holdings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WeeklyCashHoldingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private IntegerFilter fitUnits;

    private IntegerFilter unfitUnits;

    private LongFilter bankCodeId;

    private LongFilter branchIdId;

    private LongFilter subCountyCodeId;

    private LongFilter denominationId;

    private Boolean distinct;

    public WeeklyCashHoldingCriteria() {}

    public WeeklyCashHoldingCriteria(WeeklyCashHoldingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.fitUnits = other.fitUnits == null ? null : other.fitUnits.copy();
        this.unfitUnits = other.unfitUnits == null ? null : other.unfitUnits.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchIdId = other.branchIdId == null ? null : other.branchIdId.copy();
        this.subCountyCodeId = other.subCountyCodeId == null ? null : other.subCountyCodeId.copy();
        this.denominationId = other.denominationId == null ? null : other.denominationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WeeklyCashHoldingCriteria copy() {
        return new WeeklyCashHoldingCriteria(this);
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

    public IntegerFilter getFitUnits() {
        return fitUnits;
    }

    public IntegerFilter fitUnits() {
        if (fitUnits == null) {
            fitUnits = new IntegerFilter();
        }
        return fitUnits;
    }

    public void setFitUnits(IntegerFilter fitUnits) {
        this.fitUnits = fitUnits;
    }

    public IntegerFilter getUnfitUnits() {
        return unfitUnits;
    }

    public IntegerFilter unfitUnits() {
        if (unfitUnits == null) {
            unfitUnits = new IntegerFilter();
        }
        return unfitUnits;
    }

    public void setUnfitUnits(IntegerFilter unfitUnits) {
        this.unfitUnits = unfitUnits;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getBranchIdId() {
        return branchIdId;
    }

    public LongFilter branchIdId() {
        if (branchIdId == null) {
            branchIdId = new LongFilter();
        }
        return branchIdId;
    }

    public void setBranchIdId(LongFilter branchIdId) {
        this.branchIdId = branchIdId;
    }

    public LongFilter getSubCountyCodeId() {
        return subCountyCodeId;
    }

    public LongFilter subCountyCodeId() {
        if (subCountyCodeId == null) {
            subCountyCodeId = new LongFilter();
        }
        return subCountyCodeId;
    }

    public void setSubCountyCodeId(LongFilter subCountyCodeId) {
        this.subCountyCodeId = subCountyCodeId;
    }

    public LongFilter getDenominationId() {
        return denominationId;
    }

    public LongFilter denominationId() {
        if (denominationId == null) {
            denominationId = new LongFilter();
        }
        return denominationId;
    }

    public void setDenominationId(LongFilter denominationId) {
        this.denominationId = denominationId;
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
        final WeeklyCashHoldingCriteria that = (WeeklyCashHoldingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(fitUnits, that.fitUnits) &&
            Objects.equals(unfitUnits, that.unfitUnits) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchIdId, that.branchIdId) &&
            Objects.equals(subCountyCodeId, that.subCountyCodeId) &&
            Objects.equals(denominationId, that.denominationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportingDate, fitUnits, unfitUnits, bankCodeId, branchIdId, subCountyCodeId, denominationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCashHoldingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (fitUnits != null ? "fitUnits=" + fitUnits + ", " : "") +
            (unfitUnits != null ? "unfitUnits=" + unfitUnits + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchIdId != null ? "branchIdId=" + branchIdId + ", " : "") +
            (subCountyCodeId != null ? "subCountyCodeId=" + subCountyCodeId + ", " : "") +
            (denominationId != null ? "denominationId=" + denominationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
