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
