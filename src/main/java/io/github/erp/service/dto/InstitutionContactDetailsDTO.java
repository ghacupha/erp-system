package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.InstitutionContactDetails} entity.
 */
public class InstitutionContactDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String entityId;

    @NotNull
    private String entityName;

    @NotNull
    private String contactType;

    private String contactLevel;

    private String contactValue;

    private String contactName;

    private String contactDesignation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactLevel() {
        return contactLevel;
    }

    public void setContactLevel(String contactLevel) {
        this.contactLevel = contactLevel;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactDesignation() {
        return contactDesignation;
    }

    public void setContactDesignation(String contactDesignation) {
        this.contactDesignation = contactDesignation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionContactDetailsDTO)) {
            return false;
        }

        InstitutionContactDetailsDTO institutionContactDetailsDTO = (InstitutionContactDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionContactDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionContactDetailsDTO{" +
            "id=" + getId() +
            ", entityId='" + getEntityId() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", contactType='" + getContactType() + "'" +
            ", contactLevel='" + getContactLevel() + "'" +
            ", contactValue='" + getContactValue() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", contactDesignation='" + getContactDesignation() + "'" +
            "}";
    }
}
