package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * A DTO for the {@link io.github.erp.domain.CrbComplaintType} entity.
 */
public class CrbComplaintTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String complaintTypeCode;

    @NotNull
    private String complaintType;

    @Lob
    private String complaintTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintTypeCode() {
        return complaintTypeCode;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintTypeDetails() {
        return complaintTypeDetails;
    }

    public void setComplaintTypeDetails(String complaintTypeDetails) {
        this.complaintTypeDetails = complaintTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbComplaintTypeDTO)) {
            return false;
        }

        CrbComplaintTypeDTO crbComplaintTypeDTO = (CrbComplaintTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbComplaintTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintTypeDTO{" +
            "id=" + getId() +
            ", complaintTypeCode='" + getComplaintTypeCode() + "'" +
            ", complaintType='" + getComplaintType() + "'" +
            ", complaintTypeDetails='" + getComplaintTypeDetails() + "'" +
            "}";
    }
}
