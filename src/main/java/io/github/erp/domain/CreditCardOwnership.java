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
import io.github.erp.domain.enumeration.CreditCardOwnershipTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CreditCardOwnership.
 */
@Entity
@Table(name = "credit_card_ownership")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "creditcardownership")
public class CreditCardOwnership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "credit_card_ownership_category_code", nullable = false, unique = true)
    private String creditCardOwnershipCategoryCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "credit_card_ownership_category_type", nullable = false)
    private CreditCardOwnershipTypes creditCardOwnershipCategoryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CreditCardOwnership id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardOwnershipCategoryCode() {
        return this.creditCardOwnershipCategoryCode;
    }

    public CreditCardOwnership creditCardOwnershipCategoryCode(String creditCardOwnershipCategoryCode) {
        this.setCreditCardOwnershipCategoryCode(creditCardOwnershipCategoryCode);
        return this;
    }

    public void setCreditCardOwnershipCategoryCode(String creditCardOwnershipCategoryCode) {
        this.creditCardOwnershipCategoryCode = creditCardOwnershipCategoryCode;
    }

    public CreditCardOwnershipTypes getCreditCardOwnershipCategoryType() {
        return this.creditCardOwnershipCategoryType;
    }

    public CreditCardOwnership creditCardOwnershipCategoryType(CreditCardOwnershipTypes creditCardOwnershipCategoryType) {
        this.setCreditCardOwnershipCategoryType(creditCardOwnershipCategoryType);
        return this;
    }

    public void setCreditCardOwnershipCategoryType(CreditCardOwnershipTypes creditCardOwnershipCategoryType) {
        this.creditCardOwnershipCategoryType = creditCardOwnershipCategoryType;
    }

    public String getDescription() {
        return this.description;
    }

    public CreditCardOwnership description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCardOwnership)) {
            return false;
        }
        return id != null && id.equals(((CreditCardOwnership) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardOwnership{" +
            "id=" + getId() +
            ", creditCardOwnershipCategoryCode='" + getCreditCardOwnershipCategoryCode() + "'" +
            ", creditCardOwnershipCategoryType='" + getCreditCardOwnershipCategoryType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
