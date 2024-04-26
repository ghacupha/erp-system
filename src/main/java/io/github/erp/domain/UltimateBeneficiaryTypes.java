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
 * A UltimateBeneficiaryTypes.
 */
@Entity
@Table(name = "ultimate_beneficiary_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ultimatebeneficiarytypes")
public class UltimateBeneficiaryTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ultimate_beneficiary_type_code", nullable = false, unique = true)
    private String ultimateBeneficiaryTypeCode;

    @NotNull
    @Column(name = "ultimate_beneficiary_type", nullable = false, unique = true)
    private String ultimateBeneficiaryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ultimate_beneficiary_type_details")
    private String ultimateBeneficiaryTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UltimateBeneficiaryTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUltimateBeneficiaryTypeCode() {
        return this.ultimateBeneficiaryTypeCode;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryTypeCode(String ultimateBeneficiaryTypeCode) {
        this.setUltimateBeneficiaryTypeCode(ultimateBeneficiaryTypeCode);
        return this;
    }

    public void setUltimateBeneficiaryTypeCode(String ultimateBeneficiaryTypeCode) {
        this.ultimateBeneficiaryTypeCode = ultimateBeneficiaryTypeCode;
    }

    public String getUltimateBeneficiaryType() {
        return this.ultimateBeneficiaryType;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.setUltimateBeneficiaryType(ultimateBeneficiaryType);
        return this;
    }

    public void setUltimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.ultimateBeneficiaryType = ultimateBeneficiaryType;
    }

    public String getUltimateBeneficiaryTypeDetails() {
        return this.ultimateBeneficiaryTypeDetails;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryTypeDetails(String ultimateBeneficiaryTypeDetails) {
        this.setUltimateBeneficiaryTypeDetails(ultimateBeneficiaryTypeDetails);
        return this;
    }

    public void setUltimateBeneficiaryTypeDetails(String ultimateBeneficiaryTypeDetails) {
        this.ultimateBeneficiaryTypeDetails = ultimateBeneficiaryTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UltimateBeneficiaryTypes)) {
            return false;
        }
        return id != null && id.equals(((UltimateBeneficiaryTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UltimateBeneficiaryTypes{" +
            "id=" + getId() +
            ", ultimateBeneficiaryTypeCode='" + getUltimateBeneficiaryTypeCode() + "'" +
            ", ultimateBeneficiaryType='" + getUltimateBeneficiaryType() + "'" +
            ", ultimateBeneficiaryTypeDetails='" + getUltimateBeneficiaryTypeDetails() + "'" +
            "}";
    }
}
