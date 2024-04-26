package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
 * Criteria class for the {@link io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.MonthlyPrepaymentOutstandingReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monthly-prepayment-outstanding-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MonthlyPrepaymentOutstandingReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fiscalMonthEndDate;

    private BigDecimalFilter totalPrepaymentAmount;

    private BigDecimalFilter totalAmortisedAmount;

    private BigDecimalFilter totalOutstandingAmount;

    private IntegerFilter numberOfPrepaymentAccounts;

    private Boolean distinct;

    public MonthlyPrepaymentOutstandingReportItemCriteria() {}

    public MonthlyPrepaymentOutstandingReportItemCriteria(MonthlyPrepaymentOutstandingReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fiscalMonthEndDate = other.fiscalMonthEndDate == null ? null : other.fiscalMonthEndDate.copy();
        this.totalPrepaymentAmount = other.totalPrepaymentAmount == null ? null : other.totalPrepaymentAmount.copy();
        this.totalAmortisedAmount = other.totalAmortisedAmount == null ? null : other.totalAmortisedAmount.copy();
        this.totalOutstandingAmount = other.totalOutstandingAmount == null ? null : other.totalOutstandingAmount.copy();
        this.numberOfPrepaymentAccounts = other.numberOfPrepaymentAccounts == null ? null : other.numberOfPrepaymentAccounts.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MonthlyPrepaymentOutstandingReportItemCriteria copy() {
        return new MonthlyPrepaymentOutstandingReportItemCriteria(this);
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

    public BigDecimalFilter getTotalPrepaymentAmount() {
        return totalPrepaymentAmount;
    }

    public BigDecimalFilter totalPrepaymentAmount() {
        if (totalPrepaymentAmount == null) {
            totalPrepaymentAmount = new BigDecimalFilter();
        }
        return totalPrepaymentAmount;
    }

    public void setTotalPrepaymentAmount(BigDecimalFilter totalPrepaymentAmount) {
        this.totalPrepaymentAmount = totalPrepaymentAmount;
    }

    public BigDecimalFilter getTotalAmortisedAmount() {
        return totalAmortisedAmount;
    }

    public BigDecimalFilter totalAmortisedAmount() {
        if (totalAmortisedAmount == null) {
            totalAmortisedAmount = new BigDecimalFilter();
        }
        return totalAmortisedAmount;
    }

    public void setTotalAmortisedAmount(BigDecimalFilter totalAmortisedAmount) {
        this.totalAmortisedAmount = totalAmortisedAmount;
    }

    public BigDecimalFilter getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    public BigDecimalFilter totalOutstandingAmount() {
        if (totalOutstandingAmount == null) {
            totalOutstandingAmount = new BigDecimalFilter();
        }
        return totalOutstandingAmount;
    }

    public void setTotalOutstandingAmount(BigDecimalFilter totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public IntegerFilter getNumberOfPrepaymentAccounts() {
        return numberOfPrepaymentAccounts;
    }

    public IntegerFilter numberOfPrepaymentAccounts() {
        if (numberOfPrepaymentAccounts == null) {
            numberOfPrepaymentAccounts = new IntegerFilter();
        }
        return numberOfPrepaymentAccounts;
    }

    public void setNumberOfPrepaymentAccounts(IntegerFilter numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
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
        final MonthlyPrepaymentOutstandingReportItemCriteria that = (MonthlyPrepaymentOutstandingReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fiscalMonthEndDate, that.fiscalMonthEndDate) &&
            Objects.equals(totalPrepaymentAmount, that.totalPrepaymentAmount) &&
            Objects.equals(totalAmortisedAmount, that.totalAmortisedAmount) &&
            Objects.equals(totalOutstandingAmount, that.totalOutstandingAmount) &&
            Objects.equals(numberOfPrepaymentAccounts, that.numberOfPrepaymentAccounts) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fiscalMonthEndDate,
            totalPrepaymentAmount,
            totalAmortisedAmount,
            totalOutstandingAmount,
            numberOfPrepaymentAccounts,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonthlyPrepaymentOutstandingReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fiscalMonthEndDate != null ? "fiscalMonthEndDate=" + fiscalMonthEndDate + ", " : "") +
            (totalPrepaymentAmount != null ? "totalPrepaymentAmount=" + totalPrepaymentAmount + ", " : "") +
            (totalAmortisedAmount != null ? "totalAmortisedAmount=" + totalAmortisedAmount + ", " : "") +
            (totalOutstandingAmount != null ? "totalOutstandingAmount=" + totalOutstandingAmount + ", " : "") +
            (numberOfPrepaymentAccounts != null ? "numberOfPrepaymentAccounts=" + numberOfPrepaymentAccounts + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
