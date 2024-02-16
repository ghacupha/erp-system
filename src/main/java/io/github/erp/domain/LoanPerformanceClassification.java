package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LoanPerformanceClassification.
 */
@Entity
@Table(name = "loan_performance_classification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanperformanceclassification")
public class LoanPerformanceClassification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_performance_classification_code", nullable = false, unique = true)
    private String loanPerformanceClassificationCode;

    @NotNull
    @Column(name = "loan_performance_classification_type", nullable = false)
    private String loanPerformanceClassificationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "commercial_bank_description")
    private String commercialBankDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "microfinance_description")
    private String microfinanceDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanPerformanceClassification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanPerformanceClassificationCode() {
        return this.loanPerformanceClassificationCode;
    }

    public LoanPerformanceClassification loanPerformanceClassificationCode(String loanPerformanceClassificationCode) {
        this.setLoanPerformanceClassificationCode(loanPerformanceClassificationCode);
        return this;
    }

    public void setLoanPerformanceClassificationCode(String loanPerformanceClassificationCode) {
        this.loanPerformanceClassificationCode = loanPerformanceClassificationCode;
    }

    public String getLoanPerformanceClassificationType() {
        return this.loanPerformanceClassificationType;
    }

    public LoanPerformanceClassification loanPerformanceClassificationType(String loanPerformanceClassificationType) {
        this.setLoanPerformanceClassificationType(loanPerformanceClassificationType);
        return this;
    }

    public void setLoanPerformanceClassificationType(String loanPerformanceClassificationType) {
        this.loanPerformanceClassificationType = loanPerformanceClassificationType;
    }

    public String getCommercialBankDescription() {
        return this.commercialBankDescription;
    }

    public LoanPerformanceClassification commercialBankDescription(String commercialBankDescription) {
        this.setCommercialBankDescription(commercialBankDescription);
        return this;
    }

    public void setCommercialBankDescription(String commercialBankDescription) {
        this.commercialBankDescription = commercialBankDescription;
    }

    public String getMicrofinanceDescription() {
        return this.microfinanceDescription;
    }

    public LoanPerformanceClassification microfinanceDescription(String microfinanceDescription) {
        this.setMicrofinanceDescription(microfinanceDescription);
        return this;
    }

    public void setMicrofinanceDescription(String microfinanceDescription) {
        this.microfinanceDescription = microfinanceDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanPerformanceClassification)) {
            return false;
        }
        return id != null && id.equals(((LoanPerformanceClassification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanPerformanceClassification{" +
            "id=" + getId() +
            ", loanPerformanceClassificationCode='" + getLoanPerformanceClassificationCode() + "'" +
            ", loanPerformanceClassificationType='" + getLoanPerformanceClassificationType() + "'" +
            ", commercialBankDescription='" + getCommercialBankDescription() + "'" +
            ", microfinanceDescription='" + getMicrofinanceDescription() + "'" +
            "}";
    }
}
