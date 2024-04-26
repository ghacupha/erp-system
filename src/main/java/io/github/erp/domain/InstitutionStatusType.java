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
 * A InstitutionStatusType.
 */
@Entity
@Table(name = "institution_status_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "institutionstatustype")
public class InstitutionStatusType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "institution_status_code", nullable = false, unique = true)
    private String institutionStatusCode;

    @Column(name = "institution_status_type")
    private String institutionStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "insitution_status_type_description")
    private String insitutionStatusTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InstitutionStatusType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionStatusCode() {
        return this.institutionStatusCode;
    }

    public InstitutionStatusType institutionStatusCode(String institutionStatusCode) {
        this.setInstitutionStatusCode(institutionStatusCode);
        return this;
    }

    public void setInstitutionStatusCode(String institutionStatusCode) {
        this.institutionStatusCode = institutionStatusCode;
    }

    public String getInstitutionStatusType() {
        return this.institutionStatusType;
    }

    public InstitutionStatusType institutionStatusType(String institutionStatusType) {
        this.setInstitutionStatusType(institutionStatusType);
        return this;
    }

    public void setInstitutionStatusType(String institutionStatusType) {
        this.institutionStatusType = institutionStatusType;
    }

    public String getInsitutionStatusTypeDescription() {
        return this.insitutionStatusTypeDescription;
    }

    public InstitutionStatusType insitutionStatusTypeDescription(String insitutionStatusTypeDescription) {
        this.setInsitutionStatusTypeDescription(insitutionStatusTypeDescription);
        return this;
    }

    public void setInsitutionStatusTypeDescription(String insitutionStatusTypeDescription) {
        this.insitutionStatusTypeDescription = insitutionStatusTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionStatusType)) {
            return false;
        }
        return id != null && id.equals(((InstitutionStatusType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionStatusType{" +
            "id=" + getId() +
            ", institutionStatusCode='" + getInstitutionStatusCode() + "'" +
            ", institutionStatusType='" + getInstitutionStatusType() + "'" +
            ", insitutionStatusTypeDescription='" + getInsitutionStatusTypeDescription() + "'" +
            "}";
    }
}
