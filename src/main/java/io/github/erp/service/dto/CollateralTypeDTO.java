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
 * A DTO for the {@link io.github.erp.domain.CollateralType} entity.
 */
public class CollateralTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String collateralTypeCode;

    @NotNull
    private String collateralType;

    @Lob
    private String collateralTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollateralTypeCode() {
        return collateralTypeCode;
    }

    public void setCollateralTypeCode(String collateralTypeCode) {
        this.collateralTypeCode = collateralTypeCode;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getCollateralTypeDescription() {
        return collateralTypeDescription;
    }

    public void setCollateralTypeDescription(String collateralTypeDescription) {
        this.collateralTypeDescription = collateralTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollateralTypeDTO)) {
            return false;
        }

        CollateralTypeDTO collateralTypeDTO = (CollateralTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collateralTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralTypeDTO{" +
            "id=" + getId() +
            ", collateralTypeCode='" + getCollateralTypeCode() + "'" +
            ", collateralType='" + getCollateralType() + "'" +
            ", collateralTypeDescription='" + getCollateralTypeDescription() + "'" +
            "}";
    }
}
