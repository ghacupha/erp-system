package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.enumeration.CurrencyServiceability;
import io.github.erp.domain.enumeration.CurrencyServiceabilityFlagTypes;
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
 * Criteria class for the {@link io.github.erp.domain.CurrencyServiceabilityFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.CurrencyServiceabilityFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currency-serviceability-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyServiceabilityFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CurrencyServiceabilityFlagTypes
     */
    public static class CurrencyServiceabilityFlagTypesFilter extends Filter<CurrencyServiceabilityFlagTypes> {

        public CurrencyServiceabilityFlagTypesFilter() {}

        public CurrencyServiceabilityFlagTypesFilter(CurrencyServiceabilityFlagTypesFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyServiceabilityFlagTypesFilter copy() {
            return new CurrencyServiceabilityFlagTypesFilter(this);
        }
    }

    /**
     * Class for filtering CurrencyServiceability
     */
    public static class CurrencyServiceabilityFilter extends Filter<CurrencyServiceability> {

        public CurrencyServiceabilityFilter() {}

        public CurrencyServiceabilityFilter(CurrencyServiceabilityFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyServiceabilityFilter copy() {
            return new CurrencyServiceabilityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CurrencyServiceabilityFlagTypesFilter currencyServiceabilityFlag;

    private CurrencyServiceabilityFilter currencyServiceability;

    private Boolean distinct;

    public CurrencyServiceabilityFlagCriteria() {}

    public CurrencyServiceabilityFlagCriteria(CurrencyServiceabilityFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currencyServiceabilityFlag = other.currencyServiceabilityFlag == null ? null : other.currencyServiceabilityFlag.copy();
        this.currencyServiceability = other.currencyServiceability == null ? null : other.currencyServiceability.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CurrencyServiceabilityFlagCriteria copy() {
        return new CurrencyServiceabilityFlagCriteria(this);
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

    public CurrencyServiceabilityFlagTypesFilter getCurrencyServiceabilityFlag() {
        return currencyServiceabilityFlag;
    }

    public CurrencyServiceabilityFlagTypesFilter currencyServiceabilityFlag() {
        if (currencyServiceabilityFlag == null) {
            currencyServiceabilityFlag = new CurrencyServiceabilityFlagTypesFilter();
        }
        return currencyServiceabilityFlag;
    }

    public void setCurrencyServiceabilityFlag(CurrencyServiceabilityFlagTypesFilter currencyServiceabilityFlag) {
        this.currencyServiceabilityFlag = currencyServiceabilityFlag;
    }

    public CurrencyServiceabilityFilter getCurrencyServiceability() {
        return currencyServiceability;
    }

    public CurrencyServiceabilityFilter currencyServiceability() {
        if (currencyServiceability == null) {
            currencyServiceability = new CurrencyServiceabilityFilter();
        }
        return currencyServiceability;
    }

    public void setCurrencyServiceability(CurrencyServiceabilityFilter currencyServiceability) {
        this.currencyServiceability = currencyServiceability;
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
        final CurrencyServiceabilityFlagCriteria that = (CurrencyServiceabilityFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(currencyServiceabilityFlag, that.currencyServiceabilityFlag) &&
            Objects.equals(currencyServiceability, that.currencyServiceability) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyServiceabilityFlag, currencyServiceability, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyServiceabilityFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (currencyServiceabilityFlag != null ? "currencyServiceabilityFlag=" + currencyServiceabilityFlag + ", " : "") +
            (currencyServiceability != null ? "currencyServiceability=" + currencyServiceability + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
