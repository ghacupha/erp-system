package io.github.erp.service.dto;

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
