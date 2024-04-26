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
 * A ContractStatus.
 */
@Entity
@Table(name = "contract_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contractstatus")
public class ContractStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contract_status_code", nullable = false, unique = true)
    private String contractStatusCode;

    @NotNull
    @Column(name = "contract_status_type", nullable = false)
    private String contractStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "contract_status_type_description")
    private String contractStatusTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractStatusCode() {
        return this.contractStatusCode;
    }

    public ContractStatus contractStatusCode(String contractStatusCode) {
        this.setContractStatusCode(contractStatusCode);
        return this;
    }

    public void setContractStatusCode(String contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    public String getContractStatusType() {
        return this.contractStatusType;
    }

    public ContractStatus contractStatusType(String contractStatusType) {
        this.setContractStatusType(contractStatusType);
        return this;
    }

    public void setContractStatusType(String contractStatusType) {
        this.contractStatusType = contractStatusType;
    }

    public String getContractStatusTypeDescription() {
        return this.contractStatusTypeDescription;
    }

    public ContractStatus contractStatusTypeDescription(String contractStatusTypeDescription) {
        this.setContractStatusTypeDescription(contractStatusTypeDescription);
        return this;
    }

    public void setContractStatusTypeDescription(String contractStatusTypeDescription) {
        this.contractStatusTypeDescription = contractStatusTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractStatus)) {
            return false;
        }
        return id != null && id.equals(((ContractStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractStatus{" +
            "id=" + getId() +
            ", contractStatusCode='" + getContractStatusCode() + "'" +
            ", contractStatusType='" + getContractStatusType() + "'" +
            ", contractStatusTypeDescription='" + getContractStatusTypeDescription() + "'" +
            "}";
    }
}
