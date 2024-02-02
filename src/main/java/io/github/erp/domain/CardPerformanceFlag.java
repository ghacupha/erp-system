package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.enumeration.CardPerformanceFlags;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CardPerformanceFlag.
 */
@Entity
@Table(name = "card_performance_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardperformanceflag")
public class CardPerformanceFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_performance_flag", nullable = false)
    private CardPerformanceFlags cardPerformanceFlag;

    @NotNull
    @Column(name = "card_performance_flag_description", nullable = false, unique = true)
    private String cardPerformanceFlagDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_performance_flag_details")
    private String cardPerformanceFlagDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardPerformanceFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardPerformanceFlags getCardPerformanceFlag() {
        return this.cardPerformanceFlag;
    }

    public CardPerformanceFlag cardPerformanceFlag(CardPerformanceFlags cardPerformanceFlag) {
        this.setCardPerformanceFlag(cardPerformanceFlag);
        return this;
    }

    public void setCardPerformanceFlag(CardPerformanceFlags cardPerformanceFlag) {
        this.cardPerformanceFlag = cardPerformanceFlag;
    }

    public String getCardPerformanceFlagDescription() {
        return this.cardPerformanceFlagDescription;
    }

    public CardPerformanceFlag cardPerformanceFlagDescription(String cardPerformanceFlagDescription) {
        this.setCardPerformanceFlagDescription(cardPerformanceFlagDescription);
        return this;
    }

    public void setCardPerformanceFlagDescription(String cardPerformanceFlagDescription) {
        this.cardPerformanceFlagDescription = cardPerformanceFlagDescription;
    }

    public String getCardPerformanceFlagDetails() {
        return this.cardPerformanceFlagDetails;
    }

    public CardPerformanceFlag cardPerformanceFlagDetails(String cardPerformanceFlagDetails) {
        this.setCardPerformanceFlagDetails(cardPerformanceFlagDetails);
        return this;
    }

    public void setCardPerformanceFlagDetails(String cardPerformanceFlagDetails) {
        this.cardPerformanceFlagDetails = cardPerformanceFlagDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardPerformanceFlag)) {
            return false;
        }
        return id != null && id.equals(((CardPerformanceFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardPerformanceFlag{" +
            "id=" + getId() +
            ", cardPerformanceFlag='" + getCardPerformanceFlag() + "'" +
            ", cardPerformanceFlagDescription='" + getCardPerformanceFlagDescription() + "'" +
            ", cardPerformanceFlagDetails='" + getCardPerformanceFlagDetails() + "'" +
            "}";
    }
}
