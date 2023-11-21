package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
 * Criteria class for the {@link io.github.erp.domain.CollateralType} entity. This class is used
 * in {@link io.github.erp.web.rest.CollateralTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collateral-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollateralTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter collateralTypeCode;

    private StringFilter collateralType;

    private Boolean distinct;

    public CollateralTypeCriteria() {}

    public CollateralTypeCriteria(CollateralTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.collateralTypeCode = other.collateralTypeCode == null ? null : other.collateralTypeCode.copy();
        this.collateralType = other.collateralType == null ? null : other.collateralType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CollateralTypeCriteria copy() {
        return new CollateralTypeCriteria(this);
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

    public StringFilter getCollateralTypeCode() {
        return collateralTypeCode;
    }

    public StringFilter collateralTypeCode() {
        if (collateralTypeCode == null) {
            collateralTypeCode = new StringFilter();
        }
        return collateralTypeCode;
    }

    public void setCollateralTypeCode(StringFilter collateralTypeCode) {
        this.collateralTypeCode = collateralTypeCode;
    }

    public StringFilter getCollateralType() {
        return collateralType;
    }

    public StringFilter collateralType() {
        if (collateralType == null) {
            collateralType = new StringFilter();
        }
        return collateralType;
    }

    public void setCollateralType(StringFilter collateralType) {
        this.collateralType = collateralType;
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
        final CollateralTypeCriteria that = (CollateralTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(collateralTypeCode, that.collateralTypeCode) &&
            Objects.equals(collateralType, that.collateralType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, collateralTypeCode, collateralType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (collateralTypeCode != null ? "collateralTypeCode=" + collateralTypeCode + ", " : "") +
            (collateralType != null ? "collateralType=" + collateralType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
