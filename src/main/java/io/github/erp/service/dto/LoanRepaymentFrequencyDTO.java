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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LoanRepaymentFrequency} entity.
 */
public class LoanRepaymentFrequencyDTO implements Serializable {

    private Long id;

    @NotNull
    private String frequencyTypeCode;

    @NotNull
    private String frequencyType;

    @Lob
    private String frequencyTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrequencyTypeCode() {
        return frequencyTypeCode;
    }

    public void setFrequencyTypeCode(String frequencyTypeCode) {
        this.frequencyTypeCode = frequencyTypeCode;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getFrequencyTypeDetails() {
        return frequencyTypeDetails;
    }

    public void setFrequencyTypeDetails(String frequencyTypeDetails) {
        this.frequencyTypeDetails = frequencyTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanRepaymentFrequencyDTO)) {
            return false;
        }

        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = (LoanRepaymentFrequencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanRepaymentFrequencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanRepaymentFrequencyDTO{" +
            "id=" + getId() +
            ", frequencyTypeCode='" + getFrequencyTypeCode() + "'" +
            ", frequencyType='" + getFrequencyType() + "'" +
            ", frequencyTypeDetails='" + getFrequencyTypeDetails() + "'" +
            "}";
    }
}
