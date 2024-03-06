package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * A DTO for the {@link io.github.erp.domain.FxTransactionRateType} entity.
 */
public class FxTransactionRateTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fxTransactionRateTypeCode;

    @NotNull
    private String fxTransactionRateType;

    @Lob
    private String fxTransactionRateTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionRateTypeCode() {
        return fxTransactionRateTypeCode;
    }

    public void setFxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.fxTransactionRateTypeCode = fxTransactionRateTypeCode;
    }

    public String getFxTransactionRateType() {
        return fxTransactionRateType;
    }

    public void setFxTransactionRateType(String fxTransactionRateType) {
        this.fxTransactionRateType = fxTransactionRateType;
    }

    public String getFxTransactionRateTypeDetails() {
        return fxTransactionRateTypeDetails;
    }

    public void setFxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.fxTransactionRateTypeDetails = fxTransactionRateTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionRateTypeDTO)) {
            return false;
        }

        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = (FxTransactionRateTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxTransactionRateTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionRateTypeDTO{" +
            "id=" + getId() +
            ", fxTransactionRateTypeCode='" + getFxTransactionRateTypeCode() + "'" +
            ", fxTransactionRateType='" + getFxTransactionRateType() + "'" +
            ", fxTransactionRateTypeDetails='" + getFxTransactionRateTypeDetails() + "'" +
            "}";
    }
}
