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
import io.github.erp.domain.enumeration.LoanAccountMutationTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LoanAccountCategory.
 */
@Entity
@Table(name = "loan_account_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loanaccountcategory")
public class LoanAccountCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loan_account_mutation_code", nullable = false, unique = true)
    private String loanAccountMutationCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_account_mutation_type", nullable = false)
    private LoanAccountMutationTypes loanAccountMutationType;

    @NotNull
    @Column(name = "loan_account_mutation_details", nullable = false, unique = true)
    private String loanAccountMutationDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "loan_account_mutation_description")
    private String loanAccountMutationDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanAccountCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanAccountMutationCode() {
        return this.loanAccountMutationCode;
    }

    public LoanAccountCategory loanAccountMutationCode(String loanAccountMutationCode) {
        this.setLoanAccountMutationCode(loanAccountMutationCode);
        return this;
    }

    public void setLoanAccountMutationCode(String loanAccountMutationCode) {
        this.loanAccountMutationCode = loanAccountMutationCode;
    }

    public LoanAccountMutationTypes getLoanAccountMutationType() {
        return this.loanAccountMutationType;
    }

    public LoanAccountCategory loanAccountMutationType(LoanAccountMutationTypes loanAccountMutationType) {
        this.setLoanAccountMutationType(loanAccountMutationType);
        return this;
    }

    public void setLoanAccountMutationType(LoanAccountMutationTypes loanAccountMutationType) {
        this.loanAccountMutationType = loanAccountMutationType;
    }

    public String getLoanAccountMutationDetails() {
        return this.loanAccountMutationDetails;
    }

    public LoanAccountCategory loanAccountMutationDetails(String loanAccountMutationDetails) {
        this.setLoanAccountMutationDetails(loanAccountMutationDetails);
        return this;
    }

    public void setLoanAccountMutationDetails(String loanAccountMutationDetails) {
        this.loanAccountMutationDetails = loanAccountMutationDetails;
    }

    public String getLoanAccountMutationDescription() {
        return this.loanAccountMutationDescription;
    }

    public LoanAccountCategory loanAccountMutationDescription(String loanAccountMutationDescription) {
        this.setLoanAccountMutationDescription(loanAccountMutationDescription);
        return this;
    }

    public void setLoanAccountMutationDescription(String loanAccountMutationDescription) {
        this.loanAccountMutationDescription = loanAccountMutationDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanAccountCategory)) {
            return false;
        }
        return id != null && id.equals(((LoanAccountCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanAccountCategory{" +
            "id=" + getId() +
            ", loanAccountMutationCode='" + getLoanAccountMutationCode() + "'" +
            ", loanAccountMutationType='" + getLoanAccountMutationType() + "'" +
            ", loanAccountMutationDetails='" + getLoanAccountMutationDetails() + "'" +
            ", loanAccountMutationDescription='" + getLoanAccountMutationDescription() + "'" +
            "}";
    }
}
