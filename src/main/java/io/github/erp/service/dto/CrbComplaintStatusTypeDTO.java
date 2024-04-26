package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
