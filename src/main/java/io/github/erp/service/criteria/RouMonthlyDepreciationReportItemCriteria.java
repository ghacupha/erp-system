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
