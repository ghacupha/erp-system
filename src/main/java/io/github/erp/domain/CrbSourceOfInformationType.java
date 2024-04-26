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

/**
 * A CrbSourceOfInformationType.
 */
@Entity
@Table(name = "crb_source_of_information_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbsourceofinformationtype")
public class CrbSourceOfInformationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "source_of_information_type_code", nullable = false, unique = true)
    private String sourceOfInformationTypeCode;

    @Column(name = "source_of_information_type_description", unique = true)
    private String sourceOfInformationTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbSourceOfInformationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceOfInformationTypeCode() {
        return this.sourceOfInformationTypeCode;
    }

    public CrbSourceOfInformationType sourceOfInformationTypeCode(String sourceOfInformationTypeCode) {
        this.setSourceOfInformationTypeCode(sourceOfInformationTypeCode);
        return this;
    }

    public void setSourceOfInformationTypeCode(String sourceOfInformationTypeCode) {
        this.sourceOfInformationTypeCode = sourceOfInformationTypeCode;
    }

    public String getSourceOfInformationTypeDescription() {
        return this.sourceOfInformationTypeDescription;
    }

    public CrbSourceOfInformationType sourceOfInformationTypeDescription(String sourceOfInformationTypeDescription) {
        this.setSourceOfInformationTypeDescription(sourceOfInformationTypeDescription);
        return this;
    }

    public void setSourceOfInformationTypeDescription(String sourceOfInformationTypeDescription) {
        this.sourceOfInformationTypeDescription = sourceOfInformationTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbSourceOfInformationType)) {
            return false;
        }
        return id != null && id.equals(((CrbSourceOfInformationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSourceOfInformationType{" +
            "id=" + getId() +
            ", sourceOfInformationTypeCode='" + getSourceOfInformationTypeCode() + "'" +
            ", sourceOfInformationTypeDescription='" + getSourceOfInformationTypeDescription() + "'" +
            "}";
    }
}
