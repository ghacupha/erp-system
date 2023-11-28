package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LoanPerformanceClassification} entity.
 */
public class LoanPerformanceClassificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String loanPerformanceClassificationCode;

    @NotNull
    private String loanPerformanceClassificationType;

    @Lob
    private String commercialBankDescription;

    @Lob
    private String microfinanceDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanPerformanceClassificationCode() {
        return loanPerformanceClassificationCode;
    }

    public void setLoanPerformanceClassificationCode(String loanPerformanceClassificationCode) {
        this.loanPerformanceClassificationCode = loanPerformanceClassificationCode;
    }

    public String getLoanPerformanceClassificationType() {
        return loanPerformanceClassificationType;
    }

    public void setLoanPerformanceClassificationType(String loanPerformanceClassificationType) {
        this.loanPerformanceClassificationType = loanPerformanceClassificationType;
    }

    public String getCommercialBankDescription() {
        return commercialBankDescription;
    }

    public void setCommercialBankDescription(String commercialBankDescription) {
        this.commercialBankDescription = commercialBankDescription;
    }

    public String getMicrofinanceDescription() {
        return microfinanceDescription;
    }

    public void setMicrofinanceDescription(String microfinanceDescription) {
        this.microfinanceDescription = microfinanceDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanPerformanceClassificationDTO)) {
            return false;
        }

        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = (LoanPerformanceClassificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanPerformanceClassificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanPerformanceClassificationDTO{" +
            "id=" + getId() +
            ", loanPerformanceClassificationCode='" + getLoanPerformanceClassificationCode() + "'" +
            ", loanPerformanceClassificationType='" + getLoanPerformanceClassificationType() + "'" +
            ", commercialBankDescription='" + getCommercialBankDescription() + "'" +
            ", microfinanceDescription='" + getMicrofinanceDescription() + "'" +
            "}";
    }
}
