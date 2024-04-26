package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
