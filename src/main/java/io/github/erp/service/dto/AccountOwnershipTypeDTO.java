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
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AccountOwnershipType} entity.
 */
public class AccountOwnershipTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountOwnershipTypeCode;

    @NotNull
    private String accountOwnershipType;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountOwnershipTypeCode() {
        return accountOwnershipTypeCode;
    }

    public void setAccountOwnershipTypeCode(String accountOwnershipTypeCode) {
        this.accountOwnershipTypeCode = accountOwnershipTypeCode;
    }

    public String getAccountOwnershipType() {
        return accountOwnershipType;
    }

    public void setAccountOwnershipType(String accountOwnershipType) {
        this.accountOwnershipType = accountOwnershipType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountOwnershipTypeDTO)) {
            return false;
        }

        AccountOwnershipTypeDTO accountOwnershipTypeDTO = (AccountOwnershipTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountOwnershipTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountOwnershipTypeDTO{" +
            "id=" + getId() +
            ", accountOwnershipTypeCode='" + getAccountOwnershipTypeCode() + "'" +
            ", accountOwnershipType='" + getAccountOwnershipType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
