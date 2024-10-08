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

import io.github.erp.domain.enumeration.CurrencyAuthenticityFlags;
import io.github.erp.domain.enumeration.CurrencyAuthenticityTypes;
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
 * Criteria class for the {@link io.github.erp.domain.CurrencyAuthenticityFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.CurrencyAuthenticityFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currency-authenticity-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyAuthenticityFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CurrencyAuthenticityFlags
     */
    public static class CurrencyAuthenticityFlagsFilter extends Filter<CurrencyAuthenticityFlags> {

        public CurrencyAuthenticityFlagsFilter() {}

        public CurrencyAuthenticityFlagsFilter(CurrencyAuthenticityFlagsFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyAuthenticityFlagsFilter copy() {
            return new CurrencyAuthenticityFlagsFilter(this);
        }
    }

    /**
     * Class for filtering CurrencyAuthenticityTypes
     */
    public static class CurrencyAuthenticityTypesFilter extends Filter<CurrencyAuthenticityTypes> {

        public CurrencyAuthenticityTypesFilter() {}

        public CurrencyAuthenticityTypesFilter(CurrencyAuthenticityTypesFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyAuthenticityTypesFilter copy() {
            return new CurrencyAuthenticityTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CurrencyAuthenticityFlagsFilter currencyAuthenticityFlag;

    private CurrencyAuthenticityTypesFilter currencyAuthenticityType;

    private Boolean distinct;

    public CurrencyAuthenticityFlagCriteria() {}

    public CurrencyAuthenticityFlagCriteria(CurrencyAuthenticityFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currencyAuthenticityFlag = other.currencyAuthenticityFlag == null ? null : other.currencyAuthenticityFlag.copy();
        this.currencyAuthenticityType = other.currencyAuthenticityType == null ? null : other.currencyAuthenticityType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CurrencyAuthenticityFlagCriteria copy() {
        return new CurrencyAuthenticityFlagCriteria(this);
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

    public CurrencyAuthenticityFlagsFilter getCurrencyAuthenticityFlag() {
        return currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityFlagsFilter currencyAuthenticityFlag() {
        if (currencyAuthenticityFlag == null) {
            currencyAuthenticityFlag = new CurrencyAuthenticityFlagsFilter();
        }
        return currencyAuthenticityFlag;
    }

    public void setCurrencyAuthenticityFlag(CurrencyAuthenticityFlagsFilter currencyAuthenticityFlag) {
        this.currencyAuthenticityFlag = currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityTypesFilter getCurrencyAuthenticityType() {
        return currencyAuthenticityType;
    }

    public CurrencyAuthenticityTypesFilter currencyAuthenticityType() {
        if (currencyAuthenticityType == null) {
            currencyAuthenticityType = new CurrencyAuthenticityTypesFilter();
        }
        return currencyAuthenticityType;
    }

    public void setCurrencyAuthenticityType(CurrencyAuthenticityTypesFilter currencyAuthenticityType) {
        this.currencyAuthenticityType = currencyAuthenticityType;
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
        final CurrencyAuthenticityFlagCriteria that = (CurrencyAuthenticityFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(currencyAuthenticityFlag, that.currencyAuthenticityFlag) &&
            Objects.equals(currencyAuthenticityType, that.currencyAuthenticityType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyAuthenticityFlag, currencyAuthenticityType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyAuthenticityFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (currencyAuthenticityFlag != null ? "currencyAuthenticityFlag=" + currencyAuthenticityFlag + ", " : "") +
            (currencyAuthenticityType != null ? "currencyAuthenticityType=" + currencyAuthenticityType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
