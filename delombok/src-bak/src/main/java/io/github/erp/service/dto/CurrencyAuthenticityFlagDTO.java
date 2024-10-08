package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.enumeration.CurrencyAuthenticityFlags;
import io.github.erp.domain.enumeration.CurrencyAuthenticityTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CurrencyAuthenticityFlag} entity.
 */
public class CurrencyAuthenticityFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private CurrencyAuthenticityFlags currencyAuthenticityFlag;

    @NotNull
    private CurrencyAuthenticityTypes currencyAuthenticityType;

    @Lob
    private String currencyAuthenticityTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyAuthenticityFlags getCurrencyAuthenticityFlag() {
        return currencyAuthenticityFlag;
    }

    public void setCurrencyAuthenticityFlag(CurrencyAuthenticityFlags currencyAuthenticityFlag) {
        this.currencyAuthenticityFlag = currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityTypes getCurrencyAuthenticityType() {
        return currencyAuthenticityType;
    }

    public void setCurrencyAuthenticityType(CurrencyAuthenticityTypes currencyAuthenticityType) {
        this.currencyAuthenticityType = currencyAuthenticityType;
    }

    public String getCurrencyAuthenticityTypeDetails() {
        return currencyAuthenticityTypeDetails;
    }

    public void setCurrencyAuthenticityTypeDetails(String currencyAuthenticityTypeDetails) {
        this.currencyAuthenticityTypeDetails = currencyAuthenticityTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyAuthenticityFlagDTO)) {
            return false;
        }

        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = (CurrencyAuthenticityFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyAuthenticityFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyAuthenticityFlagDTO{" +
            "id=" + getId() +
            ", currencyAuthenticityFlag='" + getCurrencyAuthenticityFlag() + "'" +
            ", currencyAuthenticityType='" + getCurrencyAuthenticityType() + "'" +
            ", currencyAuthenticityTypeDetails='" + getCurrencyAuthenticityTypeDetails() + "'" +
            "}";
    }
}
