package io.github.erp.service.dto;

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
