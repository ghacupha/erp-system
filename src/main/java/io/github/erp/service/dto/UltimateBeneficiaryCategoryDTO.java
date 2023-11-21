package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
 * A DTO for the {@link io.github.erp.domain.UltimateBeneficiaryCategory} entity.
 */
public class UltimateBeneficiaryCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String ultimateBeneficiaryCategoryTypeCode;

    @NotNull
    private String ultimateBeneficiaryType;

    @Lob
    private String ultimateBeneficiaryCategoryTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUltimateBeneficiaryCategoryTypeCode() {
        return ultimateBeneficiaryCategoryTypeCode;
    }

    public void setUltimateBeneficiaryCategoryTypeCode(String ultimateBeneficiaryCategoryTypeCode) {
        this.ultimateBeneficiaryCategoryTypeCode = ultimateBeneficiaryCategoryTypeCode;
    }

    public String getUltimateBeneficiaryType() {
        return ultimateBeneficiaryType;
    }

    public void setUltimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.ultimateBeneficiaryType = ultimateBeneficiaryType;
    }

    public String getUltimateBeneficiaryCategoryTypeDetails() {
        return ultimateBeneficiaryCategoryTypeDetails;
    }

    public void setUltimateBeneficiaryCategoryTypeDetails(String ultimateBeneficiaryCategoryTypeDetails) {
        this.ultimateBeneficiaryCategoryTypeDetails = ultimateBeneficiaryCategoryTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UltimateBeneficiaryCategoryDTO)) {
            return false;
        }

        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = (UltimateBeneficiaryCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ultimateBeneficiaryCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UltimateBeneficiaryCategoryDTO{" +
            "id=" + getId() +
            ", ultimateBeneficiaryCategoryTypeCode='" + getUltimateBeneficiaryCategoryTypeCode() + "'" +
            ", ultimateBeneficiaryType='" + getUltimateBeneficiaryType() + "'" +
            ", ultimateBeneficiaryCategoryTypeDetails='" + getUltimateBeneficiaryCategoryTypeDetails() + "'" +
            "}";
    }
}
