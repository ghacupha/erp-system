package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.KenyanCurrencyDenomination} entity. This class is used
 * in {@link io.github.erp.web.rest.KenyanCurrencyDenominationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /kenyan-currency-denominations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KenyanCurrencyDenominationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter currencyDenominationCode;

    private StringFilter currencyDenominationType;

    private StringFilter currencyDenominationTypeDetails;

    private Boolean distinct;

    public KenyanCurrencyDenominationCriteria() {}

    public KenyanCurrencyDenominationCriteria(KenyanCurrencyDenominationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currencyDenominationCode = other.currencyDenominationCode == null ? null : other.currencyDenominationCode.copy();
        this.currencyDenominationType = other.currencyDenominationType == null ? null : other.currencyDenominationType.copy();
        this.currencyDenominationTypeDetails =
            other.currencyDenominationTypeDetails == null ? null : other.currencyDenominationTypeDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public KenyanCurrencyDenominationCriteria copy() {
        return new KenyanCurrencyDenominationCriteria(this);
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

    public StringFilter getCurrencyDenominationCode() {
        return currencyDenominationCode;
    }

    public StringFilter currencyDenominationCode() {
        if (currencyDenominationCode == null) {
            currencyDenominationCode = new StringFilter();
        }
        return currencyDenominationCode;
    }

    public void setCurrencyDenominationCode(StringFilter currencyDenominationCode) {
        this.currencyDenominationCode = currencyDenominationCode;
    }

    public StringFilter getCurrencyDenominationType() {
        return currencyDenominationType;
    }

    public StringFilter currencyDenominationType() {
        if (currencyDenominationType == null) {
            currencyDenominationType = new StringFilter();
        }
        return currencyDenominationType;
    }

    public void setCurrencyDenominationType(StringFilter currencyDenominationType) {
        this.currencyDenominationType = currencyDenominationType;
    }

    public StringFilter getCurrencyDenominationTypeDetails() {
        return currencyDenominationTypeDetails;
    }

    public StringFilter currencyDenominationTypeDetails() {
        if (currencyDenominationTypeDetails == null) {
            currencyDenominationTypeDetails = new StringFilter();
        }
        return currencyDenominationTypeDetails;
    }

    public void setCurrencyDenominationTypeDetails(StringFilter currencyDenominationTypeDetails) {
        this.currencyDenominationTypeDetails = currencyDenominationTypeDetails;
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
        final KenyanCurrencyDenominationCriteria that = (KenyanCurrencyDenominationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(currencyDenominationCode, that.currencyDenominationCode) &&
            Objects.equals(currencyDenominationType, that.currencyDenominationType) &&
            Objects.equals(currencyDenominationTypeDetails, that.currencyDenominationTypeDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyDenominationCode, currencyDenominationType, currencyDenominationTypeDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KenyanCurrencyDenominationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (currencyDenominationCode != null ? "currencyDenominationCode=" + currencyDenominationCode + ", " : "") +
            (currencyDenominationType != null ? "currencyDenominationType=" + currencyDenominationType + ", " : "") +
            (currencyDenominationTypeDetails != null ? "currencyDenominationTypeDetails=" + currencyDenominationTypeDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
