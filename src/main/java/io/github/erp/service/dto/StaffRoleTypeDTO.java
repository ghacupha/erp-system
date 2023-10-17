package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
 * A DTO for the {@link io.github.erp.domain.StaffRoleType} entity.
 */
public class StaffRoleTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String staffRoleTypeCode;

    @NotNull
    private String staffRoleType;

    @Lob
    private String staffRoleTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffRoleTypeCode() {
        return staffRoleTypeCode;
    }

    public void setStaffRoleTypeCode(String staffRoleTypeCode) {
        this.staffRoleTypeCode = staffRoleTypeCode;
    }

    public String getStaffRoleType() {
        return staffRoleType;
    }

    public void setStaffRoleType(String staffRoleType) {
        this.staffRoleType = staffRoleType;
    }

    public String getStaffRoleTypeDetails() {
        return staffRoleTypeDetails;
    }

    public void setStaffRoleTypeDetails(String staffRoleTypeDetails) {
        this.staffRoleTypeDetails = staffRoleTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffRoleTypeDTO)) {
            return false;
        }

        StaffRoleTypeDTO staffRoleTypeDTO = (StaffRoleTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffRoleTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffRoleTypeDTO{" +
            "id=" + getId() +
            ", staffRoleTypeCode='" + getStaffRoleTypeCode() + "'" +
            ", staffRoleType='" + getStaffRoleType() + "'" +
            ", staffRoleTypeDetails='" + getStaffRoleTypeDetails() + "'" +
            "}";
    }
}
