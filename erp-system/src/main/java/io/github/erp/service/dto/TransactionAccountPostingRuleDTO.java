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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TransactionAccountPostingRule} entity.
 */
public class TransactionAccountPostingRuleDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private UUID identifier;

    private TransactionAccountCategoryDTO debitAccountType;

    private TransactionAccountCategoryDTO creditAccountType;

    private PlaceholderDTO transactionContext;

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

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public TransactionAccountCategoryDTO getDebitAccountType() {
        return debitAccountType;
    }

    public void setDebitAccountType(TransactionAccountCategoryDTO debitAccountType) {
        this.debitAccountType = debitAccountType;
    }

    public TransactionAccountCategoryDTO getCreditAccountType() {
        return creditAccountType;
    }

    public void setCreditAccountType(TransactionAccountCategoryDTO creditAccountType) {
        this.creditAccountType = creditAccountType;
    }

    public PlaceholderDTO getTransactionContext() {
        return transactionContext;
    }

    public void setTransactionContext(PlaceholderDTO transactionContext) {
        this.transactionContext = transactionContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingRuleDTO)) {
            return false;
        }

        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = (TransactionAccountPostingRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionAccountPostingRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", debitAccountType=" + getDebitAccountType() +
            ", creditAccountType=" + getCreditAccountType() +
            ", transactionContext=" + getTransactionContext() +
            "}";
    }
}
