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
import io.github.erp.domain.enumeration.SourceOrPurposeOfRemittancFlag;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SourceRemittancePurposeType} entity.
 */
public class SourceRemittancePurposeTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String sourceOrPurposeTypeCode;

    @NotNull
    private SourceOrPurposeOfRemittancFlag sourceOrPurposeOfRemittanceFlag;

    @NotNull
    private String sourceOrPurposeOfRemittanceType;

    @Lob
    private String remittancePurposeTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceOrPurposeTypeCode() {
        return sourceOrPurposeTypeCode;
    }

    public void setSourceOrPurposeTypeCode(String sourceOrPurposeTypeCode) {
        this.sourceOrPurposeTypeCode = sourceOrPurposeTypeCode;
    }

    public SourceOrPurposeOfRemittancFlag getSourceOrPurposeOfRemittanceFlag() {
        return sourceOrPurposeOfRemittanceFlag;
    }

    public void setSourceOrPurposeOfRemittanceFlag(SourceOrPurposeOfRemittancFlag sourceOrPurposeOfRemittanceFlag) {
        this.sourceOrPurposeOfRemittanceFlag = sourceOrPurposeOfRemittanceFlag;
    }

    public String getSourceOrPurposeOfRemittanceType() {
        return sourceOrPurposeOfRemittanceType;
    }

    public void setSourceOrPurposeOfRemittanceType(String sourceOrPurposeOfRemittanceType) {
        this.sourceOrPurposeOfRemittanceType = sourceOrPurposeOfRemittanceType;
    }

    public String getRemittancePurposeTypeDetails() {
        return remittancePurposeTypeDetails;
    }

    public void setRemittancePurposeTypeDetails(String remittancePurposeTypeDetails) {
        this.remittancePurposeTypeDetails = remittancePurposeTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceRemittancePurposeTypeDTO)) {
            return false;
        }

        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = (SourceRemittancePurposeTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourceRemittancePurposeTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceRemittancePurposeTypeDTO{" +
            "id=" + getId() +
            ", sourceOrPurposeTypeCode='" + getSourceOrPurposeTypeCode() + "'" +
            ", sourceOrPurposeOfRemittanceFlag='" + getSourceOrPurposeOfRemittanceFlag() + "'" +
            ", sourceOrPurposeOfRemittanceType='" + getSourceOrPurposeOfRemittanceType() + "'" +
            ", remittancePurposeTypeDetails='" + getRemittancePurposeTypeDetails() + "'" +
            "}";
    }
}
