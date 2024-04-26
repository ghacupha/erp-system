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
