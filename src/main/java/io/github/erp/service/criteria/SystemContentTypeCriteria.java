package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.enumeration.SystemContentTypeAvailability;
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
 * Criteria class for the {@link io.github.erp.domain.SystemContentType} entity. This class is used
 * in {@link io.github.erp.web.rest.SystemContentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-content-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemContentTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SystemContentTypeAvailability
     */
    public static class SystemContentTypeAvailabilityFilter extends Filter<SystemContentTypeAvailability> {

        public SystemContentTypeAvailabilityFilter() {}

        public SystemContentTypeAvailabilityFilter(SystemContentTypeAvailabilityFilter filter) {
            super(filter);
        }

        @Override
        public SystemContentTypeAvailabilityFilter copy() {
            return new SystemContentTypeAvailabilityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contentTypeName;

    private StringFilter contentTypeHeader;

    private SystemContentTypeAvailabilityFilter availability;

    private LongFilter placeholdersId;

    private LongFilter sysMapsId;

    private Boolean distinct;

    public SystemContentTypeCriteria() {}

    public SystemContentTypeCriteria(SystemContentTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contentTypeName = other.contentTypeName == null ? null : other.contentTypeName.copy();
        this.contentTypeHeader = other.contentTypeHeader == null ? null : other.contentTypeHeader.copy();
        this.availability = other.availability == null ? null : other.availability.copy();
        this.placeholdersId = other.placeholdersId == null ? null : other.placeholdersId.copy();
        this.sysMapsId = other.sysMapsId == null ? null : other.sysMapsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SystemContentTypeCriteria copy() {
        return new SystemContentTypeCriteria(this);
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

    public StringFilter getContentTypeName() {
        return contentTypeName;
    }

    public StringFilter contentTypeName() {
        if (contentTypeName == null) {
            contentTypeName = new StringFilter();
        }
        return contentTypeName;
    }

    public void setContentTypeName(StringFilter contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public StringFilter getContentTypeHeader() {
        return contentTypeHeader;
    }

    public StringFilter contentTypeHeader() {
        if (contentTypeHeader == null) {
            contentTypeHeader = new StringFilter();
        }
        return contentTypeHeader;
    }

    public void setContentTypeHeader(StringFilter contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public SystemContentTypeAvailabilityFilter getAvailability() {
        return availability;
    }

    public SystemContentTypeAvailabilityFilter availability() {
        if (availability == null) {
            availability = new SystemContentTypeAvailabilityFilter();
        }
        return availability;
    }

    public void setAvailability(SystemContentTypeAvailabilityFilter availability) {
        this.availability = availability;
    }

    public LongFilter getPlaceholdersId() {
        return placeholdersId;
    }

    public LongFilter placeholdersId() {
        if (placeholdersId == null) {
            placeholdersId = new LongFilter();
        }
        return placeholdersId;
    }

    public void setPlaceholdersId(LongFilter placeholdersId) {
        this.placeholdersId = placeholdersId;
    }

    public LongFilter getSysMapsId() {
        return sysMapsId;
    }

    public LongFilter sysMapsId() {
        if (sysMapsId == null) {
            sysMapsId = new LongFilter();
        }
        return sysMapsId;
    }

    public void setSysMapsId(LongFilter sysMapsId) {
        this.sysMapsId = sysMapsId;
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
        final SystemContentTypeCriteria that = (SystemContentTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contentTypeName, that.contentTypeName) &&
            Objects.equals(contentTypeHeader, that.contentTypeHeader) &&
            Objects.equals(availability, that.availability) &&
            Objects.equals(placeholdersId, that.placeholdersId) &&
            Objects.equals(sysMapsId, that.sysMapsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentTypeName, contentTypeHeader, availability, placeholdersId, sysMapsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemContentTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (contentTypeName != null ? "contentTypeName=" + contentTypeName + ", " : "") +
            (contentTypeHeader != null ? "contentTypeHeader=" + contentTypeHeader + ", " : "") +
            (availability != null ? "availability=" + availability + ", " : "") +
            (placeholdersId != null ? "placeholdersId=" + placeholdersId + ", " : "") +
            (sysMapsId != null ? "sysMapsId=" + sysMapsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
