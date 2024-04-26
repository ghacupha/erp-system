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
 * A ProfessionalQualification.
 */
@Entity
@Table(name = "professional_qualification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "professionalqualification")
public class ProfessionalQualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "professional_qualifications_code", nullable = false, unique = true)
    private String professionalQualificationsCode;

    @NotNull
    @Column(name = "professional_qualifications_type", nullable = false, unique = true)
    private String professionalQualificationsType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "professional_qualifications_details")
    private String professionalQualificationsDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalQualification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfessionalQualificationsCode() {
        return this.professionalQualificationsCode;
    }

    public ProfessionalQualification professionalQualificationsCode(String professionalQualificationsCode) {
        this.setProfessionalQualificationsCode(professionalQualificationsCode);
        return this;
    }

    public void setProfessionalQualificationsCode(String professionalQualificationsCode) {
        this.professionalQualificationsCode = professionalQualificationsCode;
    }

    public String getProfessionalQualificationsType() {
        return this.professionalQualificationsType;
    }

    public ProfessionalQualification professionalQualificationsType(String professionalQualificationsType) {
        this.setProfessionalQualificationsType(professionalQualificationsType);
        return this;
    }

    public void setProfessionalQualificationsType(String professionalQualificationsType) {
        this.professionalQualificationsType = professionalQualificationsType;
    }

    public String getProfessionalQualificationsDetails() {
        return this.professionalQualificationsDetails;
    }

    public ProfessionalQualification professionalQualificationsDetails(String professionalQualificationsDetails) {
        this.setProfessionalQualificationsDetails(professionalQualificationsDetails);
        return this;
    }

    public void setProfessionalQualificationsDetails(String professionalQualificationsDetails) {
        this.professionalQualificationsDetails = professionalQualificationsDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalQualification)) {
            return false;
        }
        return id != null && id.equals(((ProfessionalQualification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalQualification{" +
            "id=" + getId() +
            ", professionalQualificationsCode='" + getProfessionalQualificationsCode() + "'" +
            ", professionalQualificationsType='" + getProfessionalQualificationsType() + "'" +
            ", professionalQualificationsDetails='" + getProfessionalQualificationsDetails() + "'" +
            "}";
    }
}
