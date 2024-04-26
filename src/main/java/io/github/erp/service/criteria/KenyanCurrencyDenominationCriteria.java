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
