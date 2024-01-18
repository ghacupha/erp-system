package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.enumeration.FlagCodes;
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
 * Criteria class for the {@link io.github.erp.domain.LoanRestructureFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.LoanRestructureFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loan-restructure-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoanRestructureFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FlagCodes
     */
    public static class FlagCodesFilter extends Filter<FlagCodes> {

        public FlagCodesFilter() {}

        public FlagCodesFilter(FlagCodesFilter filter) {
            super(filter);
        }

        @Override
        public FlagCodesFilter copy() {
            return new FlagCodesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FlagCodesFilter loanRestructureFlagCode;

    private StringFilter loanRestructureFlagType;

    private Boolean distinct;

    public LoanRestructureFlagCriteria() {}

    public LoanRestructureFlagCriteria(LoanRestructureFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loanRestructureFlagCode = other.loanRestructureFlagCode == null ? null : other.loanRestructureFlagCode.copy();
        this.loanRestructureFlagType = other.loanRestructureFlagType == null ? null : other.loanRestructureFlagType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoanRestructureFlagCriteria copy() {
        return new LoanRestructureFlagCriteria(this);
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

    public FlagCodesFilter getLoanRestructureFlagCode() {
        return loanRestructureFlagCode;
    }

    public FlagCodesFilter loanRestructureFlagCode() {
        if (loanRestructureFlagCode == null) {
            loanRestructureFlagCode = new FlagCodesFilter();
        }
        return loanRestructureFlagCode;
    }

    public void setLoanRestructureFlagCode(FlagCodesFilter loanRestructureFlagCode) {
        this.loanRestructureFlagCode = loanRestructureFlagCode;
    }

    public StringFilter getLoanRestructureFlagType() {
        return loanRestructureFlagType;
    }

    public StringFilter loanRestructureFlagType() {
        if (loanRestructureFlagType == null) {
            loanRestructureFlagType = new StringFilter();
        }
        return loanRestructureFlagType;
    }

    public void setLoanRestructureFlagType(StringFilter loanRestructureFlagType) {
        this.loanRestructureFlagType = loanRestructureFlagType;
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
        final LoanRestructureFlagCriteria that = (LoanRestructureFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(loanRestructureFlagCode, that.loanRestructureFlagCode) &&
            Objects.equals(loanRestructureFlagType, that.loanRestructureFlagType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loanRestructureFlagCode, loanRestructureFlagType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRestructureFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (loanRestructureFlagCode != null ? "loanRestructureFlagCode=" + loanRestructureFlagCode + ", " : "") +
            (loanRestructureFlagType != null ? "loanRestructureFlagType=" + loanRestructureFlagType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
