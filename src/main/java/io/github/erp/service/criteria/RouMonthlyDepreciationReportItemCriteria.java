package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouMonthlyDepreciationReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouMonthlyDepreciationReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-monthly-depreciation-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouMonthlyDepreciationReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fiscalMonthStartDate;

    private LocalDateFilter fiscalMonthEndDate;

    private BigDecimalFilter totalDepreciationAmount;

    private Boolean distinct;

    public RouMonthlyDepreciationReportItemCriteria() {}

    public RouMonthlyDepreciationReportItemCriteria(RouMonthlyDepreciationReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fiscalMonthStartDate = other.fiscalMonthStartDate == null ? null : other.fiscalMonthStartDate.copy();
        this.fiscalMonthEndDate = other.fiscalMonthEndDate == null ? null : other.fiscalMonthEndDate.copy();
        this.totalDepreciationAmount = other.totalDepreciationAmount == null ? null : other.totalDepreciationAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouMonthlyDepreciationReportItemCriteria copy() {
        return new RouMonthlyDepreciationReportItemCriteria(this);
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

    public LocalDateFilter getFiscalMonthStartDate() {
        return fiscalMonthStartDate;
    }

    public LocalDateFilter fiscalMonthStartDate() {
        if (fiscalMonthStartDate == null) {
            fiscalMonthStartDate = new LocalDateFilter();
        }
        return fiscalMonthStartDate;
    }

    public void setFiscalMonthStartDate(LocalDateFilter fiscalMonthStartDate) {
        this.fiscalMonthStartDate = fiscalMonthStartDate;
    }

    public LocalDateFilter getFiscalMonthEndDate() {
        return fiscalMonthEndDate;
    }

    public LocalDateFilter fiscalMonthEndDate() {
        if (fiscalMonthEndDate == null) {
            fiscalMonthEndDate = new LocalDateFilter();
        }
        return fiscalMonthEndDate;
    }

    public void setFiscalMonthEndDate(LocalDateFilter fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    public BigDecimalFilter getTotalDepreciationAmount() {
        return totalDepreciationAmount;
    }

    public BigDecimalFilter totalDepreciationAmount() {
        if (totalDepreciationAmount == null) {
            totalDepreciationAmount = new BigDecimalFilter();
        }
        return totalDepreciationAmount;
    }

    public void setTotalDepreciationAmount(BigDecimalFilter totalDepreciationAmount) {
        this.totalDepreciationAmount = totalDepreciationAmount;
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
        final RouMonthlyDepreciationReportItemCriteria that = (RouMonthlyDepreciationReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fiscalMonthStartDate, that.fiscalMonthStartDate) &&
            Objects.equals(fiscalMonthEndDate, that.fiscalMonthEndDate) &&
            Objects.equals(totalDepreciationAmount, that.totalDepreciationAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fiscalMonthStartDate, fiscalMonthEndDate, totalDepreciationAmount, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouMonthlyDepreciationReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fiscalMonthStartDate != null ? "fiscalMonthStartDate=" + fiscalMonthStartDate + ", " : "") +
            (fiscalMonthEndDate != null ? "fiscalMonthEndDate=" + fiscalMonthEndDate + ", " : "") +
            (totalDepreciationAmount != null ? "totalDepreciationAmount=" + totalDepreciationAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
