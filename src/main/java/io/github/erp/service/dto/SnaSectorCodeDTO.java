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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SnaSectorCode} entity.
 */
public class SnaSectorCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String sectorTypeCode;

    private String mainSectorCode;

    private String mainSectorTypeName;

    private String subSectorCode;

    private String subSectorName;

    private String subSubSectorCode;

    private String subSubSectorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorTypeCode() {
        return sectorTypeCode;
    }

    public void setSectorTypeCode(String sectorTypeCode) {
        this.sectorTypeCode = sectorTypeCode;
    }

    public String getMainSectorCode() {
        return mainSectorCode;
    }

    public void setMainSectorCode(String mainSectorCode) {
        this.mainSectorCode = mainSectorCode;
    }

    public String getMainSectorTypeName() {
        return mainSectorTypeName;
    }

    public void setMainSectorTypeName(String mainSectorTypeName) {
        this.mainSectorTypeName = mainSectorTypeName;
    }

    public String getSubSectorCode() {
        return subSectorCode;
    }

    public void setSubSectorCode(String subSectorCode) {
        this.subSectorCode = subSectorCode;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public String getSubSubSectorCode() {
        return subSubSectorCode;
    }

    public void setSubSubSectorCode(String subSubSectorCode) {
        this.subSubSectorCode = subSubSectorCode;
    }

    public String getSubSubSectorName() {
        return subSubSectorName;
    }

    public void setSubSubSectorName(String subSubSectorName) {
        this.subSubSectorName = subSubSectorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SnaSectorCodeDTO)) {
            return false;
        }

        SnaSectorCodeDTO snaSectorCodeDTO = (SnaSectorCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, snaSectorCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnaSectorCodeDTO{" +
            "id=" + getId() +
            ", sectorTypeCode='" + getSectorTypeCode() + "'" +
            ", mainSectorCode='" + getMainSectorCode() + "'" +
            ", mainSectorTypeName='" + getMainSectorTypeName() + "'" +
            ", subSectorCode='" + getSubSectorCode() + "'" +
            ", subSectorName='" + getSubSectorName() + "'" +
            ", subSubSectorCode='" + getSubSubSectorCode() + "'" +
            ", subSubSectorName='" + getSubSubSectorName() + "'" +
            "}";
    }
}
