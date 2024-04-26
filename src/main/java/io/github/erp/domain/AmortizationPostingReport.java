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
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AmortizationPostingReport.
 */
@Entity
@Table(name = "amortization_posting_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "amortizationpostingreport")
public class AmortizationPostingReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "catalogue_number")
    private String catalogueNumber;

    @Column(name = "debit_account")
    private String debitAccount;

    @Column(name = "credit_account")
    private String creditAccount;

    @Column(name = "description")
    private String description;

    @Column(name = "amortization_amount", precision = 21, scale = 2)
    private BigDecimal amortizationAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AmortizationPostingReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public AmortizationPostingReport catalogueNumber(String catalogueNumber) {
        this.setCatalogueNumber(catalogueNumber);
        return this;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getDebitAccount() {
        return this.debitAccount;
    }

    public AmortizationPostingReport debitAccount(String debitAccount) {
        this.setDebitAccount(debitAccount);
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return this.creditAccount;
    }

    public AmortizationPostingReport creditAccount(String creditAccount) {
        this.setCreditAccount(creditAccount);
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDescription() {
        return this.description;
    }

    public AmortizationPostingReport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmortizationAmount() {
        return this.amortizationAmount;
    }

    public AmortizationPostingReport amortizationAmount(BigDecimal amortizationAmount) {
        this.setAmortizationAmount(amortizationAmount);
        return this;
    }

    public void setAmortizationAmount(BigDecimal amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmortizationPostingReport)) {
            return false;
        }
        return id != null && id.equals(((AmortizationPostingReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationPostingReport{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", description='" + getDescription() + "'" +
            ", amortizationAmount=" + getAmortizationAmount() +
            "}";
    }
}
