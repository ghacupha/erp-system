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
 * A DTO for the {@link io.github.erp.domain.CrbComplaintStatusType} entity.
 */
public class CrbComplaintStatusTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String complaintStatusTypeCode;

    @NotNull
    private String complaintStatusType;

    @Lob
    private String complaintStatusDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintStatusTypeCode() {
        return complaintStatusTypeCode;
    }

    public void setComplaintStatusTypeCode(String complaintStatusTypeCode) {
        this.complaintStatusTypeCode = complaintStatusTypeCode;
    }

    public String getComplaintStatusType() {
        return complaintStatusType;
    }

    public void setComplaintStatusType(String complaintStatusType) {
        this.complaintStatusType = complaintStatusType;
    }

    public String getComplaintStatusDetails() {
        return complaintStatusDetails;
    }

    public void setComplaintStatusDetails(String complaintStatusDetails) {
        this.complaintStatusDetails = complaintStatusDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbComplaintStatusTypeDTO)) {
            return false;
        }

        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = (CrbComplaintStatusTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbComplaintStatusTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintStatusTypeDTO{" +
            "id=" + getId() +
            ", complaintStatusTypeCode='" + getComplaintStatusTypeCode() + "'" +
            ", complaintStatusType='" + getComplaintStatusType() + "'" +
            ", complaintStatusDetails='" + getComplaintStatusDetails() + "'" +
            "}";
    }
}
