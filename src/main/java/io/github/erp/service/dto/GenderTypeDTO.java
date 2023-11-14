package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.enumeration.genderTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.GenderType} entity.
 */
public class GenderTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String genderCode;

    @NotNull
    private genderTypes genderType;

    @Lob
    private String genderDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public genderTypes getGenderType() {
        return genderType;
    }

    public void setGenderType(genderTypes genderType) {
        this.genderType = genderType;
    }

    public String getGenderDescription() {
        return genderDescription;
    }

    public void setGenderDescription(String genderDescription) {
        this.genderDescription = genderDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenderTypeDTO)) {
            return false;
        }

        GenderTypeDTO genderTypeDTO = (GenderTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genderTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenderTypeDTO{" +
            "id=" + getId() +
            ", genderCode='" + getGenderCode() + "'" +
            ", genderType='" + getGenderType() + "'" +
            ", genderDescription='" + getGenderDescription() + "'" +
            "}";
    }
}
