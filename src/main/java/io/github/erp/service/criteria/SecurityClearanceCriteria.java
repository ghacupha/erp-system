package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
 * Criteria class for the {@link io.github.erp.domain.SecurityClearance} entity. This class is used
 * in {@link io.github.erp.web.rest.SecurityClearanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /security-clearances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SecurityClearanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter clearanceLevel;

    private LongFilter grantedClearancesId;

    private LongFilter placeholderId;

    private LongFilter systemParametersId;

    private Boolean distinct;

    public SecurityClearanceCriteria() {}

    public SecurityClearanceCriteria(SecurityClearanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clearanceLevel = other.clearanceLevel == null ? null : other.clearanceLevel.copy();
        this.grantedClearancesId = other.grantedClearancesId == null ? null : other.grantedClearancesId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.systemParametersId = other.systemParametersId == null ? null : other.systemParametersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SecurityClearanceCriteria copy() {
        return new SecurityClearanceCriteria(this);
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

    public StringFilter getClearanceLevel() {
        return clearanceLevel;
    }

    public StringFilter clearanceLevel() {
        if (clearanceLevel == null) {
            clearanceLevel = new StringFilter();
        }
        return clearanceLevel;
    }

    public void setClearanceLevel(StringFilter clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
    }

    public LongFilter getGrantedClearancesId() {
        return grantedClearancesId;
    }

    public LongFilter grantedClearancesId() {
        if (grantedClearancesId == null) {
            grantedClearancesId = new LongFilter();
        }
        return grantedClearancesId;
    }

    public void setGrantedClearancesId(LongFilter grantedClearancesId) {
        this.grantedClearancesId = grantedClearancesId;
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

    public LongFilter getSystemParametersId() {
        return systemParametersId;
    }

    public LongFilter systemParametersId() {
        if (systemParametersId == null) {
            systemParametersId = new LongFilter();
        }
        return systemParametersId;
    }

    public void setSystemParametersId(LongFilter systemParametersId) {
        this.systemParametersId = systemParametersId;
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
        final SecurityClearanceCriteria that = (SecurityClearanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clearanceLevel, that.clearanceLevel) &&
            Objects.equals(grantedClearancesId, that.grantedClearancesId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(systemParametersId, that.systemParametersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clearanceLevel, grantedClearancesId, placeholderId, systemParametersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityClearanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (clearanceLevel != null ? "clearanceLevel=" + clearanceLevel + ", " : "") +
            (grantedClearancesId != null ? "grantedClearancesId=" + grantedClearancesId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (systemParametersId != null ? "systemParametersId=" + systemParametersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
