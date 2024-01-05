package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import io.github.erp.domain.enumeration.LoanAccountMutationTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LoanAccountCategory} entity.
 */
public class LoanAccountCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String loanAccountMutationCode;

    @NotNull
    private LoanAccountMutationTypes loanAccountMutationType;

    @NotNull
    private String loanAccountMutationDetails;

    @Lob
    private String loanAccountMutationDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanAccountMutationCode() {
        return loanAccountMutationCode;
    }

    public void setLoanAccountMutationCode(String loanAccountMutationCode) {
        this.loanAccountMutationCode = loanAccountMutationCode;
    }

    public LoanAccountMutationTypes getLoanAccountMutationType() {
        return loanAccountMutationType;
    }

    public void setLoanAccountMutationType(LoanAccountMutationTypes loanAccountMutationType) {
        this.loanAccountMutationType = loanAccountMutationType;
    }

    public String getLoanAccountMutationDetails() {
        return loanAccountMutationDetails;
    }

    public void setLoanAccountMutationDetails(String loanAccountMutationDetails) {
        this.loanAccountMutationDetails = loanAccountMutationDetails;
    }

    public String getLoanAccountMutationDescription() {
        return loanAccountMutationDescription;
    }

    public void setLoanAccountMutationDescription(String loanAccountMutationDescription) {
        this.loanAccountMutationDescription = loanAccountMutationDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanAccountCategoryDTO)) {
            return false;
        }

        LoanAccountCategoryDTO loanAccountCategoryDTO = (LoanAccountCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanAccountCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanAccountCategoryDTO{" +
            "id=" + getId() +
            ", loanAccountMutationCode='" + getLoanAccountMutationCode() + "'" +
            ", loanAccountMutationType='" + getLoanAccountMutationType() + "'" +
            ", loanAccountMutationDetails='" + getLoanAccountMutationDetails() + "'" +
            ", loanAccountMutationDescription='" + getLoanAccountMutationDescription() + "'" +
            "}";
    }
}
