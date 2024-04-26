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
 * A AgriculturalEnterpriseActivityType.
 */
@Entity
@Table(name = "agricultural_enterprise_activity_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agriculturalenterpriseactivitytype")
public class AgriculturalEnterpriseActivityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "agricultural_enterprise_activity_type_code", nullable = false, unique = true)
    private String agriculturalEnterpriseActivityTypeCode;

    @NotNull
    @Column(name = "agricultural_enterprise_activity_type", nullable = false)
    private String agriculturalEnterpriseActivityType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "agricultural_enterprise_activity_type_description")
    private String agriculturalEnterpriseActivityTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgriculturalEnterpriseActivityType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgriculturalEnterpriseActivityTypeCode() {
        return this.agriculturalEnterpriseActivityTypeCode;
    }

    public AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityTypeCode(String agriculturalEnterpriseActivityTypeCode) {
        this.setAgriculturalEnterpriseActivityTypeCode(agriculturalEnterpriseActivityTypeCode);
        return this;
    }

    public void setAgriculturalEnterpriseActivityTypeCode(String agriculturalEnterpriseActivityTypeCode) {
        this.agriculturalEnterpriseActivityTypeCode = agriculturalEnterpriseActivityTypeCode;
    }

    public String getAgriculturalEnterpriseActivityType() {
        return this.agriculturalEnterpriseActivityType;
    }

    public AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityType(String agriculturalEnterpriseActivityType) {
        this.setAgriculturalEnterpriseActivityType(agriculturalEnterpriseActivityType);
        return this;
    }

    public void setAgriculturalEnterpriseActivityType(String agriculturalEnterpriseActivityType) {
        this.agriculturalEnterpriseActivityType = agriculturalEnterpriseActivityType;
    }

    public String getAgriculturalEnterpriseActivityTypeDescription() {
        return this.agriculturalEnterpriseActivityTypeDescription;
    }

    public AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityTypeDescription(
        String agriculturalEnterpriseActivityTypeDescription
    ) {
        this.setAgriculturalEnterpriseActivityTypeDescription(agriculturalEnterpriseActivityTypeDescription);
        return this;
    }

    public void setAgriculturalEnterpriseActivityTypeDescription(String agriculturalEnterpriseActivityTypeDescription) {
        this.agriculturalEnterpriseActivityTypeDescription = agriculturalEnterpriseActivityTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculturalEnterpriseActivityType)) {
            return false;
        }
        return id != null && id.equals(((AgriculturalEnterpriseActivityType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalEnterpriseActivityType{" +
            "id=" + getId() +
            ", agriculturalEnterpriseActivityTypeCode='" + getAgriculturalEnterpriseActivityTypeCode() + "'" +
            ", agriculturalEnterpriseActivityType='" + getAgriculturalEnterpriseActivityType() + "'" +
            ", agriculturalEnterpriseActivityTypeDescription='" + getAgriculturalEnterpriseActivityTypeDescription() + "'" +
            "}";
    }
}
