package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
 * A DTO for the {@link io.github.erp.domain.CrbCreditFacilityType} entity.
 */
public class CrbCreditFacilityTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String creditFacilityTypeCode;

    @NotNull
    private String creditFacilityType;

    @Lob
    private String creditFacilityDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditFacilityTypeCode() {
        return creditFacilityTypeCode;
    }

    public void setCreditFacilityTypeCode(String creditFacilityTypeCode) {
        this.creditFacilityTypeCode = creditFacilityTypeCode;
    }

    public String getCreditFacilityType() {
        return creditFacilityType;
    }

    public void setCreditFacilityType(String creditFacilityType) {
        this.creditFacilityType = creditFacilityType;
    }

    public String getCreditFacilityDescription() {
        return creditFacilityDescription;
    }

    public void setCreditFacilityDescription(String creditFacilityDescription) {
        this.creditFacilityDescription = creditFacilityDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbCreditFacilityTypeDTO)) {
            return false;
        }

        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = (CrbCreditFacilityTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbCreditFacilityTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCreditFacilityTypeDTO{" +
            "id=" + getId() +
            ", creditFacilityTypeCode='" + getCreditFacilityTypeCode() + "'" +
            ", creditFacilityType='" + getCreditFacilityType() + "'" +
            ", creditFacilityDescription='" + getCreditFacilityDescription() + "'" +
            "}";
    }
}
