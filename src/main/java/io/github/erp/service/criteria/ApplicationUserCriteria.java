package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.9-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.ApplicationUser} entity. This class is used
 * in {@link io.github.erp.web.rest.ApplicationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter designation;

    private StringFilter applicationIdentity;

    private LongFilter organizationId;

    private LongFilter departmentId;

    private LongFilter securityClearanceId;

    private LongFilter systemIdentityId;

    private LongFilter userPropertiesId;

    private LongFilter dealerIdentityId;

    private Boolean distinct;

    public ApplicationUserCriteria() {}

    public ApplicationUserCriteria(ApplicationUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.applicationIdentity = other.applicationIdentity == null ? null : other.applicationIdentity.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.systemIdentityId = other.systemIdentityId == null ? null : other.systemIdentityId.copy();
        this.userPropertiesId = other.userPropertiesId == null ? null : other.userPropertiesId.copy();
        this.dealerIdentityId = other.dealerIdentityId == null ? null : other.dealerIdentityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationUserCriteria copy() {
        return new ApplicationUserCriteria(this);
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

    public UUIDFilter getDesignation() {
        return designation;
    }

    public UUIDFilter designation() {
        if (designation == null) {
            designation = new UUIDFilter();
        }
        return designation;
    }

    public void setDesignation(UUIDFilter designation) {
        this.designation = designation;
    }

    public StringFilter getApplicationIdentity() {
        return applicationIdentity;
    }

    public StringFilter applicationIdentity() {
        if (applicationIdentity == null) {
            applicationIdentity = new StringFilter();
        }
        return applicationIdentity;
    }

    public void setApplicationIdentity(StringFilter applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public LongFilter getSystemIdentityId() {
        return systemIdentityId;
    }

    public LongFilter systemIdentityId() {
        if (systemIdentityId == null) {
            systemIdentityId = new LongFilter();
        }
        return systemIdentityId;
    }

    public void setSystemIdentityId(LongFilter systemIdentityId) {
        this.systemIdentityId = systemIdentityId;
    }

    public LongFilter getUserPropertiesId() {
        return userPropertiesId;
    }

    public LongFilter userPropertiesId() {
        if (userPropertiesId == null) {
            userPropertiesId = new LongFilter();
        }
        return userPropertiesId;
    }

    public void setUserPropertiesId(LongFilter userPropertiesId) {
        this.userPropertiesId = userPropertiesId;
    }

    public LongFilter getDealerIdentityId() {
        return dealerIdentityId;
    }

    public LongFilter dealerIdentityId() {
        if (dealerIdentityId == null) {
            dealerIdentityId = new LongFilter();
        }
        return dealerIdentityId;
    }

    public void setDealerIdentityId(LongFilter dealerIdentityId) {
        this.dealerIdentityId = dealerIdentityId;
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
        final ApplicationUserCriteria that = (ApplicationUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(applicationIdentity, that.applicationIdentity) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(systemIdentityId, that.systemIdentityId) &&
            Objects.equals(userPropertiesId, that.userPropertiesId) &&
            Objects.equals(dealerIdentityId, that.dealerIdentityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            designation,
            applicationIdentity,
            organizationId,
            departmentId,
            securityClearanceId,
            systemIdentityId,
            userPropertiesId,
            dealerIdentityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (designation != null ? "designation=" + designation + ", " : "") +
            (applicationIdentity != null ? "applicationIdentity=" + applicationIdentity + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (systemIdentityId != null ? "systemIdentityId=" + systemIdentityId + ", " : "") +
            (userPropertiesId != null ? "userPropertiesId=" + userPropertiesId + ", " : "") +
            (dealerIdentityId != null ? "dealerIdentityId=" + dealerIdentityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
