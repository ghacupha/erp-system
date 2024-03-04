package io.github.erp.domain;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.domain.enumeration.FlagCodes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardStatusFlag.
 */
@Entity
@Table(name = "card_status_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardstatusflag")
public class CardStatusFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status_flag", nullable = false)
    private FlagCodes cardStatusFlag;

    @NotNull
    @Column(name = "card_status_flag_description", nullable = false)
    private String cardStatusFlagDescription;

    @Column(name = "card_status_flag_details")
    private String cardStatusFlagDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardStatusFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlagCodes getCardStatusFlag() {
        return this.cardStatusFlag;
    }

    public CardStatusFlag cardStatusFlag(FlagCodes cardStatusFlag) {
        this.setCardStatusFlag(cardStatusFlag);
        return this;
    }

    public void setCardStatusFlag(FlagCodes cardStatusFlag) {
        this.cardStatusFlag = cardStatusFlag;
    }

    public String getCardStatusFlagDescription() {
        return this.cardStatusFlagDescription;
    }

    public CardStatusFlag cardStatusFlagDescription(String cardStatusFlagDescription) {
        this.setCardStatusFlagDescription(cardStatusFlagDescription);
        return this;
    }

    public void setCardStatusFlagDescription(String cardStatusFlagDescription) {
        this.cardStatusFlagDescription = cardStatusFlagDescription;
    }

    public String getCardStatusFlagDetails() {
        return this.cardStatusFlagDetails;
    }

    public CardStatusFlag cardStatusFlagDetails(String cardStatusFlagDetails) {
        this.setCardStatusFlagDetails(cardStatusFlagDetails);
        return this;
    }

    public void setCardStatusFlagDetails(String cardStatusFlagDetails) {
        this.cardStatusFlagDetails = cardStatusFlagDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardStatusFlag)) {
            return false;
        }
        return id != null && id.equals(((CardStatusFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardStatusFlag{" +
            "id=" + getId() +
            ", cardStatusFlag='" + getCardStatusFlag() + "'" +
            ", cardStatusFlagDescription='" + getCardStatusFlagDescription() + "'" +
            ", cardStatusFlagDetails='" + getCardStatusFlagDetails() + "'" +
            "}";
    }
}
