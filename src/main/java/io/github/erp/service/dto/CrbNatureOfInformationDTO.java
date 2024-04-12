package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
 * A DTO for the {@link io.github.erp.domain.CrbNatureOfInformation} entity.
 */
public class CrbNatureOfInformationDTO implements Serializable {

    private Long id;

    @NotNull
    private String natureOfInformationTypeCode;

    @NotNull
    private String natureOfInformationType;

    @Lob
    private String natureOfInformationTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatureOfInformationTypeCode() {
        return natureOfInformationTypeCode;
    }

    public void setNatureOfInformationTypeCode(String natureOfInformationTypeCode) {
        this.natureOfInformationTypeCode = natureOfInformationTypeCode;
    }

    public String getNatureOfInformationType() {
        return natureOfInformationType;
    }

    public void setNatureOfInformationType(String natureOfInformationType) {
        this.natureOfInformationType = natureOfInformationType;
    }

    public String getNatureOfInformationTypeDescription() {
        return natureOfInformationTypeDescription;
    }

    public void setNatureOfInformationTypeDescription(String natureOfInformationTypeDescription) {
        this.natureOfInformationTypeDescription = natureOfInformationTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbNatureOfInformationDTO)) {
            return false;
        }

        CrbNatureOfInformationDTO crbNatureOfInformationDTO = (CrbNatureOfInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbNatureOfInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbNatureOfInformationDTO{" +
            "id=" + getId() +
            ", natureOfInformationTypeCode='" + getNatureOfInformationTypeCode() + "'" +
            ", natureOfInformationType='" + getNatureOfInformationType() + "'" +
            ", natureOfInformationTypeDescription='" + getNatureOfInformationTypeDescription() + "'" +
            "}";
    }
}
