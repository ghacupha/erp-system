package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountAttribute.
 */
@Entity
@Table(name = "account_attribute")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountattribute")
public class AccountAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Column(name = "customer_number", nullable = false)
    private String customerNumber;

    @NotNull
    @Column(name = "account_contract_number", nullable = false, unique = true)
    private String accountContractNumber;

    @NotNull
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_opening_date")
    private LocalDate accountOpeningDate;

    @Column(name = "account_closing_date")
    private LocalDate accountClosingDate;

    @NotNull
    @Column(name = "debit_interest_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal debitInterestRate;

    @NotNull
    @Column(name = "credit_interest_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal creditInterestRate;

    @NotNull
    @Column(name = "sanctioned_account_limit_fcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal sanctionedAccountLimitFcy;

    @NotNull
    @Column(name = "sanctioned_account_limit_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal sanctionedAccountLimitLcy;

    @Column(name = "account_status_change_date")
    private LocalDate accountStatusChangeDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchCode;

    @ManyToOne(optional = false)
    @NotNull
    private AccountOwnershipType accountOwnershipType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountAttribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public AccountAttribute reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCustomerNumber() {
        return this.customerNumber;
    }

    public AccountAttribute customerNumber(String customerNumber) {
        this.setCustomerNumber(customerNumber);
        return this;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getAccountContractNumber() {
        return this.accountContractNumber;
    }

    public AccountAttribute accountContractNumber(String accountContractNumber) {
        this.setAccountContractNumber(accountContractNumber);
        return this;
    }

    public void setAccountContractNumber(String accountContractNumber) {
        this.accountContractNumber = accountContractNumber;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public AccountAttribute accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LocalDate getAccountOpeningDate() {
        return this.accountOpeningDate;
    }

    public AccountAttribute accountOpeningDate(LocalDate accountOpeningDate) {
        this.setAccountOpeningDate(accountOpeningDate);
        return this;
    }

    public void setAccountOpeningDate(LocalDate accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public LocalDate getAccountClosingDate() {
        return this.accountClosingDate;
    }

    public AccountAttribute accountClosingDate(LocalDate accountClosingDate) {
        this.setAccountClosingDate(accountClosingDate);
        return this;
    }

    public void setAccountClosingDate(LocalDate accountClosingDate) {
        this.accountClosingDate = accountClosingDate;
    }

    public BigDecimal getDebitInterestRate() {
        return this.debitInterestRate;
    }

    public AccountAttribute debitInterestRate(BigDecimal debitInterestRate) {
        this.setDebitInterestRate(debitInterestRate);
        return this;
    }

    public void setDebitInterestRate(BigDecimal debitInterestRate) {
        this.debitInterestRate = debitInterestRate;
    }

    public BigDecimal getCreditInterestRate() {
        return this.creditInterestRate;
    }

    public AccountAttribute creditInterestRate(BigDecimal creditInterestRate) {
        this.setCreditInterestRate(creditInterestRate);
        return this;
    }

    public void setCreditInterestRate(BigDecimal creditInterestRate) {
        this.creditInterestRate = creditInterestRate;
    }

    public BigDecimal getSanctionedAccountLimitFcy() {
        return this.sanctionedAccountLimitFcy;
    }

    public AccountAttribute sanctionedAccountLimitFcy(BigDecimal sanctionedAccountLimitFcy) {
        this.setSanctionedAccountLimitFcy(sanctionedAccountLimitFcy);
        return this;
    }

    public void setSanctionedAccountLimitFcy(BigDecimal sanctionedAccountLimitFcy) {
        this.sanctionedAccountLimitFcy = sanctionedAccountLimitFcy;
    }

    public BigDecimal getSanctionedAccountLimitLcy() {
        return this.sanctionedAccountLimitLcy;
    }

    public AccountAttribute sanctionedAccountLimitLcy(BigDecimal sanctionedAccountLimitLcy) {
        this.setSanctionedAccountLimitLcy(sanctionedAccountLimitLcy);
        return this;
    }

    public void setSanctionedAccountLimitLcy(BigDecimal sanctionedAccountLimitLcy) {
        this.sanctionedAccountLimitLcy = sanctionedAccountLimitLcy;
    }

    public LocalDate getAccountStatusChangeDate() {
        return this.accountStatusChangeDate;
    }

    public AccountAttribute accountStatusChangeDate(LocalDate accountStatusChangeDate) {
        this.setAccountStatusChangeDate(accountStatusChangeDate);
        return this;
    }

    public void setAccountStatusChangeDate(LocalDate accountStatusChangeDate) {
        this.accountStatusChangeDate = accountStatusChangeDate;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public AccountAttribute expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public AccountAttribute bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(BankBranchCode bankBranchCode) {
        this.branchCode = bankBranchCode;
    }

    public AccountAttribute branchCode(BankBranchCode bankBranchCode) {
        this.setBranchCode(bankBranchCode);
        return this;
    }

    public AccountOwnershipType getAccountOwnershipType() {
        return this.accountOwnershipType;
    }

    public void setAccountOwnershipType(AccountOwnershipType accountOwnershipType) {
        this.accountOwnershipType = accountOwnershipType;
    }

    public AccountAttribute accountOwnershipType(AccountOwnershipType accountOwnershipType) {
        this.setAccountOwnershipType(accountOwnershipType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountAttribute)) {
            return false;
        }
        return id != null && id.equals(((AccountAttribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttribute{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", customerNumber='" + getCustomerNumber() + "'" +
            ", accountContractNumber='" + getAccountContractNumber() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", accountOpeningDate='" + getAccountOpeningDate() + "'" +
            ", accountClosingDate='" + getAccountClosingDate() + "'" +
            ", debitInterestRate=" + getDebitInterestRate() +
            ", creditInterestRate=" + getCreditInterestRate() +
            ", sanctionedAccountLimitFcy=" + getSanctionedAccountLimitFcy() +
            ", sanctionedAccountLimitLcy=" + getSanctionedAccountLimitLcy() +
            ", accountStatusChangeDate='" + getAccountStatusChangeDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            "}";
    }
}
