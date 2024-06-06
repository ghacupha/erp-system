package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouDepreciationPostingReportItem.
 */
@Entity
@Table(name = "rou_depreciation_posting_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roudepreciationpostingreportitem")
public class RouDepreciationPostingReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "lease_contract_number")
    private String leaseContractNumber;

    @Column(name = "lease_description")
    private String leaseDescription;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "account_for_credit")
    private String accountForCredit;

    @Column(name = "account_for_debit")
    private String accountForDebit;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouDepreciationPostingReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return this.leaseContractNumber;
    }

    public RouDepreciationPostingReportItem leaseContractNumber(String leaseContractNumber) {
        this.setLeaseContractNumber(leaseContractNumber);
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getLeaseDescription() {
        return this.leaseDescription;
    }

    public RouDepreciationPostingReportItem leaseDescription(String leaseDescription) {
        this.setLeaseDescription(leaseDescription);
        return this;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getFiscalMonthCode() {
        return this.fiscalMonthCode;
    }

    public RouDepreciationPostingReportItem fiscalMonthCode(String fiscalMonthCode) {
        this.setFiscalMonthCode(fiscalMonthCode);
        return this;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public String getAccountForCredit() {
        return this.accountForCredit;
    }

    public RouDepreciationPostingReportItem accountForCredit(String accountForCredit) {
        this.setAccountForCredit(accountForCredit);
        return this;
    }

    public void setAccountForCredit(String accountForCredit) {
        this.accountForCredit = accountForCredit;
    }

    public String getAccountForDebit() {
        return this.accountForDebit;
    }

    public RouDepreciationPostingReportItem accountForDebit(String accountForDebit) {
        this.setAccountForDebit(accountForDebit);
        return this;
    }

    public void setAccountForDebit(String accountForDebit) {
        this.accountForDebit = accountForDebit;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public RouDepreciationPostingReportItem depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationPostingReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouDepreciationPostingReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationPostingReportItem{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", leaseDescription='" + getLeaseDescription() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", accountForCredit='" + getAccountForCredit() + "'" +
            ", accountForDebit='" + getAccountForDebit() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            "}";
    }
}
