package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * A DTO for the {@link io.github.erp.domain.ContractStatus} entity.
 */
public class ContractStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String contractStatusCode;

    @NotNull
    private String contractStatusType;

    @Lob
    private String contractStatusTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractStatusCode() {
        return contractStatusCode;
    }

    public void setContractStatusCode(String contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    public String getContractStatusType() {
        return contractStatusType;
    }

    public void setContractStatusType(String contractStatusType) {
        this.contractStatusType = contractStatusType;
    }

    public String getContractStatusTypeDescription() {
        return contractStatusTypeDescription;
    }

    public void setContractStatusTypeDescription(String contractStatusTypeDescription) {
        this.contractStatusTypeDescription = contractStatusTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractStatusDTO)) {
            return false;
        }

        ContractStatusDTO contractStatusDTO = (ContractStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractStatusDTO{" +
            "id=" + getId() +
            ", contractStatusCode='" + getContractStatusCode() + "'" +
            ", contractStatusType='" + getContractStatusType() + "'" +
            ", contractStatusTypeDescription='" + getContractStatusTypeDescription() + "'" +
            "}";
    }
}
