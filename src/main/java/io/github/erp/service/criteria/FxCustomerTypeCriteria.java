package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * Criteria class for the {@link io.github.erp.domain.FxCustomerType} entity. This class is used
 * in {@link io.github.erp.web.rest.FxCustomerTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fx-customer-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FxCustomerTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter foreignExchangeCustomerTypeCode;

    private StringFilter foreignCustomerType;

    private Boolean distinct;

    public FxCustomerTypeCriteria() {}

    public FxCustomerTypeCriteria(FxCustomerTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.foreignExchangeCustomerTypeCode =
            other.foreignExchangeCustomerTypeCode == null ? null : other.foreignExchangeCustomerTypeCode.copy();
        this.foreignCustomerType = other.foreignCustomerType == null ? null : other.foreignCustomerType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FxCustomerTypeCriteria copy() {
        return new FxCustomerTypeCriteria(this);
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

    public StringFilter getForeignExchangeCustomerTypeCode() {
        return foreignExchangeCustomerTypeCode;
    }

    public StringFilter foreignExchangeCustomerTypeCode() {
        if (foreignExchangeCustomerTypeCode == null) {
            foreignExchangeCustomerTypeCode = new StringFilter();
        }
        return foreignExchangeCustomerTypeCode;
    }

    public void setForeignExchangeCustomerTypeCode(StringFilter foreignExchangeCustomerTypeCode) {
        this.foreignExchangeCustomerTypeCode = foreignExchangeCustomerTypeCode;
    }

    public StringFilter getForeignCustomerType() {
        return foreignCustomerType;
    }

    public StringFilter foreignCustomerType() {
        if (foreignCustomerType == null) {
            foreignCustomerType = new StringFilter();
        }
        return foreignCustomerType;
    }

    public void setForeignCustomerType(StringFilter foreignCustomerType) {
        this.foreignCustomerType = foreignCustomerType;
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
        final FxCustomerTypeCriteria that = (FxCustomerTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(foreignExchangeCustomerTypeCode, that.foreignExchangeCustomerTypeCode) &&
            Objects.equals(foreignCustomerType, that.foreignCustomerType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignExchangeCustomerTypeCode, foreignCustomerType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxCustomerTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (foreignExchangeCustomerTypeCode != null ? "foreignExchangeCustomerTypeCode=" + foreignExchangeCustomerTypeCode + ", " : "") +
            (foreignCustomerType != null ? "foreignCustomerType=" + foreignCustomerType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
