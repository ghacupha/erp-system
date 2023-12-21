package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
 * A DTO for the {@link io.github.erp.domain.AnticipatedMaturityPeriood} entity.
 */
public class AnticipatedMaturityPerioodDTO implements Serializable {

    private Long id;

    @NotNull
    private String anticipatedMaturityTenorCode;

    @NotNull
    private String aniticipatedMaturityTenorType;

    @Lob
    private String anticipatedMaturityTenorDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnticipatedMaturityTenorCode() {
        return anticipatedMaturityTenorCode;
    }

    public void setAnticipatedMaturityTenorCode(String anticipatedMaturityTenorCode) {
        this.anticipatedMaturityTenorCode = anticipatedMaturityTenorCode;
    }

    public String getAniticipatedMaturityTenorType() {
        return aniticipatedMaturityTenorType;
    }

    public void setAniticipatedMaturityTenorType(String aniticipatedMaturityTenorType) {
        this.aniticipatedMaturityTenorType = aniticipatedMaturityTenorType;
    }

    public String getAnticipatedMaturityTenorDetails() {
        return anticipatedMaturityTenorDetails;
    }

    public void setAnticipatedMaturityTenorDetails(String anticipatedMaturityTenorDetails) {
        this.anticipatedMaturityTenorDetails = anticipatedMaturityTenorDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnticipatedMaturityPerioodDTO)) {
            return false;
        }

        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = (AnticipatedMaturityPerioodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, anticipatedMaturityPerioodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnticipatedMaturityPerioodDTO{" +
            "id=" + getId() +
            ", anticipatedMaturityTenorCode='" + getAnticipatedMaturityTenorCode() + "'" +
            ", aniticipatedMaturityTenorType='" + getAniticipatedMaturityTenorType() + "'" +
            ", anticipatedMaturityTenorDetails='" + getAnticipatedMaturityTenorDetails() + "'" +
            "}";
    }
}
