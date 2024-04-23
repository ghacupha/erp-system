package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.enumeration.genderTypes;
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
 * Criteria class for the {@link io.github.erp.domain.GenderType} entity. This class is used
 * in {@link io.github.erp.web.rest.GenderTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gender-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenderTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering genderTypes
     */
    public static class genderTypesFilter extends Filter<genderTypes> {

        public genderTypesFilter() {}

        public genderTypesFilter(genderTypesFilter filter) {
            super(filter);
        }

        @Override
        public genderTypesFilter copy() {
            return new genderTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter genderCode;

    private genderTypesFilter genderType;

    private Boolean distinct;

    public GenderTypeCriteria() {}

    public GenderTypeCriteria(GenderTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.genderCode = other.genderCode == null ? null : other.genderCode.copy();
        this.genderType = other.genderType == null ? null : other.genderType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenderTypeCriteria copy() {
        return new GenderTypeCriteria(this);
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

    public StringFilter getGenderCode() {
        return genderCode;
    }

    public StringFilter genderCode() {
        if (genderCode == null) {
            genderCode = new StringFilter();
        }
        return genderCode;
    }

    public void setGenderCode(StringFilter genderCode) {
        this.genderCode = genderCode;
    }

    public genderTypesFilter getGenderType() {
        return genderType;
    }

    public genderTypesFilter genderType() {
        if (genderType == null) {
            genderType = new genderTypesFilter();
        }
        return genderType;
    }

    public void setGenderType(genderTypesFilter genderType) {
        this.genderType = genderType;
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
        final GenderTypeCriteria that = (GenderTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(genderCode, that.genderCode) &&
            Objects.equals(genderType, that.genderType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genderCode, genderType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenderTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (genderCode != null ? "genderCode=" + genderCode + ", " : "") +
            (genderType != null ? "genderType=" + genderType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
