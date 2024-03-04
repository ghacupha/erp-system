package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FinancialDerivativeTypeCode} entity.
 */
public class FinancialDerivativeTypeCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String financialDerivativeTypeCode;

    @NotNull
    private String financialDerivativeType;

    @Lob
    private String financialDerivativeTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFinancialDerivativeTypeCode() {
        return financialDerivativeTypeCode;
    }

    public void setFinancialDerivativeTypeCode(String financialDerivativeTypeCode) {
        this.financialDerivativeTypeCode = financialDerivativeTypeCode;
    }

    public String getFinancialDerivativeType() {
        return financialDerivativeType;
    }

    public void setFinancialDerivativeType(String financialDerivativeType) {
        this.financialDerivativeType = financialDerivativeType;
    }

    public String getFinancialDerivativeTypeDetails() {
        return financialDerivativeTypeDetails;
    }

    public void setFinancialDerivativeTypeDetails(String financialDerivativeTypeDetails) {
        this.financialDerivativeTypeDetails = financialDerivativeTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialDerivativeTypeCodeDTO)) {
            return false;
        }

        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = (FinancialDerivativeTypeCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialDerivativeTypeCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialDerivativeTypeCodeDTO{" +
            "id=" + getId() +
            ", financialDerivativeTypeCode='" + getFinancialDerivativeTypeCode() + "'" +
            ", financialDerivativeType='" + getFinancialDerivativeType() + "'" +
            ", financialDerivativeTypeDetails='" + getFinancialDerivativeTypeDetails() + "'" +
            "}";
    }
}
