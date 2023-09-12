package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
 * Criteria class for the {@link io.github.erp.domain.BusinessSegmentTypes} entity. This class is used
 * in {@link io.github.erp.web.rest.BusinessSegmentTypesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /business-segment-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusinessSegmentTypesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter businessEconomicSegmentCode;

    private StringFilter businessEconomicSegment;

    private Boolean distinct;

    public BusinessSegmentTypesCriteria() {}

    public BusinessSegmentTypesCriteria(BusinessSegmentTypesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.businessEconomicSegmentCode = other.businessEconomicSegmentCode == null ? null : other.businessEconomicSegmentCode.copy();
        this.businessEconomicSegment = other.businessEconomicSegment == null ? null : other.businessEconomicSegment.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusinessSegmentTypesCriteria copy() {
        return new BusinessSegmentTypesCriteria(this);
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

    public StringFilter getBusinessEconomicSegmentCode() {
        return businessEconomicSegmentCode;
    }

    public StringFilter businessEconomicSegmentCode() {
        if (businessEconomicSegmentCode == null) {
            businessEconomicSegmentCode = new StringFilter();
        }
        return businessEconomicSegmentCode;
    }

    public void setBusinessEconomicSegmentCode(StringFilter businessEconomicSegmentCode) {
        this.businessEconomicSegmentCode = businessEconomicSegmentCode;
    }

    public StringFilter getBusinessEconomicSegment() {
        return businessEconomicSegment;
    }

    public StringFilter businessEconomicSegment() {
        if (businessEconomicSegment == null) {
            businessEconomicSegment = new StringFilter();
        }
        return businessEconomicSegment;
    }

    public void setBusinessEconomicSegment(StringFilter businessEconomicSegment) {
        this.businessEconomicSegment = businessEconomicSegment;
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
        final BusinessSegmentTypesCriteria that = (BusinessSegmentTypesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(businessEconomicSegmentCode, that.businessEconomicSegmentCode) &&
            Objects.equals(businessEconomicSegment, that.businessEconomicSegment) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, businessEconomicSegmentCode, businessEconomicSegment, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessSegmentTypesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (businessEconomicSegmentCode != null ? "businessEconomicSegmentCode=" + businessEconomicSegmentCode + ", " : "") +
            (businessEconomicSegment != null ? "businessEconomicSegment=" + businessEconomicSegment + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
