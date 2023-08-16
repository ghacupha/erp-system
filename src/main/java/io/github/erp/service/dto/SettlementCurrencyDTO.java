package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SettlementCurrency} entity.
 */
public class SettlementCurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    private String iso4217CurrencyCode;

    @NotNull
    private String currencyName;

    @NotNull
    private String country;

    private String numericCode;

    private String minorUnit;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso4217CurrencyCode() {
        return iso4217CurrencyCode;
    }

    public void setIso4217CurrencyCode(String iso4217CurrencyCode) {
        this.iso4217CurrencyCode = iso4217CurrencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementCurrencyDTO)) {
            return false;
        }

        SettlementCurrencyDTO settlementCurrencyDTO = (SettlementCurrencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settlementCurrencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementCurrencyDTO{" +
            "id=" + getId() +
            ", iso4217CurrencyCode='" + getIso4217CurrencyCode() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", country='" + getCountry() + "'" +
            ", numericCode='" + getNumericCode() + "'" +
            ", minorUnit='" + getMinorUnit() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
