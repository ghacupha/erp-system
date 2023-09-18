package io.github.erp.domain;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
