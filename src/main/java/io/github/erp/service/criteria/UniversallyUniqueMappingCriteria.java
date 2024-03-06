package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * Criteria class for the {@link io.github.erp.domain.UniversallyUniqueMapping} entity. This class is used
 * in {@link io.github.erp.web.rest.UniversallyUniqueMappingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /universally-unique-mappings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UniversallyUniqueMappingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter universalKey;

    private StringFilter mappedValue;

    private LongFilter parentMappingId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public UniversallyUniqueMappingCriteria() {}

    public UniversallyUniqueMappingCriteria(UniversallyUniqueMappingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.universalKey = other.universalKey == null ? null : other.universalKey.copy();
        this.mappedValue = other.mappedValue == null ? null : other.mappedValue.copy();
        this.parentMappingId = other.parentMappingId == null ? null : other.parentMappingId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UniversallyUniqueMappingCriteria copy() {
        return new UniversallyUniqueMappingCriteria(this);
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

    public StringFilter getUniversalKey() {
        return universalKey;
    }

    public StringFilter universalKey() {
        if (universalKey == null) {
            universalKey = new StringFilter();
        }
        return universalKey;
    }

    public void setUniversalKey(StringFilter universalKey) {
        this.universalKey = universalKey;
    }

    public StringFilter getMappedValue() {
        return mappedValue;
    }

    public StringFilter mappedValue() {
        if (mappedValue == null) {
            mappedValue = new StringFilter();
        }
        return mappedValue;
    }

    public void setMappedValue(StringFilter mappedValue) {
        this.mappedValue = mappedValue;
    }

    public LongFilter getParentMappingId() {
        return parentMappingId;
    }

    public LongFilter parentMappingId() {
        if (parentMappingId == null) {
            parentMappingId = new LongFilter();
        }
        return parentMappingId;
    }

    public void setParentMappingId(LongFilter parentMappingId) {
        this.parentMappingId = parentMappingId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
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
        final UniversallyUniqueMappingCriteria that = (UniversallyUniqueMappingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(universalKey, that.universalKey) &&
            Objects.equals(mappedValue, that.mappedValue) &&
            Objects.equals(parentMappingId, that.parentMappingId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, universalKey, mappedValue, parentMappingId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversallyUniqueMappingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (universalKey != null ? "universalKey=" + universalKey + ", " : "") +
            (mappedValue != null ? "mappedValue=" + mappedValue + ", " : "") +
            (parentMappingId != null ? "parentMappingId=" + parentMappingId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
