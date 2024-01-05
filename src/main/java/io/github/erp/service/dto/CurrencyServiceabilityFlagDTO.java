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
import io.github.erp.domain.enumeration.CurrencyServiceability;
import io.github.erp.domain.enumeration.CurrencyServiceabilityFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CurrencyServiceabilityFlag} entity.
 */
public class CurrencyServiceabilityFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private CurrencyServiceabilityFlagTypes currencyServiceabilityFlag;

    @NotNull
    private CurrencyServiceability currencyServiceability;

    @Lob
    private String currencyServiceabilityFlagDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyServiceabilityFlagTypes getCurrencyServiceabilityFlag() {
        return currencyServiceabilityFlag;
    }

    public void setCurrencyServiceabilityFlag(CurrencyServiceabilityFlagTypes currencyServiceabilityFlag) {
        this.currencyServiceabilityFlag = currencyServiceabilityFlag;
    }

    public CurrencyServiceability getCurrencyServiceability() {
        return currencyServiceability;
    }

    public void setCurrencyServiceability(CurrencyServiceability currencyServiceability) {
        this.currencyServiceability = currencyServiceability;
    }

    public String getCurrencyServiceabilityFlagDetails() {
        return currencyServiceabilityFlagDetails;
    }

    public void setCurrencyServiceabilityFlagDetails(String currencyServiceabilityFlagDetails) {
        this.currencyServiceabilityFlagDetails = currencyServiceabilityFlagDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyServiceabilityFlagDTO)) {
            return false;
        }

        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = (CurrencyServiceabilityFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyServiceabilityFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyServiceabilityFlagDTO{" +
            "id=" + getId() +
            ", currencyServiceabilityFlag='" + getCurrencyServiceabilityFlag() + "'" +
            ", currencyServiceability='" + getCurrencyServiceability() + "'" +
            ", currencyServiceabilityFlagDetails='" + getCurrencyServiceabilityFlagDetails() + "'" +
            "}";
    }
}
