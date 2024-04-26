package io.github.erp.domain;

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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LoanApplicationStatus.
 */
@Entity
@Table(name = "loan_application_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanapplicationstatus")
public class LoanApplicationStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_application_status_type_code", nullable = false, unique = true)
    private String loanApplicationStatusTypeCode;

    @NotNull
    @Column(name = "loan_application_status_type", nullable = false, unique = true)
    private String loanApplicationStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_application_status_details")
    private String loanApplicationStatusDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanApplicationStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanApplicationStatusTypeCode() {
        return this.loanApplicationStatusTypeCode;
    }

    public LoanApplicationStatus loanApplicationStatusTypeCode(String loanApplicationStatusTypeCode) {
        this.setLoanApplicationStatusTypeCode(loanApplicationStatusTypeCode);
        return this;
    }

    public void setLoanApplicationStatusTypeCode(String loanApplicationStatusTypeCode) {
        this.loanApplicationStatusTypeCode = loanApplicationStatusTypeCode;
    }

    public String getLoanApplicationStatusType() {
        return this.loanApplicationStatusType;
    }

    public LoanApplicationStatus loanApplicationStatusType(String loanApplicationStatusType) {
        this.setLoanApplicationStatusType(loanApplicationStatusType);
        return this;
    }

    public void setLoanApplicationStatusType(String loanApplicationStatusType) {
        this.loanApplicationStatusType = loanApplicationStatusType;
    }

    public String getLoanApplicationStatusDetails() {
        return this.loanApplicationStatusDetails;
    }

    public LoanApplicationStatus loanApplicationStatusDetails(String loanApplicationStatusDetails) {
        this.setLoanApplicationStatusDetails(loanApplicationStatusDetails);
        return this;
    }

    public void setLoanApplicationStatusDetails(String loanApplicationStatusDetails) {
        this.loanApplicationStatusDetails = loanApplicationStatusDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanApplicationStatus)) {
            return false;
        }
        return id != null && id.equals(((LoanApplicationStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanApplicationStatus{" +
            "id=" + getId() +
            ", loanApplicationStatusTypeCode='" + getLoanApplicationStatusTypeCode() + "'" +
            ", loanApplicationStatusType='" + getLoanApplicationStatusType() + "'" +
            ", loanApplicationStatusDetails='" + getLoanApplicationStatusDetails() + "'" +
            "}";
    }
}
