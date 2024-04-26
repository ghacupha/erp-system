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
