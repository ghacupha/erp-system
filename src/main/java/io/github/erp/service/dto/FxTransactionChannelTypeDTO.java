package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FxTransactionChannelType} entity.
 */
public class FxTransactionChannelTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fxTransactionChannelCode;

    @NotNull
    private String fxTransactionChannelType;

    @Lob
    private String fxChannelTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionChannelCode() {
        return fxTransactionChannelCode;
    }

    public void setFxTransactionChannelCode(String fxTransactionChannelCode) {
        this.fxTransactionChannelCode = fxTransactionChannelCode;
    }

    public String getFxTransactionChannelType() {
        return fxTransactionChannelType;
    }

    public void setFxTransactionChannelType(String fxTransactionChannelType) {
        this.fxTransactionChannelType = fxTransactionChannelType;
    }

    public String getFxChannelTypeDetails() {
        return fxChannelTypeDetails;
    }

    public void setFxChannelTypeDetails(String fxChannelTypeDetails) {
        this.fxChannelTypeDetails = fxChannelTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionChannelTypeDTO)) {
            return false;
        }

        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = (FxTransactionChannelTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxTransactionChannelTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionChannelTypeDTO{" +
            "id=" + getId() +
            ", fxTransactionChannelCode='" + getFxTransactionChannelCode() + "'" +
            ", fxTransactionChannelType='" + getFxTransactionChannelType() + "'" +
            ", fxChannelTypeDetails='" + getFxChannelTypeDetails() + "'" +
            "}";
    }
}
