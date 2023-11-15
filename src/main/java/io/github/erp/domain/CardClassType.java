package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * A CardClassType.
 */
@Entity
@Table(name = "card_class_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardclasstype")
public class CardClassType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_class_type_code", nullable = false, unique = true)
    private String cardClassTypeCode;

    @NotNull
    @Column(name = "card_class_type", nullable = false, unique = true)
    private String cardClassType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_class_details")
    private String cardClassDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardClassType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardClassTypeCode() {
        return this.cardClassTypeCode;
    }

    public CardClassType cardClassTypeCode(String cardClassTypeCode) {
        this.setCardClassTypeCode(cardClassTypeCode);
        return this;
    }

    public void setCardClassTypeCode(String cardClassTypeCode) {
        this.cardClassTypeCode = cardClassTypeCode;
    }

    public String getCardClassType() {
        return this.cardClassType;
    }

    public CardClassType cardClassType(String cardClassType) {
        this.setCardClassType(cardClassType);
        return this;
    }

    public void setCardClassType(String cardClassType) {
        this.cardClassType = cardClassType;
    }

    public String getCardClassDetails() {
        return this.cardClassDetails;
    }

    public CardClassType cardClassDetails(String cardClassDetails) {
        this.setCardClassDetails(cardClassDetails);
        return this;
    }

    public void setCardClassDetails(String cardClassDetails) {
        this.cardClassDetails = cardClassDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardClassType)) {
            return false;
        }
        return id != null && id.equals(((CardClassType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardClassType{" +
            "id=" + getId() +
            ", cardClassTypeCode='" + getCardClassTypeCode() + "'" +
            ", cardClassType='" + getCardClassType() + "'" +
            ", cardClassDetails='" + getCardClassDetails() + "'" +
            "}";
    }
}
