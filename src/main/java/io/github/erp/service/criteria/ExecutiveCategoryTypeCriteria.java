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
 * Criteria class for the {@link io.github.erp.domain.ExecutiveCategoryType} entity. This class is used
 * in {@link io.github.erp.web.rest.ExecutiveCategoryTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /executive-category-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExecutiveCategoryTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter directorCategoryTypeCode;

    private StringFilter directorCategoryType;

    private Boolean distinct;

    public ExecutiveCategoryTypeCriteria() {}

    public ExecutiveCategoryTypeCriteria(ExecutiveCategoryTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.directorCategoryTypeCode = other.directorCategoryTypeCode == null ? null : other.directorCategoryTypeCode.copy();
        this.directorCategoryType = other.directorCategoryType == null ? null : other.directorCategoryType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExecutiveCategoryTypeCriteria copy() {
        return new ExecutiveCategoryTypeCriteria(this);
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

    public StringFilter getDirectorCategoryTypeCode() {
        return directorCategoryTypeCode;
    }

    public StringFilter directorCategoryTypeCode() {
        if (directorCategoryTypeCode == null) {
            directorCategoryTypeCode = new StringFilter();
        }
        return directorCategoryTypeCode;
    }

    public void setDirectorCategoryTypeCode(StringFilter directorCategoryTypeCode) {
        this.directorCategoryTypeCode = directorCategoryTypeCode;
    }

    public StringFilter getDirectorCategoryType() {
        return directorCategoryType;
    }

    public StringFilter directorCategoryType() {
        if (directorCategoryType == null) {
            directorCategoryType = new StringFilter();
        }
        return directorCategoryType;
    }

    public void setDirectorCategoryType(StringFilter directorCategoryType) {
        this.directorCategoryType = directorCategoryType;
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
        final ExecutiveCategoryTypeCriteria that = (ExecutiveCategoryTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(directorCategoryTypeCode, that.directorCategoryTypeCode) &&
            Objects.equals(directorCategoryType, that.directorCategoryType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, directorCategoryTypeCode, directorCategoryType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutiveCategoryTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (directorCategoryTypeCode != null ? "directorCategoryTypeCode=" + directorCategoryTypeCode + ", " : "") +
            (directorCategoryType != null ? "directorCategoryType=" + directorCategoryType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
