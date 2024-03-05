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
 * Criteria class for the {@link io.github.erp.domain.SystemModule} entity. This class is used
 * in {@link io.github.erp.web.rest.SystemModuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-modules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemModuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter moduleName;

    private LongFilter placeholderId;

    private LongFilter applicationMappingId;

    private Boolean distinct;

    public SystemModuleCriteria() {}

    public SystemModuleCriteria(SystemModuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.moduleName = other.moduleName == null ? null : other.moduleName.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.applicationMappingId = other.applicationMappingId == null ? null : other.applicationMappingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SystemModuleCriteria copy() {
        return new SystemModuleCriteria(this);
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

    public StringFilter getModuleName() {
        return moduleName;
    }

    public StringFilter moduleName() {
        if (moduleName == null) {
            moduleName = new StringFilter();
        }
        return moduleName;
    }

    public void setModuleName(StringFilter moduleName) {
        this.moduleName = moduleName;
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

    public LongFilter getApplicationMappingId() {
        return applicationMappingId;
    }

    public LongFilter applicationMappingId() {
        if (applicationMappingId == null) {
            applicationMappingId = new LongFilter();
        }
        return applicationMappingId;
    }

    public void setApplicationMappingId(LongFilter applicationMappingId) {
        this.applicationMappingId = applicationMappingId;
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
        final SystemModuleCriteria that = (SystemModuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(moduleName, that.moduleName) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(applicationMappingId, that.applicationMappingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moduleName, placeholderId, applicationMappingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemModuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (moduleName != null ? "moduleName=" + moduleName + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (applicationMappingId != null ? "applicationMappingId=" + applicationMappingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
