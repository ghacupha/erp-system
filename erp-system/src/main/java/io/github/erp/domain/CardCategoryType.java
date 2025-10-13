package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.CardCategoryFlag;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CardCategoryType.
 */
@Entity
@Table(name = "card_category_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardcategorytype")
public class CardCategoryType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_category_flag", nullable = false)
    private CardCategoryFlag cardCategoryFlag;

    @NotNull
    @Column(name = "card_category_description", nullable = false, unique = true)
    private String cardCategoryDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_category_details")
    private String cardCategoryDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardCategoryType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardCategoryFlag getCardCategoryFlag() {
        return this.cardCategoryFlag;
    }

    public CardCategoryType cardCategoryFlag(CardCategoryFlag cardCategoryFlag) {
        this.setCardCategoryFlag(cardCategoryFlag);
        return this;
    }

    public void setCardCategoryFlag(CardCategoryFlag cardCategoryFlag) {
        this.cardCategoryFlag = cardCategoryFlag;
    }

    public String getCardCategoryDescription() {
        return this.cardCategoryDescription;
    }

    public CardCategoryType cardCategoryDescription(String cardCategoryDescription) {
        this.setCardCategoryDescription(cardCategoryDescription);
        return this;
    }

    public void setCardCategoryDescription(String cardCategoryDescription) {
        this.cardCategoryDescription = cardCategoryDescription;
    }

    public String getCardCategoryDetails() {
        return this.cardCategoryDetails;
    }

    public CardCategoryType cardCategoryDetails(String cardCategoryDetails) {
        this.setCardCategoryDetails(cardCategoryDetails);
        return this;
    }

    public void setCardCategoryDetails(String cardCategoryDetails) {
        this.cardCategoryDetails = cardCategoryDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardCategoryType)) {
            return false;
        }
        return id != null && id.equals(((CardCategoryType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCategoryType{" +
            "id=" + getId() +
            ", cardCategoryFlag='" + getCardCategoryFlag() + "'" +
            ", cardCategoryDescription='" + getCardCategoryDescription() + "'" +
            ", cardCategoryDetails='" + getCardCategoryDetails() + "'" +
            "}";
    }
}
