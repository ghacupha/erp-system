package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * A DTO for the {@link io.github.erp.domain.StaffCurrentEmploymentStatus} entity.
 */
public class StaffCurrentEmploymentStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String staffCurrentEmploymentStatusTypeCode;

    @NotNull
    private String staffCurrentEmploymentStatusType;

    @Lob
    private String staffCurrentEmploymentStatusTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffCurrentEmploymentStatusTypeCode() {
        return staffCurrentEmploymentStatusTypeCode;
    }

    public void setStaffCurrentEmploymentStatusTypeCode(String staffCurrentEmploymentStatusTypeCode) {
        this.staffCurrentEmploymentStatusTypeCode = staffCurrentEmploymentStatusTypeCode;
    }

    public String getStaffCurrentEmploymentStatusType() {
        return staffCurrentEmploymentStatusType;
    }

    public void setStaffCurrentEmploymentStatusType(String staffCurrentEmploymentStatusType) {
        this.staffCurrentEmploymentStatusType = staffCurrentEmploymentStatusType;
    }

    public String getStaffCurrentEmploymentStatusTypeDetails() {
        return staffCurrentEmploymentStatusTypeDetails;
    }

    public void setStaffCurrentEmploymentStatusTypeDetails(String staffCurrentEmploymentStatusTypeDetails) {
        this.staffCurrentEmploymentStatusTypeDetails = staffCurrentEmploymentStatusTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffCurrentEmploymentStatusDTO)) {
            return false;
        }

        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = (StaffCurrentEmploymentStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffCurrentEmploymentStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffCurrentEmploymentStatusDTO{" +
            "id=" + getId() +
            ", staffCurrentEmploymentStatusTypeCode='" + getStaffCurrentEmploymentStatusTypeCode() + "'" +
            ", staffCurrentEmploymentStatusType='" + getStaffCurrentEmploymentStatusType() + "'" +
            ", staffCurrentEmploymentStatusTypeDetails='" + getStaffCurrentEmploymentStatusTypeDetails() + "'" +
            "}";
    }
}
