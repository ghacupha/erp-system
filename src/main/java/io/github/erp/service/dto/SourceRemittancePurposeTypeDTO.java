package io.github.erp.service.dto;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
