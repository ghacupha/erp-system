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
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CrbAgingBands} entity.
 */
public class CrbAgingBandsDTO implements Serializable {

    private Long id;

    @NotNull
    private String agingBandCategoryCode;

    @NotNull
    private String agingBandCategory;

    @NotNull
    private String agingBandCategoryDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgingBandCategoryCode() {
        return agingBandCategoryCode;
    }

    public void setAgingBandCategoryCode(String agingBandCategoryCode) {
        this.agingBandCategoryCode = agingBandCategoryCode;
    }

    public String getAgingBandCategory() {
        return agingBandCategory;
    }

    public void setAgingBandCategory(String agingBandCategory) {
        this.agingBandCategory = agingBandCategory;
    }

    public String getAgingBandCategoryDetails() {
        return agingBandCategoryDetails;
    }

    public void setAgingBandCategoryDetails(String agingBandCategoryDetails) {
        this.agingBandCategoryDetails = agingBandCategoryDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAgingBandsDTO)) {
            return false;
        }

        CrbAgingBandsDTO crbAgingBandsDTO = (CrbAgingBandsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbAgingBandsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAgingBandsDTO{" +
            "id=" + getId() +
            ", agingBandCategoryCode='" + getAgingBandCategoryCode() + "'" +
            ", agingBandCategory='" + getAgingBandCategory() + "'" +
            ", agingBandCategoryDetails='" + getAgingBandCategoryDetails() + "'" +
            "}";
    }
}
