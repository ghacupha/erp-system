package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.enumeration.LoanAccountMutationTypes;
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
 * Criteria class for the {@link io.github.erp.domain.LoanAccountCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.LoanAccountCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loan-account-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoanAccountCategoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LoanAccountMutationTypes
     */
    public static class LoanAccountMutationTypesFilter extends Filter<LoanAccountMutationTypes> {

        public LoanAccountMutationTypesFilter() {}

        public LoanAccountMutationTypesFilter(LoanAccountMutationTypesFilter filter) {
            super(filter);
        }

        @Override
        public LoanAccountMutationTypesFilter copy() {
            return new LoanAccountMutationTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loanAccountMutationCode;

    private LoanAccountMutationTypesFilter loanAccountMutationType;

    private StringFilter loanAccountMutationDetails;

    private Boolean distinct;

    public LoanAccountCategoryCriteria() {}

    public LoanAccountCategoryCriteria(LoanAccountCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loanAccountMutationCode = other.loanAccountMutationCode == null ? null : other.loanAccountMutationCode.copy();
        this.loanAccountMutationType = other.loanAccountMutationType == null ? null : other.loanAccountMutationType.copy();
        this.loanAccountMutationDetails = other.loanAccountMutationDetails == null ? null : other.loanAccountMutationDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoanAccountCategoryCriteria copy() {
        return new LoanAccountCategoryCriteria(this);
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

    public StringFilter getLoanAccountMutationCode() {
        return loanAccountMutationCode;
    }

    public StringFilter loanAccountMutationCode() {
        if (loanAccountMutationCode == null) {
            loanAccountMutationCode = new StringFilter();
        }
        return loanAccountMutationCode;
    }

    public void setLoanAccountMutationCode(StringFilter loanAccountMutationCode) {
        this.loanAccountMutationCode = loanAccountMutationCode;
    }

    public LoanAccountMutationTypesFilter getLoanAccountMutationType() {
        return loanAccountMutationType;
    }

    public LoanAccountMutationTypesFilter loanAccountMutationType() {
        if (loanAccountMutationType == null) {
            loanAccountMutationType = new LoanAccountMutationTypesFilter();
        }
        return loanAccountMutationType;
    }

    public void setLoanAccountMutationType(LoanAccountMutationTypesFilter loanAccountMutationType) {
        this.loanAccountMutationType = loanAccountMutationType;
    }

    public StringFilter getLoanAccountMutationDetails() {
        return loanAccountMutationDetails;
    }

    public StringFilter loanAccountMutationDetails() {
        if (loanAccountMutationDetails == null) {
            loanAccountMutationDetails = new StringFilter();
        }
        return loanAccountMutationDetails;
    }

    public void setLoanAccountMutationDetails(StringFilter loanAccountMutationDetails) {
        this.loanAccountMutationDetails = loanAccountMutationDetails;
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
        final LoanAccountCategoryCriteria that = (LoanAccountCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(loanAccountMutationCode, that.loanAccountMutationCode) &&
            Objects.equals(loanAccountMutationType, that.loanAccountMutationType) &&
            Objects.equals(loanAccountMutationDetails, that.loanAccountMutationDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loanAccountMutationCode, loanAccountMutationType, loanAccountMutationDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanAccountCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (loanAccountMutationCode != null ? "loanAccountMutationCode=" + loanAccountMutationCode + ", " : "") +
            (loanAccountMutationType != null ? "loanAccountMutationType=" + loanAccountMutationType + ", " : "") +
            (loanAccountMutationDetails != null ? "loanAccountMutationDetails=" + loanAccountMutationDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
