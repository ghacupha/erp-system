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
 * A CounterPartyDealType.
 */
@Entity
@Table(name = "counter_party_deal_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "counterpartydealtype")
public class CounterPartyDealType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "counterparty_deal_code", nullable = false, unique = true)
    private String counterpartyDealCode;

    @NotNull
    @Column(name = "counterparty_deal_type_details", nullable = false, unique = true)
    private String counterpartyDealTypeDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "counterparty_deal_type_description")
    private String counterpartyDealTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CounterPartyDealType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounterpartyDealCode() {
        return this.counterpartyDealCode;
    }

    public CounterPartyDealType counterpartyDealCode(String counterpartyDealCode) {
        this.setCounterpartyDealCode(counterpartyDealCode);
        return this;
    }

    public void setCounterpartyDealCode(String counterpartyDealCode) {
        this.counterpartyDealCode = counterpartyDealCode;
    }

    public String getCounterpartyDealTypeDetails() {
        return this.counterpartyDealTypeDetails;
    }

    public CounterPartyDealType counterpartyDealTypeDetails(String counterpartyDealTypeDetails) {
        this.setCounterpartyDealTypeDetails(counterpartyDealTypeDetails);
        return this;
    }

    public void setCounterpartyDealTypeDetails(String counterpartyDealTypeDetails) {
        this.counterpartyDealTypeDetails = counterpartyDealTypeDetails;
    }

    public String getCounterpartyDealTypeDescription() {
        return this.counterpartyDealTypeDescription;
    }

    public CounterPartyDealType counterpartyDealTypeDescription(String counterpartyDealTypeDescription) {
        this.setCounterpartyDealTypeDescription(counterpartyDealTypeDescription);
        return this;
    }

    public void setCounterpartyDealTypeDescription(String counterpartyDealTypeDescription) {
        this.counterpartyDealTypeDescription = counterpartyDealTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounterPartyDealType)) {
            return false;
        }
        return id != null && id.equals(((CounterPartyDealType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterPartyDealType{" +
            "id=" + getId() +
            ", counterpartyDealCode='" + getCounterpartyDealCode() + "'" +
            ", counterpartyDealTypeDetails='" + getCounterpartyDealTypeDetails() + "'" +
            ", counterpartyDealTypeDescription='" + getCounterpartyDealTypeDescription() + "'" +
            "}";
    }
}
