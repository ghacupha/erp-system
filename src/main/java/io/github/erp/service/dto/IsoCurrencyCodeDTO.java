package io.github.erp.service.dto;

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

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.IsoCurrencyCode} entity.
 */
public class IsoCurrencyCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String alphabeticCode;

    @NotNull
    private String numericCode;

    @NotNull
    private String minorUnit;

    @NotNull
    private String currency;

    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlphabeticCode() {
        return alphabeticCode;
    }

    public void setAlphabeticCode(String alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getMinorUnit() {
        return minorUnit;
    }

    public void setMinorUnit(String minorUnit) {
        this.minorUnit = minorUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsoCurrencyCodeDTO)) {
            return false;
        }

        IsoCurrencyCodeDTO isoCurrencyCodeDTO = (IsoCurrencyCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isoCurrencyCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCurrencyCodeDTO{" +
            "id=" + getId() +
            ", alphabeticCode='" + getAlphabeticCode() + "'" +
            ", numericCode='" + getNumericCode() + "'" +
            ", minorUnit='" + getMinorUnit() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
