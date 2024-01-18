package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepartmentType} entity.
 */
public class DepartmentTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String departmentTypeCode;

    @NotNull
    private String departmentType;

    @Lob
    private String departmentTypeDetails;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentTypeCode() {
        return departmentTypeCode;
    }

    public void setDepartmentTypeCode(String departmentTypeCode) {
        this.departmentTypeCode = departmentTypeCode;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getDepartmentTypeDetails() {
        return departmentTypeDetails;
    }

    public void setDepartmentTypeDetails(String departmentTypeDetails) {
        this.departmentTypeDetails = departmentTypeDetails;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentTypeDTO)) {
            return false;
        }

        DepartmentTypeDTO departmentTypeDTO = (DepartmentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentTypeDTO{" +
            "id=" + getId() +
            ", departmentTypeCode='" + getDepartmentTypeCode() + "'" +
            ", departmentType='" + getDepartmentType() + "'" +
            ", departmentTypeDetails='" + getDepartmentTypeDetails() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
