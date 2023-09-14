package io.github.erp.service.dto;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
