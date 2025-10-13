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

import io.github.erp.domain.enumeration.transactionAccountPostingTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TransactionAccountCategory} entity.
 */
public class TransactionAccountCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private transactionAccountPostingTypes transactionAccountPostingType;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private TransactionAccountLedgerDTO accountLedger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public transactionAccountPostingTypes getTransactionAccountPostingType() {
        return transactionAccountPostingType;
    }

    public void setTransactionAccountPostingType(transactionAccountPostingTypes transactionAccountPostingType) {
        this.transactionAccountPostingType = transactionAccountPostingType;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public TransactionAccountLedgerDTO getAccountLedger() {
        return accountLedger;
    }

    public void setAccountLedger(TransactionAccountLedgerDTO accountLedger) {
        this.accountLedger = accountLedger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountCategoryDTO)) {
            return false;
        }

        TransactionAccountCategoryDTO transactionAccountCategoryDTO = (TransactionAccountCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionAccountCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", transactionAccountPostingType='" + getTransactionAccountPostingType() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", accountLedger=" + getAccountLedger() +
            "}";
    }
}
