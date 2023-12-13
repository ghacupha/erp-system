package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
 * A DTO for the {@link io.github.erp.domain.SecurityTenure} entity.
 */
public class SecurityTenureDTO implements Serializable {

    private Long id;

    @NotNull
    private String securityTenureCode;

    @NotNull
    private String securityTenureType;

    @Lob
    private String securityTenureDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityTenureCode() {
        return securityTenureCode;
    }

    public void setSecurityTenureCode(String securityTenureCode) {
        this.securityTenureCode = securityTenureCode;
    }

    public String getSecurityTenureType() {
        return securityTenureType;
    }

    public void setSecurityTenureType(String securityTenureType) {
        this.securityTenureType = securityTenureType;
    }

    public String getSecurityTenureDetails() {
        return securityTenureDetails;
    }

    public void setSecurityTenureDetails(String securityTenureDetails) {
        this.securityTenureDetails = securityTenureDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityTenureDTO)) {
            return false;
        }

        SecurityTenureDTO securityTenureDTO = (SecurityTenureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, securityTenureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityTenureDTO{" +
            "id=" + getId() +
            ", securityTenureCode='" + getSecurityTenureCode() + "'" +
            ", securityTenureType='" + getSecurityTenureType() + "'" +
            ", securityTenureDetails='" + getSecurityTenureDetails() + "'" +
            "}";
    }
}
