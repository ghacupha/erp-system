package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AccountAttribute} entity.
 */
public class AccountAttributeDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String customerNumber;

    @NotNull
    private String accountContractNumber;

    @NotNull
    private String accountName;

    private LocalDate accountOpeningDate;

    private LocalDate accountClosingDate;

    @NotNull
    private BigDecimal debitInterestRate;

    @NotNull
    private BigDecimal creditInterestRate;

    @NotNull
    private BigDecimal sanctionedAccountLimitFcy;

    @NotNull
    private BigDecimal sanctionedAccountLimitLcy;

    private LocalDate accountStatusChangeDate;

    private LocalDate expiryDate;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchCode;

    private AccountOwnershipTypeDTO accountOwnershipType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getAccountContractNumber() {
        return accountContractNumber;
    }

    public void setAccountContractNumber(String accountContractNumber) {
        this.accountContractNumber = accountContractNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LocalDate getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(LocalDate accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public LocalDate getAccountClosingDate() {
        return accountClosingDate;
    }

    public void setAccountClosingDate(LocalDate accountClosingDate) {
        this.accountClosingDate = accountClosingDate;
    }

    public BigDecimal getDebitInterestRate() {
        return debitInterestRate;
    }

    public void setDebitInterestRate(BigDecimal debitInterestRate) {
        this.debitInterestRate = debitInterestRate;
    }

    public BigDecimal getCreditInterestRate() {
        return creditInterestRate;
    }

    public void setCreditInterestRate(BigDecimal creditInterestRate) {
        this.creditInterestRate = creditInterestRate;
    }

    public BigDecimal getSanctionedAccountLimitFcy() {
        return sanctionedAccountLimitFcy;
    }

    public void setSanctionedAccountLimitFcy(BigDecimal sanctionedAccountLimitFcy) {
        this.sanctionedAccountLimitFcy = sanctionedAccountLimitFcy;
    }

    public BigDecimal getSanctionedAccountLimitLcy() {
        return sanctionedAccountLimitLcy;
    }

    public void setSanctionedAccountLimitLcy(BigDecimal sanctionedAccountLimitLcy) {
        this.sanctionedAccountLimitLcy = sanctionedAccountLimitLcy;
    }

    public LocalDate getAccountStatusChangeDate() {
        return accountStatusChangeDate;
    }

    public void setAccountStatusChangeDate(LocalDate accountStatusChangeDate) {
        this.accountStatusChangeDate = accountStatusChangeDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(BankBranchCodeDTO branchCode) {
        this.branchCode = branchCode;
    }

    public AccountOwnershipTypeDTO getAccountOwnershipType() {
        return accountOwnershipType;
    }

    public void setAccountOwnershipType(AccountOwnershipTypeDTO accountOwnershipType) {
        this.accountOwnershipType = accountOwnershipType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountAttributeDTO)) {
            return false;
        }

        AccountAttributeDTO accountAttributeDTO = (AccountAttributeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountAttributeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttributeDTO{" +
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
            ", bankCode=" + getBankCode() +
            ", branchCode=" + getBranchCode() +
            ", accountOwnershipType=" + getAccountOwnershipType() +
            "}";
    }
}
