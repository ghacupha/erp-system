package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
 * Criteria class for the {@link io.github.erp.domain.InstitutionContactDetails} entity. This class is used
 * in {@link io.github.erp.web.rest.InstitutionContactDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /institution-contact-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InstitutionContactDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter entityId;

    private StringFilter entityName;

    private StringFilter contactType;

    private StringFilter contactLevel;

    private StringFilter contactValue;

    private StringFilter contactName;

    private StringFilter contactDesignation;

    private Boolean distinct;

    public InstitutionContactDetailsCriteria() {}

    public InstitutionContactDetailsCriteria(InstitutionContactDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entityId = other.entityId == null ? null : other.entityId.copy();
        this.entityName = other.entityName == null ? null : other.entityName.copy();
        this.contactType = other.contactType == null ? null : other.contactType.copy();
        this.contactLevel = other.contactLevel == null ? null : other.contactLevel.copy();
        this.contactValue = other.contactValue == null ? null : other.contactValue.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.contactDesignation = other.contactDesignation == null ? null : other.contactDesignation.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InstitutionContactDetailsCriteria copy() {
        return new InstitutionContactDetailsCriteria(this);
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

    public StringFilter getEntityId() {
        return entityId;
    }

    public StringFilter entityId() {
        if (entityId == null) {
            entityId = new StringFilter();
        }
        return entityId;
    }

    public void setEntityId(StringFilter entityId) {
        this.entityId = entityId;
    }

    public StringFilter getEntityName() {
        return entityName;
    }

    public StringFilter entityName() {
        if (entityName == null) {
            entityName = new StringFilter();
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public StringFilter getContactType() {
        return contactType;
    }

    public StringFilter contactType() {
        if (contactType == null) {
            contactType = new StringFilter();
        }
        return contactType;
    }

    public void setContactType(StringFilter contactType) {
        this.contactType = contactType;
    }

    public StringFilter getContactLevel() {
        return contactLevel;
    }

    public StringFilter contactLevel() {
        if (contactLevel == null) {
            contactLevel = new StringFilter();
        }
        return contactLevel;
    }

    public void setContactLevel(StringFilter contactLevel) {
        this.contactLevel = contactLevel;
    }

    public StringFilter getContactValue() {
        return contactValue;
    }

    public StringFilter contactValue() {
        if (contactValue == null) {
            contactValue = new StringFilter();
        }
        return contactValue;
    }

    public void setContactValue(StringFilter contactValue) {
        this.contactValue = contactValue;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public StringFilter contactName() {
        if (contactName == null) {
            contactName = new StringFilter();
        }
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getContactDesignation() {
        return contactDesignation;
    }

    public StringFilter contactDesignation() {
        if (contactDesignation == null) {
            contactDesignation = new StringFilter();
        }
        return contactDesignation;
    }

    public void setContactDesignation(StringFilter contactDesignation) {
        this.contactDesignation = contactDesignation;
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
        final InstitutionContactDetailsCriteria that = (InstitutionContactDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entityId, that.entityId) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(contactType, that.contactType) &&
            Objects.equals(contactLevel, that.contactLevel) &&
            Objects.equals(contactValue, that.contactValue) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(contactDesignation, that.contactDesignation) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityId, entityName, contactType, contactLevel, contactValue, contactName, contactDesignation, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionContactDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entityId != null ? "entityId=" + entityId + ", " : "") +
            (entityName != null ? "entityName=" + entityName + ", " : "") +
            (contactType != null ? "contactType=" + contactType + ", " : "") +
            (contactLevel != null ? "contactLevel=" + contactLevel + ", " : "") +
            (contactValue != null ? "contactValue=" + contactValue + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (contactDesignation != null ? "contactDesignation=" + contactDesignation + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
