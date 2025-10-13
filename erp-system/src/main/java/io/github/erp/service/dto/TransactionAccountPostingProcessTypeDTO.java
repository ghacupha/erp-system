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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TransactionAccountPostingProcessType} entity.
 */
public class TransactionAccountPostingProcessTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private TransactionAccountCategoryDTO debitAccountType;

    private TransactionAccountCategoryDTO creditAccountType;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingProcessTypeDTO)) {
            return false;
        }

        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = (TransactionAccountPostingProcessTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionAccountPostingProcessTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingProcessTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", debitAccountType=" + getDebitAccountType() +
            ", creditAccountType=" + getCreditAccountType() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
