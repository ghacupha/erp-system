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
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AcquiringIssuingFlag.
 */
@Entity
@Table(name = "acquiring_issuing_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "acquiringissuingflag")
public class AcquiringIssuingFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_acquiring_issuing_flag_code", nullable = false, unique = true)
    private String cardAcquiringIssuingFlagCode;

    @NotNull
    @Column(name = "card_acquiring_issuing_description", nullable = false, unique = true)
    private String cardAcquiringIssuingDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "card_acquiring_issuing_details")
    private String cardAcquiringIssuingDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AcquiringIssuingFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardAcquiringIssuingFlagCode() {
        return this.cardAcquiringIssuingFlagCode;
    }

    public AcquiringIssuingFlag cardAcquiringIssuingFlagCode(String cardAcquiringIssuingFlagCode) {
        this.setCardAcquiringIssuingFlagCode(cardAcquiringIssuingFlagCode);
        return this;
    }

    public void setCardAcquiringIssuingFlagCode(String cardAcquiringIssuingFlagCode) {
        this.cardAcquiringIssuingFlagCode = cardAcquiringIssuingFlagCode;
    }

    public String getCardAcquiringIssuingDescription() {
        return this.cardAcquiringIssuingDescription;
    }

    public AcquiringIssuingFlag cardAcquiringIssuingDescription(String cardAcquiringIssuingDescription) {
        this.setCardAcquiringIssuingDescription(cardAcquiringIssuingDescription);
        return this;
    }

    public void setCardAcquiringIssuingDescription(String cardAcquiringIssuingDescription) {
        this.cardAcquiringIssuingDescription = cardAcquiringIssuingDescription;
    }

    public String getCardAcquiringIssuingDetails() {
        return this.cardAcquiringIssuingDetails;
    }

    public AcquiringIssuingFlag cardAcquiringIssuingDetails(String cardAcquiringIssuingDetails) {
        this.setCardAcquiringIssuingDetails(cardAcquiringIssuingDetails);
        return this;
    }

    public void setCardAcquiringIssuingDetails(String cardAcquiringIssuingDetails) {
        this.cardAcquiringIssuingDetails = cardAcquiringIssuingDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcquiringIssuingFlag)) {
            return false;
        }
        return id != null && id.equals(((AcquiringIssuingFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcquiringIssuingFlag{" +
            "id=" + getId() +
            ", cardAcquiringIssuingFlagCode='" + getCardAcquiringIssuingFlagCode() + "'" +
            ", cardAcquiringIssuingDescription='" + getCardAcquiringIssuingDescription() + "'" +
            ", cardAcquiringIssuingDetails='" + getCardAcquiringIssuingDetails() + "'" +
            "}";
    }
}
