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
 * A FraudType.
 */
@Entity
@Table(name = "fraud_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fraudtype")
public class FraudType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fraud_type_code", nullable = false, unique = true)
    private String fraudTypeCode;

    @NotNull
    @Column(name = "fraud_type", nullable = false, unique = true)
    private String fraudType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fraud_type_details")
    private String fraudTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FraudType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFraudTypeCode() {
        return this.fraudTypeCode;
    }

    public FraudType fraudTypeCode(String fraudTypeCode) {
        this.setFraudTypeCode(fraudTypeCode);
        return this;
    }

    public void setFraudTypeCode(String fraudTypeCode) {
        this.fraudTypeCode = fraudTypeCode;
    }

    public String getFraudType() {
        return this.fraudType;
    }

    public FraudType fraudType(String fraudType) {
        this.setFraudType(fraudType);
        return this;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getFraudTypeDetails() {
        return this.fraudTypeDetails;
    }

    public FraudType fraudTypeDetails(String fraudTypeDetails) {
        this.setFraudTypeDetails(fraudTypeDetails);
        return this;
    }

    public void setFraudTypeDetails(String fraudTypeDetails) {
        this.fraudTypeDetails = fraudTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraudType)) {
            return false;
        }
        return id != null && id.equals(((FraudType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraudType{" +
            "id=" + getId() +
            ", fraudTypeCode='" + getFraudTypeCode() + "'" +
            ", fraudType='" + getFraudType() + "'" +
            ", fraudTypeDetails='" + getFraudTypeDetails() + "'" +
            "}";
    }
}
