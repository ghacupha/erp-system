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
 * A DTO for the {@link io.github.erp.domain.CrbCreditApplicationStatus} entity.
 */
public class CrbCreditApplicationStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String crbCreditApplicationStatusTypeCode;

    @NotNull
    private String crbCreditApplicationStatusType;

    @Lob
    private String crbCreditApplicationStatusDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrbCreditApplicationStatusTypeCode() {
        return crbCreditApplicationStatusTypeCode;
    }

    public void setCrbCreditApplicationStatusTypeCode(String crbCreditApplicationStatusTypeCode) {
        this.crbCreditApplicationStatusTypeCode = crbCreditApplicationStatusTypeCode;
    }

    public String getCrbCreditApplicationStatusType() {
        return crbCreditApplicationStatusType;
    }

    public void setCrbCreditApplicationStatusType(String crbCreditApplicationStatusType) {
        this.crbCreditApplicationStatusType = crbCreditApplicationStatusType;
    }

    public String getCrbCreditApplicationStatusDetails() {
        return crbCreditApplicationStatusDetails;
    }

    public void setCrbCreditApplicationStatusDetails(String crbCreditApplicationStatusDetails) {
        this.crbCreditApplicationStatusDetails = crbCreditApplicationStatusDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbCreditApplicationStatusDTO)) {
            return false;
        }

        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = (CrbCreditApplicationStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbCreditApplicationStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCreditApplicationStatusDTO{" +
            "id=" + getId() +
            ", crbCreditApplicationStatusTypeCode='" + getCrbCreditApplicationStatusTypeCode() + "'" +
            ", crbCreditApplicationStatusType='" + getCrbCreditApplicationStatusType() + "'" +
            ", crbCreditApplicationStatusDetails='" + getCrbCreditApplicationStatusDetails() + "'" +
            "}";
    }
}
