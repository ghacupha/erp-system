package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A SecurityClassificationType.
 */
@Entity
@Table(name = "security_classification_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "securityclassificationtype")
public class SecurityClassificationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "security_classification_type_code", nullable = false, unique = true)
    private String securityClassificationTypeCode;

    @NotNull
    @Column(name = "security_classification_type", nullable = false, unique = true)
    private String securityClassificationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "security_classification_details")
    private String securityClassificationDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityClassificationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityClassificationTypeCode() {
        return this.securityClassificationTypeCode;
    }

    public SecurityClassificationType securityClassificationTypeCode(String securityClassificationTypeCode) {
        this.setSecurityClassificationTypeCode(securityClassificationTypeCode);
        return this;
    }

    public void setSecurityClassificationTypeCode(String securityClassificationTypeCode) {
        this.securityClassificationTypeCode = securityClassificationTypeCode;
    }

    public String getSecurityClassificationType() {
        return this.securityClassificationType;
    }

    public SecurityClassificationType securityClassificationType(String securityClassificationType) {
        this.setSecurityClassificationType(securityClassificationType);
        return this;
    }

    public void setSecurityClassificationType(String securityClassificationType) {
        this.securityClassificationType = securityClassificationType;
    }

    public String getSecurityClassificationDetails() {
        return this.securityClassificationDetails;
    }

    public SecurityClassificationType securityClassificationDetails(String securityClassificationDetails) {
        this.setSecurityClassificationDetails(securityClassificationDetails);
        return this;
    }

    public void setSecurityClassificationDetails(String securityClassificationDetails) {
        this.securityClassificationDetails = securityClassificationDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityClassificationType)) {
            return false;
        }
        return id != null && id.equals(((SecurityClassificationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityClassificationType{" +
            "id=" + getId() +
            ", securityClassificationTypeCode='" + getSecurityClassificationTypeCode() + "'" +
            ", securityClassificationType='" + getSecurityClassificationType() + "'" +
            ", securityClassificationDetails='" + getSecurityClassificationDetails() + "'" +
            "}";
    }
}
