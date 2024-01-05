package io.github.erp.domain;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import io.github.erp.domain.enumeration.CardStateFlagTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardState.
 */
@Entity
@Table(name = "card_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardstate")
public class CardState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_state_flag", nullable = false, unique = true)
    private CardStateFlagTypes cardStateFlag;

    @NotNull
    @Column(name = "card_state_flag_details", nullable = false)
    private String cardStateFlagDetails;

    @Column(name = "card_state_flag_description")
    private String cardStateFlagDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardState id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardStateFlagTypes getCardStateFlag() {
        return this.cardStateFlag;
    }

    public CardState cardStateFlag(CardStateFlagTypes cardStateFlag) {
        this.setCardStateFlag(cardStateFlag);
        return this;
    }

    public void setCardStateFlag(CardStateFlagTypes cardStateFlag) {
        this.cardStateFlag = cardStateFlag;
    }

    public String getCardStateFlagDetails() {
        return this.cardStateFlagDetails;
    }

    public CardState cardStateFlagDetails(String cardStateFlagDetails) {
        this.setCardStateFlagDetails(cardStateFlagDetails);
        return this;
    }

    public void setCardStateFlagDetails(String cardStateFlagDetails) {
        this.cardStateFlagDetails = cardStateFlagDetails;
    }

    public String getCardStateFlagDescription() {
        return this.cardStateFlagDescription;
    }

    public CardState cardStateFlagDescription(String cardStateFlagDescription) {
        this.setCardStateFlagDescription(cardStateFlagDescription);
        return this;
    }

    public void setCardStateFlagDescription(String cardStateFlagDescription) {
        this.cardStateFlagDescription = cardStateFlagDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardState)) {
            return false;
        }
        return id != null && id.equals(((CardState) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardState{" +
            "id=" + getId() +
            ", cardStateFlag='" + getCardStateFlag() + "'" +
            ", cardStateFlagDetails='" + getCardStateFlagDetails() + "'" +
            ", cardStateFlagDescription='" + getCardStateFlagDescription() + "'" +
            "}";
    }
}
