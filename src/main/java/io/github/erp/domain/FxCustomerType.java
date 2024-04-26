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
 * A FxCustomerType.
 */
@Entity
@Table(name = "fx_customer_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxcustomertype")
public class FxCustomerType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "foreign_exchange_customer_type_code", nullable = false, unique = true)
    private String foreignExchangeCustomerTypeCode;

    @NotNull
    @Column(name = "foreign_customer_type", nullable = false, unique = true)
    private String foreignCustomerType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxCustomerType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getForeignExchangeCustomerTypeCode() {
        return this.foreignExchangeCustomerTypeCode;
    }

    public FxCustomerType foreignExchangeCustomerTypeCode(String foreignExchangeCustomerTypeCode) {
        this.setForeignExchangeCustomerTypeCode(foreignExchangeCustomerTypeCode);
        return this;
    }

    public void setForeignExchangeCustomerTypeCode(String foreignExchangeCustomerTypeCode) {
        this.foreignExchangeCustomerTypeCode = foreignExchangeCustomerTypeCode;
    }

    public String getForeignCustomerType() {
        return this.foreignCustomerType;
    }

    public FxCustomerType foreignCustomerType(String foreignCustomerType) {
        this.setForeignCustomerType(foreignCustomerType);
        return this;
    }

    public void setForeignCustomerType(String foreignCustomerType) {
        this.foreignCustomerType = foreignCustomerType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxCustomerType)) {
            return false;
        }
        return id != null && id.equals(((FxCustomerType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxCustomerType{" +
            "id=" + getId() +
            ", foreignExchangeCustomerTypeCode='" + getForeignExchangeCustomerTypeCode() + "'" +
            ", foreignCustomerType='" + getForeignCustomerType() + "'" +
            "}";
    }
}
