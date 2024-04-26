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
 * A FxTransactionRateType.
 */
@Entity
@Table(name = "fx_transaction_rate_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxtransactionratetype")
public class FxTransactionRateType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fx_transaction_rate_type_code", nullable = false, unique = true)
    private String fxTransactionRateTypeCode;

    @NotNull
    @Column(name = "fx_transaction_rate_type", nullable = false, unique = true)
    private String fxTransactionRateType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fx_transaction_rate_type_details")
    private String fxTransactionRateTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxTransactionRateType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionRateTypeCode() {
        return this.fxTransactionRateTypeCode;
    }

    public FxTransactionRateType fxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.setFxTransactionRateTypeCode(fxTransactionRateTypeCode);
        return this;
    }

    public void setFxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.fxTransactionRateTypeCode = fxTransactionRateTypeCode;
    }

    public String getFxTransactionRateType() {
        return this.fxTransactionRateType;
    }

    public FxTransactionRateType fxTransactionRateType(String fxTransactionRateType) {
        this.setFxTransactionRateType(fxTransactionRateType);
        return this;
    }

    public void setFxTransactionRateType(String fxTransactionRateType) {
        this.fxTransactionRateType = fxTransactionRateType;
    }

    public String getFxTransactionRateTypeDetails() {
        return this.fxTransactionRateTypeDetails;
    }

    public FxTransactionRateType fxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.setFxTransactionRateTypeDetails(fxTransactionRateTypeDetails);
        return this;
    }

    public void setFxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.fxTransactionRateTypeDetails = fxTransactionRateTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionRateType)) {
            return false;
        }
        return id != null && id.equals(((FxTransactionRateType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionRateType{" +
            "id=" + getId() +
            ", fxTransactionRateTypeCode='" + getFxTransactionRateTypeCode() + "'" +
            ", fxTransactionRateType='" + getFxTransactionRateType() + "'" +
            ", fxTransactionRateTypeDetails='" + getFxTransactionRateTypeDetails() + "'" +
            "}";
    }
}
