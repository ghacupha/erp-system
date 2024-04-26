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
