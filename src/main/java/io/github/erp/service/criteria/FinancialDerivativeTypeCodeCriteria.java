package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.FinancialDerivativeTypeCode} entity. This class is used
 * in {@link io.github.erp.web.rest.FinancialDerivativeTypeCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /financial-derivative-type-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FinancialDerivativeTypeCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter financialDerivativeTypeCode;

    private StringFilter financialDerivativeType;

    private Boolean distinct;

    public FinancialDerivativeTypeCodeCriteria() {}

    public FinancialDerivativeTypeCodeCriteria(FinancialDerivativeTypeCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.financialDerivativeTypeCode = other.financialDerivativeTypeCode == null ? null : other.financialDerivativeTypeCode.copy();
        this.financialDerivativeType = other.financialDerivativeType == null ? null : other.financialDerivativeType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FinancialDerivativeTypeCodeCriteria copy() {
        return new FinancialDerivativeTypeCodeCriteria(this);
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

    public StringFilter getFinancialDerivativeTypeCode() {
        return financialDerivativeTypeCode;
    }

    public StringFilter financialDerivativeTypeCode() {
        if (financialDerivativeTypeCode == null) {
            financialDerivativeTypeCode = new StringFilter();
        }
        return financialDerivativeTypeCode;
    }

    public void setFinancialDerivativeTypeCode(StringFilter financialDerivativeTypeCode) {
        this.financialDerivativeTypeCode = financialDerivativeTypeCode;
    }

    public StringFilter getFinancialDerivativeType() {
        return financialDerivativeType;
    }

    public StringFilter financialDerivativeType() {
        if (financialDerivativeType == null) {
            financialDerivativeType = new StringFilter();
        }
        return financialDerivativeType;
    }

    public void setFinancialDerivativeType(StringFilter financialDerivativeType) {
        this.financialDerivativeType = financialDerivativeType;
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
        final FinancialDerivativeTypeCodeCriteria that = (FinancialDerivativeTypeCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(financialDerivativeTypeCode, that.financialDerivativeTypeCode) &&
            Objects.equals(financialDerivativeType, that.financialDerivativeType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialDerivativeTypeCode, financialDerivativeType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialDerivativeTypeCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (financialDerivativeTypeCode != null ? "financialDerivativeTypeCode=" + financialDerivativeTypeCode + ", " : "") +
            (financialDerivativeType != null ? "financialDerivativeType=" + financialDerivativeType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
