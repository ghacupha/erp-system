package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
