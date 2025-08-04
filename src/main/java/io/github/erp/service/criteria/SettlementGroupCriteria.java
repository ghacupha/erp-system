package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * Criteria class for the {@link io.github.erp.domain.SettlementGroup} entity. This class is used
 * in {@link io.github.erp.web.rest.SettlementGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /settlement-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SettlementGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter groupName;

    private StringFilter description;

    private LongFilter parentGroupId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public SettlementGroupCriteria() {}

    public SettlementGroupCriteria(SettlementGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.groupName = other.groupName == null ? null : other.groupName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.parentGroupId = other.parentGroupId == null ? null : other.parentGroupId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SettlementGroupCriteria copy() {
        return new SettlementGroupCriteria(this);
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

    public StringFilter getGroupName() {
        return groupName;
    }

    public StringFilter groupName() {
        if (groupName == null) {
            groupName = new StringFilter();
        }
        return groupName;
    }

    public void setGroupName(StringFilter groupName) {
        this.groupName = groupName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getParentGroupId() {
        return parentGroupId;
    }

    public LongFilter parentGroupId() {
        if (parentGroupId == null) {
            parentGroupId = new LongFilter();
        }
        return parentGroupId;
    }

    public void setParentGroupId(LongFilter parentGroupId) {
        this.parentGroupId = parentGroupId;
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
        final SettlementGroupCriteria that = (SettlementGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(groupName, that.groupName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(parentGroupId, that.parentGroupId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, description, parentGroupId, placeholderId, distinct);
    }

    @Override
    public String toString() {
        return "SettlementGroupCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (groupName != null ? "groupName=" + groupName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (parentGroupId != null ? "parentGroupId=" + parentGroupId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
