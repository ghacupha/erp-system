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
 * A CollateralType.
 */
@Entity
@Table(name = "collateral_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "collateraltype")
public class CollateralType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "collateral_type_code", nullable = false, unique = true)
    private String collateralTypeCode;

    @NotNull
    @Column(name = "collateral_type", nullable = false)
    private String collateralType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "collateral_type_description")
    private String collateralTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CollateralType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollateralTypeCode() {
        return this.collateralTypeCode;
    }

    public CollateralType collateralTypeCode(String collateralTypeCode) {
        this.setCollateralTypeCode(collateralTypeCode);
        return this;
    }

    public void setCollateralTypeCode(String collateralTypeCode) {
        this.collateralTypeCode = collateralTypeCode;
    }

    public String getCollateralType() {
        return this.collateralType;
    }

    public CollateralType collateralType(String collateralType) {
        this.setCollateralType(collateralType);
        return this;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getCollateralTypeDescription() {
        return this.collateralTypeDescription;
    }

    public CollateralType collateralTypeDescription(String collateralTypeDescription) {
        this.setCollateralTypeDescription(collateralTypeDescription);
        return this;
    }

    public void setCollateralTypeDescription(String collateralTypeDescription) {
        this.collateralTypeDescription = collateralTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollateralType)) {
            return false;
        }
        return id != null && id.equals(((CollateralType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralType{" +
            "id=" + getId() +
            ", collateralTypeCode='" + getCollateralTypeCode() + "'" +
            ", collateralType='" + getCollateralType() + "'" +
            ", collateralTypeDescription='" + getCollateralTypeDescription() + "'" +
            "}";
    }
}
