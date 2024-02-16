package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.ChartOfAccountsCode} entity. This class is used
 * in {@link io.github.erp.web.rest.ChartOfAccountsCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chart-of-accounts-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChartOfAccountsCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter chartOfAccountsCode;

    private StringFilter chartOfAccountsClass;

    private Boolean distinct;

    public ChartOfAccountsCodeCriteria() {}

    public ChartOfAccountsCodeCriteria(ChartOfAccountsCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.chartOfAccountsCode = other.chartOfAccountsCode == null ? null : other.chartOfAccountsCode.copy();
        this.chartOfAccountsClass = other.chartOfAccountsClass == null ? null : other.chartOfAccountsClass.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ChartOfAccountsCodeCriteria copy() {
        return new ChartOfAccountsCodeCriteria(this);
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

    public StringFilter getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public StringFilter chartOfAccountsCode() {
        if (chartOfAccountsCode == null) {
            chartOfAccountsCode = new StringFilter();
        }
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(StringFilter chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public StringFilter getChartOfAccountsClass() {
        return chartOfAccountsClass;
    }

    public StringFilter chartOfAccountsClass() {
        if (chartOfAccountsClass == null) {
            chartOfAccountsClass = new StringFilter();
        }
        return chartOfAccountsClass;
    }

    public void setChartOfAccountsClass(StringFilter chartOfAccountsClass) {
        this.chartOfAccountsClass = chartOfAccountsClass;
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
        final ChartOfAccountsCodeCriteria that = (ChartOfAccountsCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(chartOfAccountsCode, that.chartOfAccountsCode) &&
            Objects.equals(chartOfAccountsClass, that.chartOfAccountsClass) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chartOfAccountsCode, chartOfAccountsClass, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccountsCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (chartOfAccountsCode != null ? "chartOfAccountsCode=" + chartOfAccountsCode + ", " : "") +
            (chartOfAccountsClass != null ? "chartOfAccountsClass=" + chartOfAccountsClass + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
