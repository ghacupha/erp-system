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
 * A CardCharges.
 */
@Entity
@Table(name = "card_charges")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardcharges")
public class CardCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_charge_type", nullable = false, unique = true)
    private String cardChargeType;

    @NotNull
    @Column(name = "card_charge_type_name", nullable = false, unique = true)
    private String cardChargeTypeName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_charge_details")
    private String cardChargeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardCharges id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardChargeType() {
        return this.cardChargeType;
    }

    public CardCharges cardChargeType(String cardChargeType) {
        this.setCardChargeType(cardChargeType);
        return this;
    }

    public void setCardChargeType(String cardChargeType) {
        this.cardChargeType = cardChargeType;
    }

    public String getCardChargeTypeName() {
        return this.cardChargeTypeName;
    }

    public CardCharges cardChargeTypeName(String cardChargeTypeName) {
        this.setCardChargeTypeName(cardChargeTypeName);
        return this;
    }

    public void setCardChargeTypeName(String cardChargeTypeName) {
        this.cardChargeTypeName = cardChargeTypeName;
    }

    public String getCardChargeDetails() {
        return this.cardChargeDetails;
    }

    public CardCharges cardChargeDetails(String cardChargeDetails) {
        this.setCardChargeDetails(cardChargeDetails);
        return this;
    }

    public void setCardChargeDetails(String cardChargeDetails) {
        this.cardChargeDetails = cardChargeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardCharges)) {
            return false;
        }
        return id != null && id.equals(((CardCharges) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCharges{" +
            "id=" + getId() +
            ", cardChargeType='" + getCardChargeType() + "'" +
            ", cardChargeTypeName='" + getCardChargeTypeName() + "'" +
            ", cardChargeDetails='" + getCardChargeDetails() + "'" +
            "}";
    }
}
