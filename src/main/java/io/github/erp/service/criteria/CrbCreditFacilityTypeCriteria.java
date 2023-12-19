package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
 * Criteria class for the {@link io.github.erp.domain.CrbCreditFacilityType} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbCreditFacilityTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-credit-facility-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbCreditFacilityTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter creditFacilityTypeCode;

    private StringFilter creditFacilityType;

    private Boolean distinct;

    public CrbCreditFacilityTypeCriteria() {}

    public CrbCreditFacilityTypeCriteria(CrbCreditFacilityTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creditFacilityTypeCode = other.creditFacilityTypeCode == null ? null : other.creditFacilityTypeCode.copy();
        this.creditFacilityType = other.creditFacilityType == null ? null : other.creditFacilityType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbCreditFacilityTypeCriteria copy() {
        return new CrbCreditFacilityTypeCriteria(this);
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

    public StringFilter getCreditFacilityTypeCode() {
        return creditFacilityTypeCode;
    }

    public StringFilter creditFacilityTypeCode() {
        if (creditFacilityTypeCode == null) {
            creditFacilityTypeCode = new StringFilter();
        }
        return creditFacilityTypeCode;
    }

    public void setCreditFacilityTypeCode(StringFilter creditFacilityTypeCode) {
        this.creditFacilityTypeCode = creditFacilityTypeCode;
    }

    public StringFilter getCreditFacilityType() {
        return creditFacilityType;
    }

    public StringFilter creditFacilityType() {
        if (creditFacilityType == null) {
            creditFacilityType = new StringFilter();
        }
        return creditFacilityType;
    }

    public void setCreditFacilityType(StringFilter creditFacilityType) {
        this.creditFacilityType = creditFacilityType;
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
        final CrbCreditFacilityTypeCriteria that = (CrbCreditFacilityTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creditFacilityTypeCode, that.creditFacilityTypeCode) &&
            Objects.equals(creditFacilityType, that.creditFacilityType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditFacilityTypeCode, creditFacilityType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCreditFacilityTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creditFacilityTypeCode != null ? "creditFacilityTypeCode=" + creditFacilityTypeCode + ", " : "") +
            (creditFacilityType != null ? "creditFacilityType=" + creditFacilityType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
