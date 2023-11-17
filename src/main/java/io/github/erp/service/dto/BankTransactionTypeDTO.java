package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.BankTransactionType} entity.
 */
public class BankTransactionTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String transactionTypeCode;

    @NotNull
    private String transactionTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(String transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public String getTransactionTypeDetails() {
        return transactionTypeDetails;
    }

    public void setTransactionTypeDetails(String transactionTypeDetails) {
        this.transactionTypeDetails = transactionTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankTransactionTypeDTO)) {
            return false;
        }

        BankTransactionTypeDTO bankTransactionTypeDTO = (BankTransactionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankTransactionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransactionTypeDTO{" +
            "id=" + getId() +
            ", transactionTypeCode='" + getTransactionTypeCode() + "'" +
            ", transactionTypeDetails='" + getTransactionTypeDetails() + "'" +
            "}";
    }
}
