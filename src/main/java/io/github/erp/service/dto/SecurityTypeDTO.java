package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
 * A DTO for the {@link io.github.erp.domain.SecurityType} entity.
 */
public class SecurityTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String securityTypeCode;

    @NotNull
    private String securityType;

    @Lob
    private String securityTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityTypeCode() {
        return securityTypeCode;
    }

    public void setSecurityTypeCode(String securityTypeCode) {
        this.securityTypeCode = securityTypeCode;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSecurityTypeDetails() {
        return securityTypeDetails;
    }

    public void setSecurityTypeDetails(String securityTypeDetails) {
        this.securityTypeDetails = securityTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityTypeDTO)) {
            return false;
        }

        SecurityTypeDTO securityTypeDTO = (SecurityTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, securityTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityTypeDTO{" +
            "id=" + getId() +
            ", securityTypeCode='" + getSecurityTypeCode() + "'" +
            ", securityType='" + getSecurityType() + "'" +
            ", securityTypeDetails='" + getSecurityTypeDetails() + "'" +
            "}";
    }
}
