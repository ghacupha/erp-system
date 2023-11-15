package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * Criteria class for the {@link io.github.erp.domain.CounterPartyDealType} entity. This class is used
 * in {@link io.github.erp.web.rest.CounterPartyDealTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counter-party-deal-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CounterPartyDealTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter counterpartyDealCode;

    private StringFilter counterpartyDealTypeDetails;

    private Boolean distinct;

    public CounterPartyDealTypeCriteria() {}

    public CounterPartyDealTypeCriteria(CounterPartyDealTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.counterpartyDealCode = other.counterpartyDealCode == null ? null : other.counterpartyDealCode.copy();
        this.counterpartyDealTypeDetails = other.counterpartyDealTypeDetails == null ? null : other.counterpartyDealTypeDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CounterPartyDealTypeCriteria copy() {
        return new CounterPartyDealTypeCriteria(this);
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

    public StringFilter getCounterpartyDealCode() {
        return counterpartyDealCode;
    }

    public StringFilter counterpartyDealCode() {
        if (counterpartyDealCode == null) {
            counterpartyDealCode = new StringFilter();
        }
        return counterpartyDealCode;
    }

    public void setCounterpartyDealCode(StringFilter counterpartyDealCode) {
        this.counterpartyDealCode = counterpartyDealCode;
    }

    public StringFilter getCounterpartyDealTypeDetails() {
        return counterpartyDealTypeDetails;
    }

    public StringFilter counterpartyDealTypeDetails() {
        if (counterpartyDealTypeDetails == null) {
            counterpartyDealTypeDetails = new StringFilter();
        }
        return counterpartyDealTypeDetails;
    }

    public void setCounterpartyDealTypeDetails(StringFilter counterpartyDealTypeDetails) {
        this.counterpartyDealTypeDetails = counterpartyDealTypeDetails;
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
        final CounterPartyDealTypeCriteria that = (CounterPartyDealTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(counterpartyDealCode, that.counterpartyDealCode) &&
            Objects.equals(counterpartyDealTypeDetails, that.counterpartyDealTypeDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, counterpartyDealCode, counterpartyDealTypeDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterPartyDealTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (counterpartyDealCode != null ? "counterpartyDealCode=" + counterpartyDealCode + ", " : "") +
            (counterpartyDealTypeDetails != null ? "counterpartyDealTypeDetails=" + counterpartyDealTypeDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
