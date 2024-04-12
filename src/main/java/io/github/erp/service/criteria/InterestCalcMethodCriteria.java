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
 * Criteria class for the {@link io.github.erp.domain.InterestCalcMethod} entity. This class is used
 * in {@link io.github.erp.web.rest.InterestCalcMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interest-calc-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InterestCalcMethodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter interestCalculationMethodCode;

    private StringFilter interestCalculationMthodType;

    private Boolean distinct;

    public InterestCalcMethodCriteria() {}

    public InterestCalcMethodCriteria(InterestCalcMethodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.interestCalculationMethodCode =
            other.interestCalculationMethodCode == null ? null : other.interestCalculationMethodCode.copy();
        this.interestCalculationMthodType = other.interestCalculationMthodType == null ? null : other.interestCalculationMthodType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InterestCalcMethodCriteria copy() {
        return new InterestCalcMethodCriteria(this);
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

    public StringFilter getInterestCalculationMethodCode() {
        return interestCalculationMethodCode;
    }

    public StringFilter interestCalculationMethodCode() {
        if (interestCalculationMethodCode == null) {
            interestCalculationMethodCode = new StringFilter();
        }
        return interestCalculationMethodCode;
    }

    public void setInterestCalculationMethodCode(StringFilter interestCalculationMethodCode) {
        this.interestCalculationMethodCode = interestCalculationMethodCode;
    }

    public StringFilter getInterestCalculationMthodType() {
        return interestCalculationMthodType;
    }

    public StringFilter interestCalculationMthodType() {
        if (interestCalculationMthodType == null) {
            interestCalculationMthodType = new StringFilter();
        }
        return interestCalculationMthodType;
    }

    public void setInterestCalculationMthodType(StringFilter interestCalculationMthodType) {
        this.interestCalculationMthodType = interestCalculationMthodType;
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
        final InterestCalcMethodCriteria that = (InterestCalcMethodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(interestCalculationMethodCode, that.interestCalculationMethodCode) &&
            Objects.equals(interestCalculationMthodType, that.interestCalculationMthodType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interestCalculationMethodCode, interestCalculationMthodType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterestCalcMethodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (interestCalculationMethodCode != null ? "interestCalculationMethodCode=" + interestCalculationMethodCode + ", " : "") +
            (interestCalculationMthodType != null ? "interestCalculationMthodType=" + interestCalculationMthodType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
