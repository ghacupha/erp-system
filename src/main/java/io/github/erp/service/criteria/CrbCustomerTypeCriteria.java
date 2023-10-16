package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
 * Criteria class for the {@link io.github.erp.domain.CrbCustomerType} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbCustomerTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-customer-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbCustomerTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerTypeCode;

    private StringFilter customerType;

    private Boolean distinct;

    public CrbCustomerTypeCriteria() {}

    public CrbCustomerTypeCriteria(CrbCustomerTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerTypeCode = other.customerTypeCode == null ? null : other.customerTypeCode.copy();
        this.customerType = other.customerType == null ? null : other.customerType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbCustomerTypeCriteria copy() {
        return new CrbCustomerTypeCriteria(this);
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

    public StringFilter getCustomerTypeCode() {
        return customerTypeCode;
    }

    public StringFilter customerTypeCode() {
        if (customerTypeCode == null) {
            customerTypeCode = new StringFilter();
        }
        return customerTypeCode;
    }

    public void setCustomerTypeCode(StringFilter customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public StringFilter getCustomerType() {
        return customerType;
    }

    public StringFilter customerType() {
        if (customerType == null) {
            customerType = new StringFilter();
        }
        return customerType;
    }

    public void setCustomerType(StringFilter customerType) {
        this.customerType = customerType;
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
        final CrbCustomerTypeCriteria that = (CrbCustomerTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerTypeCode, that.customerTypeCode) &&
            Objects.equals(customerType, that.customerType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerTypeCode, customerType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCustomerTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerTypeCode != null ? "customerTypeCode=" + customerTypeCode + ", " : "") +
            (customerType != null ? "customerType=" + customerType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
