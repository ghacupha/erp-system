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
 * A CardFraudIncidentCategory.
 */
@Entity
@Table(name = "card_fraud_incident_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardfraudincidentcategory")
public class CardFraudIncidentCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_fraud_category_type_code", nullable = false, unique = true)
    private String cardFraudCategoryTypeCode;

    @NotNull
    @Column(name = "card_fraud_category_type", nullable = false, unique = true)
    private String cardFraudCategoryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_fraud_category_type_description")
    private String cardFraudCategoryTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardFraudIncidentCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardFraudCategoryTypeCode() {
        return this.cardFraudCategoryTypeCode;
    }

    public CardFraudIncidentCategory cardFraudCategoryTypeCode(String cardFraudCategoryTypeCode) {
        this.setCardFraudCategoryTypeCode(cardFraudCategoryTypeCode);
        return this;
    }

    public void setCardFraudCategoryTypeCode(String cardFraudCategoryTypeCode) {
        this.cardFraudCategoryTypeCode = cardFraudCategoryTypeCode;
    }

    public String getCardFraudCategoryType() {
        return this.cardFraudCategoryType;
    }

    public CardFraudIncidentCategory cardFraudCategoryType(String cardFraudCategoryType) {
        this.setCardFraudCategoryType(cardFraudCategoryType);
        return this;
    }

    public void setCardFraudCategoryType(String cardFraudCategoryType) {
        this.cardFraudCategoryType = cardFraudCategoryType;
    }

    public String getCardFraudCategoryTypeDescription() {
        return this.cardFraudCategoryTypeDescription;
    }

    public CardFraudIncidentCategory cardFraudCategoryTypeDescription(String cardFraudCategoryTypeDescription) {
        this.setCardFraudCategoryTypeDescription(cardFraudCategoryTypeDescription);
        return this;
    }

    public void setCardFraudCategoryTypeDescription(String cardFraudCategoryTypeDescription) {
        this.cardFraudCategoryTypeDescription = cardFraudCategoryTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardFraudIncidentCategory)) {
            return false;
        }
        return id != null && id.equals(((CardFraudIncidentCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardFraudIncidentCategory{" +
            "id=" + getId() +
            ", cardFraudCategoryTypeCode='" + getCardFraudCategoryTypeCode() + "'" +
            ", cardFraudCategoryType='" + getCardFraudCategoryType() + "'" +
            ", cardFraudCategoryTypeDescription='" + getCardFraudCategoryTypeDescription() + "'" +
            "}";
    }
}
