package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.enumeration.RemittanceType;
import io.github.erp.domain.enumeration.RemittanceTypeFlag;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RemittanceFlag} entity.
 */
public class RemittanceFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private RemittanceTypeFlag remittanceTypeFlag;

    @NotNull
    private RemittanceType remittanceType;

    @Lob
    private String remittanceTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemittanceTypeFlag getRemittanceTypeFlag() {
        return remittanceTypeFlag;
    }

    public void setRemittanceTypeFlag(RemittanceTypeFlag remittanceTypeFlag) {
        this.remittanceTypeFlag = remittanceTypeFlag;
    }

    public RemittanceType getRemittanceType() {
        return remittanceType;
    }

    public void setRemittanceType(RemittanceType remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getRemittanceTypeDetails() {
        return remittanceTypeDetails;
    }

    public void setRemittanceTypeDetails(String remittanceTypeDetails) {
        this.remittanceTypeDetails = remittanceTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RemittanceFlagDTO)) {
            return false;
        }

        RemittanceFlagDTO remittanceFlagDTO = (RemittanceFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, remittanceFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RemittanceFlagDTO{" +
            "id=" + getId() +
            ", remittanceTypeFlag='" + getRemittanceTypeFlag() + "'" +
            ", remittanceType='" + getRemittanceType() + "'" +
            ", remittanceTypeDetails='" + getRemittanceTypeDetails() + "'" +
            "}";
    }
}
