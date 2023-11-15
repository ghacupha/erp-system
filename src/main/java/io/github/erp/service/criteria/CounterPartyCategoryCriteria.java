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
import io.github.erp.domain.enumeration.CounterpartyCategory;
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
 * Criteria class for the {@link io.github.erp.domain.CounterPartyCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.CounterPartyCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counter-party-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CounterPartyCategoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CounterpartyCategory
     */
    public static class CounterpartyCategoryFilter extends Filter<CounterpartyCategory> {

        public CounterpartyCategoryFilter() {}

        public CounterpartyCategoryFilter(CounterpartyCategoryFilter filter) {
            super(filter);
        }

        @Override
        public CounterpartyCategoryFilter copy() {
            return new CounterpartyCategoryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter counterpartyCategoryCode;

    private CounterpartyCategoryFilter counterpartyCategoryCodeDetails;

    private Boolean distinct;

    public CounterPartyCategoryCriteria() {}

    public CounterPartyCategoryCriteria(CounterPartyCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.counterpartyCategoryCode = other.counterpartyCategoryCode == null ? null : other.counterpartyCategoryCode.copy();
        this.counterpartyCategoryCodeDetails =
            other.counterpartyCategoryCodeDetails == null ? null : other.counterpartyCategoryCodeDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CounterPartyCategoryCriteria copy() {
        return new CounterPartyCategoryCriteria(this);
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

    public StringFilter getCounterpartyCategoryCode() {
        return counterpartyCategoryCode;
    }

    public StringFilter counterpartyCategoryCode() {
        if (counterpartyCategoryCode == null) {
            counterpartyCategoryCode = new StringFilter();
        }
        return counterpartyCategoryCode;
    }

    public void setCounterpartyCategoryCode(StringFilter counterpartyCategoryCode) {
        this.counterpartyCategoryCode = counterpartyCategoryCode;
    }

    public CounterpartyCategoryFilter getCounterpartyCategoryCodeDetails() {
        return counterpartyCategoryCodeDetails;
    }

    public CounterpartyCategoryFilter counterpartyCategoryCodeDetails() {
        if (counterpartyCategoryCodeDetails == null) {
            counterpartyCategoryCodeDetails = new CounterpartyCategoryFilter();
        }
        return counterpartyCategoryCodeDetails;
    }

    public void setCounterpartyCategoryCodeDetails(CounterpartyCategoryFilter counterpartyCategoryCodeDetails) {
        this.counterpartyCategoryCodeDetails = counterpartyCategoryCodeDetails;
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
        final CounterPartyCategoryCriteria that = (CounterPartyCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(counterpartyCategoryCode, that.counterpartyCategoryCode) &&
            Objects.equals(counterpartyCategoryCodeDetails, that.counterpartyCategoryCodeDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, counterpartyCategoryCode, counterpartyCategoryCodeDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterPartyCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (counterpartyCategoryCode != null ? "counterpartyCategoryCode=" + counterpartyCategoryCode + ", " : "") +
            (counterpartyCategoryCodeDetails != null ? "counterpartyCategoryCodeDetails=" + counterpartyCategoryCodeDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
