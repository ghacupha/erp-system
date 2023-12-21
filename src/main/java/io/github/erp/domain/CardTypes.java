package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * A CardTypes.
 */
@Entity
@Table(name = "card_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardtypes")
public class CardTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_type_code", nullable = false, unique = true)
    private String cardTypeCode;

    @NotNull
    @Column(name = "card_type", nullable = false, unique = true)
    private String cardType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_type_details")
    private String cardTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardTypeCode() {
        return this.cardTypeCode;
    }

    public CardTypes cardTypeCode(String cardTypeCode) {
        this.setCardTypeCode(cardTypeCode);
        return this;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardType() {
        return this.cardType;
    }

    public CardTypes cardType(String cardType) {
        this.setCardType(cardType);
        return this;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeDetails() {
        return this.cardTypeDetails;
    }

    public CardTypes cardTypeDetails(String cardTypeDetails) {
        this.setCardTypeDetails(cardTypeDetails);
        return this;
    }

    public void setCardTypeDetails(String cardTypeDetails) {
        this.cardTypeDetails = cardTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardTypes)) {
            return false;
        }
        return id != null && id.equals(((CardTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardTypes{" +
            "id=" + getId() +
            ", cardTypeCode='" + getCardTypeCode() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", cardTypeDetails='" + getCardTypeDetails() + "'" +
            "}";
    }
}
