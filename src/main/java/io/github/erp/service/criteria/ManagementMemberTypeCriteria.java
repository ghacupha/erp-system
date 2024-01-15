package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
 * Criteria class for the {@link io.github.erp.domain.ManagementMemberType} entity. This class is used
 * in {@link io.github.erp.web.rest.ManagementMemberTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /management-member-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ManagementMemberTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter managementMemberTypeCode;

    private StringFilter managementMemberType;

    private Boolean distinct;

    public ManagementMemberTypeCriteria() {}

    public ManagementMemberTypeCriteria(ManagementMemberTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.managementMemberTypeCode = other.managementMemberTypeCode == null ? null : other.managementMemberTypeCode.copy();
        this.managementMemberType = other.managementMemberType == null ? null : other.managementMemberType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ManagementMemberTypeCriteria copy() {
        return new ManagementMemberTypeCriteria(this);
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

    public StringFilter getManagementMemberTypeCode() {
        return managementMemberTypeCode;
    }

    public StringFilter managementMemberTypeCode() {
        if (managementMemberTypeCode == null) {
            managementMemberTypeCode = new StringFilter();
        }
        return managementMemberTypeCode;
    }

    public void setManagementMemberTypeCode(StringFilter managementMemberTypeCode) {
        this.managementMemberTypeCode = managementMemberTypeCode;
    }

    public StringFilter getManagementMemberType() {
        return managementMemberType;
    }

    public StringFilter managementMemberType() {
        if (managementMemberType == null) {
            managementMemberType = new StringFilter();
        }
        return managementMemberType;
    }

    public void setManagementMemberType(StringFilter managementMemberType) {
        this.managementMemberType = managementMemberType;
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
        final ManagementMemberTypeCriteria that = (ManagementMemberTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(managementMemberTypeCode, that.managementMemberTypeCode) &&
            Objects.equals(managementMemberType, that.managementMemberType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, managementMemberTypeCode, managementMemberType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagementMemberTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (managementMemberTypeCode != null ? "managementMemberTypeCode=" + managementMemberTypeCode + ", " : "") +
            (managementMemberType != null ? "managementMemberType=" + managementMemberType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
