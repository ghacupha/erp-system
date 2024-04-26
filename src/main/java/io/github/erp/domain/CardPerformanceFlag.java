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
