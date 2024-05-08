package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
 * A DTO for the {@link io.github.erp.domain.BusinessSegmentTypes} entity.
 */
public class BusinessSegmentTypesDTO implements Serializable {

    private Long id;

    @NotNull
    private String businessEconomicSegmentCode;

    @NotNull
    private String businessEconomicSegment;

    @Lob
    private String details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessEconomicSegmentCode() {
        return businessEconomicSegmentCode;
    }

    public void setBusinessEconomicSegmentCode(String businessEconomicSegmentCode) {
        this.businessEconomicSegmentCode = businessEconomicSegmentCode;
    }

    public String getBusinessEconomicSegment() {
        return businessEconomicSegment;
    }

    public void setBusinessEconomicSegment(String businessEconomicSegment) {
        this.businessEconomicSegment = businessEconomicSegment;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessSegmentTypesDTO)) {
            return false;
        }

        BusinessSegmentTypesDTO businessSegmentTypesDTO = (BusinessSegmentTypesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessSegmentTypesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessSegmentTypesDTO{" +
            "id=" + getId() +
            ", businessEconomicSegmentCode='" + getBusinessEconomicSegmentCode() + "'" +
            ", businessEconomicSegment='" + getBusinessEconomicSegment() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
