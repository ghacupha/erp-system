package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CardBrandType.
 */
@Entity
@Table(name = "card_brand_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardbrandtype")
public class CardBrandType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_brand_type_code", nullable = false, unique = true)
    private String cardBrandTypeCode;

    @NotNull
    @Column(name = "card_brand_type", nullable = false, unique = true)
    private String cardBrandType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_brand_type_details")
    private String cardBrandTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardBrandType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardBrandTypeCode() {
        return this.cardBrandTypeCode;
    }

    public CardBrandType cardBrandTypeCode(String cardBrandTypeCode) {
        this.setCardBrandTypeCode(cardBrandTypeCode);
        return this;
    }

    public void setCardBrandTypeCode(String cardBrandTypeCode) {
        this.cardBrandTypeCode = cardBrandTypeCode;
    }

    public String getCardBrandType() {
        return this.cardBrandType;
    }

    public CardBrandType cardBrandType(String cardBrandType) {
        this.setCardBrandType(cardBrandType);
        return this;
    }

    public void setCardBrandType(String cardBrandType) {
        this.cardBrandType = cardBrandType;
    }

    public String getCardBrandTypeDetails() {
        return this.cardBrandTypeDetails;
    }

    public CardBrandType cardBrandTypeDetails(String cardBrandTypeDetails) {
        this.setCardBrandTypeDetails(cardBrandTypeDetails);
        return this;
    }

    public void setCardBrandTypeDetails(String cardBrandTypeDetails) {
        this.cardBrandTypeDetails = cardBrandTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardBrandType)) {
            return false;
        }
        return id != null && id.equals(((CardBrandType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardBrandType{" +
            "id=" + getId() +
            ", cardBrandTypeCode='" + getCardBrandTypeCode() + "'" +
            ", cardBrandType='" + getCardBrandType() + "'" +
            ", cardBrandTypeDetails='" + getCardBrandTypeDetails() + "'" +
            "}";
    }
}
