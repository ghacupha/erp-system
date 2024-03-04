package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CrbAccountHolderType} entity.
 */
public class CrbAccountHolderTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountHolderCategoryTypeCode;

    @NotNull
    private String accountHolderCategoryType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountHolderCategoryTypeCode() {
        return accountHolderCategoryTypeCode;
    }

    public void setAccountHolderCategoryTypeCode(String accountHolderCategoryTypeCode) {
        this.accountHolderCategoryTypeCode = accountHolderCategoryTypeCode;
    }

    public String getAccountHolderCategoryType() {
        return accountHolderCategoryType;
    }

    public void setAccountHolderCategoryType(String accountHolderCategoryType) {
        this.accountHolderCategoryType = accountHolderCategoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAccountHolderTypeDTO)) {
            return false;
        }

        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = (CrbAccountHolderTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbAccountHolderTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAccountHolderTypeDTO{" +
            "id=" + getId() +
            ", accountHolderCategoryTypeCode='" + getAccountHolderCategoryTypeCode() + "'" +
            ", accountHolderCategoryType='" + getAccountHolderCategoryType() + "'" +
            "}";
    }
}
