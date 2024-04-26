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

/**
 * A KenyanCurrencyDenomination.
 */
@Entity
@Table(name = "kenyan_currency_denomination")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "kenyancurrencydenomination")
public class KenyanCurrencyDenomination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "currency_denomination_code", nullable = false, unique = true)
    private String currencyDenominationCode;

    @NotNull
    @Column(name = "currency_denomination_type", nullable = false)
    private String currencyDenominationType;

    @Column(name = "currency_denomination_type_details")
    private String currencyDenominationTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KenyanCurrencyDenomination id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyDenominationCode() {
        return this.currencyDenominationCode;
    }

    public KenyanCurrencyDenomination currencyDenominationCode(String currencyDenominationCode) {
        this.setCurrencyDenominationCode(currencyDenominationCode);
        return this;
    }

    public void setCurrencyDenominationCode(String currencyDenominationCode) {
        this.currencyDenominationCode = currencyDenominationCode;
    }

    public String getCurrencyDenominationType() {
        return this.currencyDenominationType;
    }

    public KenyanCurrencyDenomination currencyDenominationType(String currencyDenominationType) {
        this.setCurrencyDenominationType(currencyDenominationType);
        return this;
    }

    public void setCurrencyDenominationType(String currencyDenominationType) {
        this.currencyDenominationType = currencyDenominationType;
    }

    public String getCurrencyDenominationTypeDetails() {
        return this.currencyDenominationTypeDetails;
    }

    public KenyanCurrencyDenomination currencyDenominationTypeDetails(String currencyDenominationTypeDetails) {
        this.setCurrencyDenominationTypeDetails(currencyDenominationTypeDetails);
        return this;
    }

    public void setCurrencyDenominationTypeDetails(String currencyDenominationTypeDetails) {
        this.currencyDenominationTypeDetails = currencyDenominationTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KenyanCurrencyDenomination)) {
            return false;
        }
        return id != null && id.equals(((KenyanCurrencyDenomination) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KenyanCurrencyDenomination{" +
            "id=" + getId() +
            ", currencyDenominationCode='" + getCurrencyDenominationCode() + "'" +
            ", currencyDenominationType='" + getCurrencyDenominationType() + "'" +
            ", currencyDenominationTypeDetails='" + getCurrencyDenominationTypeDetails() + "'" +
            "}";
    }
}
