package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
 * Criteria class for the {@link io.github.erp.domain.IsoCurrencyCode} entity. This class is used
 * in {@link io.github.erp.web.rest.IsoCurrencyCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /iso-currency-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IsoCurrencyCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter alphabeticCode;

    private StringFilter numericCode;

    private StringFilter minorUnit;

    private StringFilter currency;

    private StringFilter country;

    private Boolean distinct;

    public IsoCurrencyCodeCriteria() {}

    public IsoCurrencyCodeCriteria(IsoCurrencyCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.alphabeticCode = other.alphabeticCode == null ? null : other.alphabeticCode.copy();
        this.numericCode = other.numericCode == null ? null : other.numericCode.copy();
        this.minorUnit = other.minorUnit == null ? null : other.minorUnit.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IsoCurrencyCodeCriteria copy() {
        return new IsoCurrencyCodeCriteria(this);
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

    public StringFilter getAlphabeticCode() {
        return alphabeticCode;
    }

    public StringFilter alphabeticCode() {
        if (alphabeticCode == null) {
            alphabeticCode = new StringFilter();
        }
        return alphabeticCode;
    }

    public void setAlphabeticCode(StringFilter alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public StringFilter getNumericCode() {
        return numericCode;
    }

    public StringFilter numericCode() {
        if (numericCode == null) {
            numericCode = new StringFilter();
        }
        return numericCode;
    }

    public void setNumericCode(StringFilter numericCode) {
        this.numericCode = numericCode;
    }

    public StringFilter getMinorUnit() {
        return minorUnit;
    }

    public StringFilter minorUnit() {
        if (minorUnit == null) {
            minorUnit = new StringFilter();
        }
        return minorUnit;
    }

    public void setMinorUnit(StringFilter minorUnit) {
        this.minorUnit = minorUnit;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
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
        final IsoCurrencyCodeCriteria that = (IsoCurrencyCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(alphabeticCode, that.alphabeticCode) &&
            Objects.equals(numericCode, that.numericCode) &&
            Objects.equals(minorUnit, that.minorUnit) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(country, that.country) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alphabeticCode, numericCode, minorUnit, currency, country, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCurrencyCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (alphabeticCode != null ? "alphabeticCode=" + alphabeticCode + ", " : "") +
            (numericCode != null ? "numericCode=" + numericCode + ", " : "") +
            (minorUnit != null ? "minorUnit=" + minorUnit + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
