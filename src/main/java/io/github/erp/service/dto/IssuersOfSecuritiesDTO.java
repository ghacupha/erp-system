package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.IssuersOfSecurities} entity.
 */
public class IssuersOfSecuritiesDTO implements Serializable {

    private Long id;

    @NotNull
    private String issuerOfSecuritiesCode;

    @NotNull
    private String issuerOfSecurities;

    @Lob
    private String issuerOfSecuritiesDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuerOfSecuritiesCode() {
        return issuerOfSecuritiesCode;
    }

    public void setIssuerOfSecuritiesCode(String issuerOfSecuritiesCode) {
        this.issuerOfSecuritiesCode = issuerOfSecuritiesCode;
    }

    public String getIssuerOfSecurities() {
        return issuerOfSecurities;
    }

    public void setIssuerOfSecurities(String issuerOfSecurities) {
        this.issuerOfSecurities = issuerOfSecurities;
    }

    public String getIssuerOfSecuritiesDescription() {
        return issuerOfSecuritiesDescription;
    }

    public void setIssuerOfSecuritiesDescription(String issuerOfSecuritiesDescription) {
        this.issuerOfSecuritiesDescription = issuerOfSecuritiesDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssuersOfSecuritiesDTO)) {
            return false;
        }

        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = (IssuersOfSecuritiesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issuersOfSecuritiesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssuersOfSecuritiesDTO{" +
            "id=" + getId() +
            ", issuerOfSecuritiesCode='" + getIssuerOfSecuritiesCode() + "'" +
            ", issuerOfSecurities='" + getIssuerOfSecurities() + "'" +
            ", issuerOfSecuritiesDescription='" + getIssuerOfSecuritiesDescription() + "'" +
            "}";
    }
}
