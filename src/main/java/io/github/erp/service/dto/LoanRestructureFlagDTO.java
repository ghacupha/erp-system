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
import io.github.erp.domain.enumeration.FlagCodes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LoanRestructureFlag} entity.
 */
public class LoanRestructureFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private FlagCodes loanRestructureFlagCode;

    @NotNull
    private String loanRestructureFlagType;

    @Lob
    private String loanRestructureFlagDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlagCodes getLoanRestructureFlagCode() {
        return loanRestructureFlagCode;
    }

    public void setLoanRestructureFlagCode(FlagCodes loanRestructureFlagCode) {
        this.loanRestructureFlagCode = loanRestructureFlagCode;
    }

    public String getLoanRestructureFlagType() {
        return loanRestructureFlagType;
    }

    public void setLoanRestructureFlagType(String loanRestructureFlagType) {
        this.loanRestructureFlagType = loanRestructureFlagType;
    }

    public String getLoanRestructureFlagDetails() {
        return loanRestructureFlagDetails;
    }

    public void setLoanRestructureFlagDetails(String loanRestructureFlagDetails) {
        this.loanRestructureFlagDetails = loanRestructureFlagDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRestructureFlagDTO)) {
            return false;
        }

        LoanRestructureFlagDTO loanRestructureFlagDTO = (LoanRestructureFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanRestructureFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRestructureFlagDTO{" +
            "id=" + getId() +
            ", loanRestructureFlagCode='" + getLoanRestructureFlagCode() + "'" +
            ", loanRestructureFlagType='" + getLoanRestructureFlagType() + "'" +
            ", loanRestructureFlagDetails='" + getLoanRestructureFlagDetails() + "'" +
            "}";
    }
}
