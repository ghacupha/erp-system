package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * Criteria class for the {@link io.github.erp.domain.LoanPerformanceClassification} entity. This class is used
 * in {@link io.github.erp.web.rest.LoanPerformanceClassificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loan-performance-classifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoanPerformanceClassificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loanPerformanceClassificationCode;

    private StringFilter loanPerformanceClassificationType;

    private Boolean distinct;

    public LoanPerformanceClassificationCriteria() {}

    public LoanPerformanceClassificationCriteria(LoanPerformanceClassificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loanPerformanceClassificationCode =
            other.loanPerformanceClassificationCode == null ? null : other.loanPerformanceClassificationCode.copy();
        this.loanPerformanceClassificationType =
            other.loanPerformanceClassificationType == null ? null : other.loanPerformanceClassificationType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoanPerformanceClassificationCriteria copy() {
        return new LoanPerformanceClassificationCriteria(this);
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

    public StringFilter getLoanPerformanceClassificationCode() {
        return loanPerformanceClassificationCode;
    }

    public StringFilter loanPerformanceClassificationCode() {
        if (loanPerformanceClassificationCode == null) {
            loanPerformanceClassificationCode = new StringFilter();
        }
        return loanPerformanceClassificationCode;
    }

    public void setLoanPerformanceClassificationCode(StringFilter loanPerformanceClassificationCode) {
        this.loanPerformanceClassificationCode = loanPerformanceClassificationCode;
    }

    public StringFilter getLoanPerformanceClassificationType() {
        return loanPerformanceClassificationType;
    }

    public StringFilter loanPerformanceClassificationType() {
        if (loanPerformanceClassificationType == null) {
            loanPerformanceClassificationType = new StringFilter();
        }
        return loanPerformanceClassificationType;
    }

    public void setLoanPerformanceClassificationType(StringFilter loanPerformanceClassificationType) {
        this.loanPerformanceClassificationType = loanPerformanceClassificationType;
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
        final LoanPerformanceClassificationCriteria that = (LoanPerformanceClassificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(loanPerformanceClassificationCode, that.loanPerformanceClassificationCode) &&
            Objects.equals(loanPerformanceClassificationType, that.loanPerformanceClassificationType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loanPerformanceClassificationCode, loanPerformanceClassificationType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanPerformanceClassificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (loanPerformanceClassificationCode != null ? "loanPerformanceClassificationCode=" + loanPerformanceClassificationCode + ", " : "") +
            (loanPerformanceClassificationType != null ? "loanPerformanceClassificationType=" + loanPerformanceClassificationType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
