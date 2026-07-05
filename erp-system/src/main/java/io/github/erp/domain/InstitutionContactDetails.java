package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InstitutionContactDetails.
 */
@Entity
@Table(name = "institution_contact_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "institutioncontactdetails")
public class InstitutionContactDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @NotNull
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @NotNull
    @Column(name = "contact_type", nullable = false)
    private String contactType;

    @Column(name = "contact_level")
    private String contactLevel;

    @Column(name = "contact_value")
    private String contactValue;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_designation")
    private String contactDesignation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InstitutionContactDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public InstitutionContactDetails entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public InstitutionContactDetails entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getContactType() {
        return this.contactType;
    }

    public InstitutionContactDetails contactType(String contactType) {
        this.setContactType(contactType);
        return this;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactLevel() {
        return this.contactLevel;
    }

    public InstitutionContactDetails contactLevel(String contactLevel) {
        this.setContactLevel(contactLevel);
        return this;
    }

    public void setContactLevel(String contactLevel) {
        this.contactLevel = contactLevel;
    }

    public String getContactValue() {
        return this.contactValue;
    }

    public InstitutionContactDetails contactValue(String contactValue) {
        this.setContactValue(contactValue);
        return this;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getContactName() {
        return this.contactName;
    }

    public InstitutionContactDetails contactName(String contactName) {
        this.setContactName(contactName);
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactDesignation() {
        return this.contactDesignation;
    }

    public InstitutionContactDetails contactDesignation(String contactDesignation) {
        this.setContactDesignation(contactDesignation);
        return this;
    }

    public void setContactDesignation(String contactDesignation) {
        this.contactDesignation = contactDesignation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionContactDetails)) {
            return false;
        }
        return id != null && id.equals(((InstitutionContactDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionContactDetails{" +
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
