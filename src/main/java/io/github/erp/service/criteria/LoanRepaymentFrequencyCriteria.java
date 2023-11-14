package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
 * Criteria class for the {@link io.github.erp.domain.LoanRepaymentFrequency} entity. This class is used
 * in {@link io.github.erp.web.rest.LoanRepaymentFrequencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loan-repayment-frequencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoanRepaymentFrequencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter frequencyTypeCode;

    private StringFilter frequencyType;

    private Boolean distinct;

    public LoanRepaymentFrequencyCriteria() {}

    public LoanRepaymentFrequencyCriteria(LoanRepaymentFrequencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.frequencyTypeCode = other.frequencyTypeCode == null ? null : other.frequencyTypeCode.copy();
        this.frequencyType = other.frequencyType == null ? null : other.frequencyType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LoanRepaymentFrequencyCriteria copy() {
        return new LoanRepaymentFrequencyCriteria(this);
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

    public StringFilter getFrequencyTypeCode() {
        return frequencyTypeCode;
    }

    public StringFilter frequencyTypeCode() {
        if (frequencyTypeCode == null) {
            frequencyTypeCode = new StringFilter();
        }
        return frequencyTypeCode;
    }

    public void setFrequencyTypeCode(StringFilter frequencyTypeCode) {
        this.frequencyTypeCode = frequencyTypeCode;
    }

    public StringFilter getFrequencyType() {
        return frequencyType;
    }

    public StringFilter frequencyType() {
        if (frequencyType == null) {
            frequencyType = new StringFilter();
        }
        return frequencyType;
    }

    public void setFrequencyType(StringFilter frequencyType) {
        this.frequencyType = frequencyType;
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
        final LoanRepaymentFrequencyCriteria that = (LoanRepaymentFrequencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(frequencyTypeCode, that.frequencyTypeCode) &&
            Objects.equals(frequencyType, that.frequencyType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, frequencyTypeCode, frequencyType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRepaymentFrequencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (frequencyTypeCode != null ? "frequencyTypeCode=" + frequencyTypeCode + ", " : "") +
            (frequencyType != null ? "frequencyType=" + frequencyType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
