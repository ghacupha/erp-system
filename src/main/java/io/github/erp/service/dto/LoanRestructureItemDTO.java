package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
 * A DTO for the {@link io.github.erp.domain.LoanRestructureItem} entity.
 */
public class LoanRestructureItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String loanRestructureItemCode;

    @NotNull
    private String loanRestructureItemType;

    @Lob
    private String loanRestructureItemDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanRestructureItemCode() {
        return loanRestructureItemCode;
    }

    public void setLoanRestructureItemCode(String loanRestructureItemCode) {
        this.loanRestructureItemCode = loanRestructureItemCode;
    }

    public String getLoanRestructureItemType() {
        return loanRestructureItemType;
    }

    public void setLoanRestructureItemType(String loanRestructureItemType) {
        this.loanRestructureItemType = loanRestructureItemType;
    }

    public String getLoanRestructureItemDetails() {
        return loanRestructureItemDetails;
    }

    public void setLoanRestructureItemDetails(String loanRestructureItemDetails) {
        this.loanRestructureItemDetails = loanRestructureItemDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRestructureItemDTO)) {
            return false;
        }

        LoanRestructureItemDTO loanRestructureItemDTO = (LoanRestructureItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanRestructureItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRestructureItemDTO{" +
            "id=" + getId() +
            ", loanRestructureItemCode='" + getLoanRestructureItemCode() + "'" +
            ", loanRestructureItemType='" + getLoanRestructureItemType() + "'" +
            ", loanRestructureItemDetails='" + getLoanRestructureItemDetails() + "'" +
            "}";
    }
}
