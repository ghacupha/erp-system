package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
 * A DTO for the {@link io.github.erp.domain.SourcesOfFundsTypeCode} entity.
 */
public class SourcesOfFundsTypeCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String sourceOfFundsTypeCode;

    @NotNull
    private String sourceOfFundsType;

    @Lob
    private String sourceOfFundsTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceOfFundsTypeCode() {
        return sourceOfFundsTypeCode;
    }

    public void setSourceOfFundsTypeCode(String sourceOfFundsTypeCode) {
        this.sourceOfFundsTypeCode = sourceOfFundsTypeCode;
    }

    public String getSourceOfFundsType() {
        return sourceOfFundsType;
    }

    public void setSourceOfFundsType(String sourceOfFundsType) {
        this.sourceOfFundsType = sourceOfFundsType;
    }

    public String getSourceOfFundsTypeDetails() {
        return sourceOfFundsTypeDetails;
    }

    public void setSourceOfFundsTypeDetails(String sourceOfFundsTypeDetails) {
        this.sourceOfFundsTypeDetails = sourceOfFundsTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourcesOfFundsTypeCodeDTO)) {
            return false;
        }

        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = (SourcesOfFundsTypeCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourcesOfFundsTypeCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourcesOfFundsTypeCodeDTO{" +
            "id=" + getId() +
            ", sourceOfFundsTypeCode='" + getSourceOfFundsTypeCode() + "'" +
            ", sourceOfFundsType='" + getSourceOfFundsType() + "'" +
            ", sourceOfFundsTypeDetails='" + getSourceOfFundsTypeDetails() + "'" +
            "}";
    }
}
