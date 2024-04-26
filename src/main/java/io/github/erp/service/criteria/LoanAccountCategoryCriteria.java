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
