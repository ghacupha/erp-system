package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.enumeration.AccountStatusTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AccountStatusType} entity.
 */
public class AccountStatusTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountStatusCode;

    @NotNull
    private AccountStatusTypes accountStatusType;

    @Lob
    private String accountStatusDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public AccountStatusTypes getAccountStatusType() {
        return accountStatusType;
    }

    public void setAccountStatusType(AccountStatusTypes accountStatusType) {
        this.accountStatusType = accountStatusType;
    }

    public String getAccountStatusDescription() {
        return accountStatusDescription;
    }

    public void setAccountStatusDescription(String accountStatusDescription) {
        this.accountStatusDescription = accountStatusDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountStatusTypeDTO)) {
            return false;
        }

        AccountStatusTypeDTO accountStatusTypeDTO = (AccountStatusTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountStatusTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountStatusTypeDTO{" +
            "id=" + getId() +
            ", accountStatusCode='" + getAccountStatusCode() + "'" +
            ", accountStatusType='" + getAccountStatusType() + "'" +
            ", accountStatusDescription='" + getAccountStatusDescription() + "'" +
            "}";
    }
}
