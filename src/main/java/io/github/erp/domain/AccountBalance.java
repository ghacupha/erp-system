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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountBalance.
 */
@Entity
@Table(name = "account_balance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountbalance")
public class AccountBalance implements Serializable {

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
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @NotNull
    @Size(min = 12, max = 16)
    @Pattern(regexp = "^\\d{15}$")
    @Column(name = "account_contract_number", length = 16, nullable = false, unique = true)
    private String accountContractNumber;

    @NotNull
    @Column(name = "accrued_interest_balance_fcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal accruedInterestBalanceFCY;

    @NotNull
    @Column(name = "accrued_interest_balance_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal accruedInterestBalanceLCY;

    @NotNull
    @Column(name = "account_balance_fcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal accountBalanceFCY;

    @NotNull
    @Column(name = "account_balance_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal accountBalanceLCY;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchId;

    @ManyToOne(optional = false)
    @NotNull
    private IsoCurrencyCode currencyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountBalance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public AccountBalance reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public AccountBalance customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountContractNumber() {
        return this.accountContractNumber;
    }

    public AccountBalance accountContractNumber(String accountContractNumber) {
        this.setAccountContractNumber(accountContractNumber);
        return this;
    }

    public void setAccountContractNumber(String accountContractNumber) {
        this.accountContractNumber = accountContractNumber;
    }

    public BigDecimal getAccruedInterestBalanceFCY() {
        return this.accruedInterestBalanceFCY;
    }

    public AccountBalance accruedInterestBalanceFCY(BigDecimal accruedInterestBalanceFCY) {
        this.setAccruedInterestBalanceFCY(accruedInterestBalanceFCY);
        return this;
    }

    public void setAccruedInterestBalanceFCY(BigDecimal accruedInterestBalanceFCY) {
        this.accruedInterestBalanceFCY = accruedInterestBalanceFCY;
    }

    public BigDecimal getAccruedInterestBalanceLCY() {
        return this.accruedInterestBalanceLCY;
    }

    public AccountBalance accruedInterestBalanceLCY(BigDecimal accruedInterestBalanceLCY) {
        this.setAccruedInterestBalanceLCY(accruedInterestBalanceLCY);
        return this;
    }

    public void setAccruedInterestBalanceLCY(BigDecimal accruedInterestBalanceLCY) {
        this.accruedInterestBalanceLCY = accruedInterestBalanceLCY;
    }

    public BigDecimal getAccountBalanceFCY() {
        return this.accountBalanceFCY;
    }

    public AccountBalance accountBalanceFCY(BigDecimal accountBalanceFCY) {
        this.setAccountBalanceFCY(accountBalanceFCY);
        return this;
    }

    public void setAccountBalanceFCY(BigDecimal accountBalanceFCY) {
        this.accountBalanceFCY = accountBalanceFCY;
    }

    public BigDecimal getAccountBalanceLCY() {
        return this.accountBalanceLCY;
    }

    public AccountBalance accountBalanceLCY(BigDecimal accountBalanceLCY) {
        this.setAccountBalanceLCY(accountBalanceLCY);
        return this;
    }

    public void setAccountBalanceLCY(BigDecimal accountBalanceLCY) {
        this.accountBalanceLCY = accountBalanceLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public AccountBalance bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchId() {
        return this.branchId;
    }

    public void setBranchId(BankBranchCode bankBranchCode) {
        this.branchId = bankBranchCode;
    }

    public AccountBalance branchId(BankBranchCode bankBranchCode) {
        this.setBranchId(bankBranchCode);
        return this;
    }

    public IsoCurrencyCode getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.currencyCode = isoCurrencyCode;
    }

    public AccountBalance currencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.setCurrencyCode(isoCurrencyCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBalance)) {
            return false;
        }
        return id != null && id.equals(((AccountBalance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBalance{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", accountContractNumber='" + getAccountContractNumber() + "'" +
            ", accruedInterestBalanceFCY=" + getAccruedInterestBalanceFCY() +
            ", accruedInterestBalanceLCY=" + getAccruedInterestBalanceLCY() +
            ", accountBalanceFCY=" + getAccountBalanceFCY() +
            ", accountBalanceLCY=" + getAccountBalanceLCY() +
            "}";
    }
}
