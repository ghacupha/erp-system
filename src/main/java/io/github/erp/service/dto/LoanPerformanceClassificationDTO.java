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
