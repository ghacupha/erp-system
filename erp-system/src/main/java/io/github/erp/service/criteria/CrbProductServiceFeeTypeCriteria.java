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
 * Criteria class for the {@link io.github.erp.domain.CrbProductServiceFeeType} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbProductServiceFeeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-product-service-fee-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbProductServiceFeeTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter chargeTypeCode;

    private StringFilter chargeTypeDescription;

    private StringFilter chargeTypeCategory;

    private Boolean distinct;

    public CrbProductServiceFeeTypeCriteria() {}

    public CrbProductServiceFeeTypeCriteria(CrbProductServiceFeeTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.chargeTypeCode = other.chargeTypeCode == null ? null : other.chargeTypeCode.copy();
        this.chargeTypeDescription = other.chargeTypeDescription == null ? null : other.chargeTypeDescription.copy();
        this.chargeTypeCategory = other.chargeTypeCategory == null ? null : other.chargeTypeCategory.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbProductServiceFeeTypeCriteria copy() {
        return new CrbProductServiceFeeTypeCriteria(this);
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

    public StringFilter getChargeTypeCode() {
        return chargeTypeCode;
    }

    public StringFilter chargeTypeCode() {
        if (chargeTypeCode == null) {
            chargeTypeCode = new StringFilter();
        }
        return chargeTypeCode;
    }

    public void setChargeTypeCode(StringFilter chargeTypeCode) {
        this.chargeTypeCode = chargeTypeCode;
    }

    public StringFilter getChargeTypeDescription() {
        return chargeTypeDescription;
    }

    public StringFilter chargeTypeDescription() {
        if (chargeTypeDescription == null) {
            chargeTypeDescription = new StringFilter();
        }
        return chargeTypeDescription;
    }

    public void setChargeTypeDescription(StringFilter chargeTypeDescription) {
        this.chargeTypeDescription = chargeTypeDescription;
    }

    public StringFilter getChargeTypeCategory() {
        return chargeTypeCategory;
    }

    public StringFilter chargeTypeCategory() {
        if (chargeTypeCategory == null) {
            chargeTypeCategory = new StringFilter();
        }
        return chargeTypeCategory;
    }

    public void setChargeTypeCategory(StringFilter chargeTypeCategory) {
        this.chargeTypeCategory = chargeTypeCategory;
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
        final CrbProductServiceFeeTypeCriteria that = (CrbProductServiceFeeTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(chargeTypeCode, that.chargeTypeCode) &&
            Objects.equals(chargeTypeDescription, that.chargeTypeDescription) &&
            Objects.equals(chargeTypeCategory, that.chargeTypeCategory) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chargeTypeCode, chargeTypeDescription, chargeTypeCategory, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbProductServiceFeeTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (chargeTypeCode != null ? "chargeTypeCode=" + chargeTypeCode + ", " : "") +
            (chargeTypeDescription != null ? "chargeTypeDescription=" + chargeTypeDescription + ", " : "") +
            (chargeTypeCategory != null ? "chargeTypeCategory=" + chargeTypeCategory + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
