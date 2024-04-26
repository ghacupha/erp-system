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
