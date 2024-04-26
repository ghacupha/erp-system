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
