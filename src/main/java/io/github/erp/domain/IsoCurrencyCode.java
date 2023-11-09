package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

/**
 * A IsoCurrencyCode.
 */
@Entity
@Table(name = "iso_currency_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isocurrencycode")
public class IsoCurrencyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "alphabetic_code", nullable = false)
    private String alphabeticCode;

    @NotNull
    @Column(name = "numeric_code", nullable = false)
    private String numericCode;

    @NotNull
    @Column(name = "minor_unit", nullable = false)
    private String minorUnit;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "country")
    private String country;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IsoCurrencyCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlphabeticCode() {
        return this.alphabeticCode;
    }

    public IsoCurrencyCode alphabeticCode(String alphabeticCode) {
        this.setAlphabeticCode(alphabeticCode);
        return this;
    }

    public void setAlphabeticCode(String alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public String getNumericCode() {
        return this.numericCode;
    }

    public IsoCurrencyCode numericCode(String numericCode) {
        this.setNumericCode(numericCode);
        return this;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getMinorUnit() {
        return this.minorUnit;
    }

    public IsoCurrencyCode minorUnit(String minorUnit) {
        this.setMinorUnit(minorUnit);
        return this;
    }

    public void setMinorUnit(String minorUnit) {
        this.minorUnit = minorUnit;
    }

    public String getCurrency() {
        return this.currency;
    }

    public IsoCurrencyCode currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return this.country;
    }

    public IsoCurrencyCode country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsoCurrencyCode)) {
            return false;
        }
        return id != null && id.equals(((IsoCurrencyCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCurrencyCode{" +
            "id=" + getId() +
            ", alphabeticCode='" + getAlphabeticCode() + "'" +
            ", numericCode='" + getNumericCode() + "'" +
            ", minorUnit='" + getMinorUnit() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
