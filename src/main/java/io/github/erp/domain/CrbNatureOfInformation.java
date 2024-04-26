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
 * A CrbNatureOfInformation.
 */
@Entity
@Table(name = "crb_nature_of_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbnatureofinformation")
public class CrbNatureOfInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nature_of_information_type_code", nullable = false, unique = true)
    private String natureOfInformationTypeCode;

    @NotNull
    @Column(name = "nature_of_information_type", nullable = false, unique = true)
    private String natureOfInformationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "nature_of_information_type_description")
    private String natureOfInformationTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbNatureOfInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatureOfInformationTypeCode() {
        return this.natureOfInformationTypeCode;
    }

    public CrbNatureOfInformation natureOfInformationTypeCode(String natureOfInformationTypeCode) {
        this.setNatureOfInformationTypeCode(natureOfInformationTypeCode);
        return this;
    }

    public void setNatureOfInformationTypeCode(String natureOfInformationTypeCode) {
        this.natureOfInformationTypeCode = natureOfInformationTypeCode;
    }

    public String getNatureOfInformationType() {
        return this.natureOfInformationType;
    }

    public CrbNatureOfInformation natureOfInformationType(String natureOfInformationType) {
        this.setNatureOfInformationType(natureOfInformationType);
        return this;
    }

    public void setNatureOfInformationType(String natureOfInformationType) {
        this.natureOfInformationType = natureOfInformationType;
    }

    public String getNatureOfInformationTypeDescription() {
        return this.natureOfInformationTypeDescription;
    }

    public CrbNatureOfInformation natureOfInformationTypeDescription(String natureOfInformationTypeDescription) {
        this.setNatureOfInformationTypeDescription(natureOfInformationTypeDescription);
        return this;
    }

    public void setNatureOfInformationTypeDescription(String natureOfInformationTypeDescription) {
        this.natureOfInformationTypeDescription = natureOfInformationTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbNatureOfInformation)) {
            return false;
        }
        return id != null && id.equals(((CrbNatureOfInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbNatureOfInformation{" +
            "id=" + getId() +
            ", natureOfInformationTypeCode='" + getNatureOfInformationTypeCode() + "'" +
            ", natureOfInformationType='" + getNatureOfInformationType() + "'" +
            ", natureOfInformationTypeDescription='" + getNatureOfInformationTypeDescription() + "'" +
            "}";
    }
}
