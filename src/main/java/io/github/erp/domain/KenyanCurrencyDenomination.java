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
