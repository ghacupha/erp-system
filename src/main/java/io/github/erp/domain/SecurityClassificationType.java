package io.github.erp.domain;

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
