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
