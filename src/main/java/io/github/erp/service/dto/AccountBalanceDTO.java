package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AccountBalance} entity.
 */
public class AccountBalanceDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String customerId;

    @NotNull
    @Size(min = 12, max = 16)
    @Pattern(regexp = "^\\d{15}$")
    private String accountContractNumber;

    @NotNull
    private BigDecimal accruedInterestBalanceFCY;

    @NotNull
    private BigDecimal accruedInterestBalanceLCY;

    @NotNull
    private BigDecimal accountBalanceFCY;

    @NotNull
    private BigDecimal accountBalanceLCY;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchId;

    private IsoCurrencyCodeDTO currencyCode;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountContractNumber() {
        return accountContractNumber;
    }

    public void setAccountContractNumber(String accountContractNumber) {
        this.accountContractNumber = accountContractNumber;
    }

    public BigDecimal getAccruedInterestBalanceFCY() {
        return accruedInterestBalanceFCY;
    }

    public void setAccruedInterestBalanceFCY(BigDecimal accruedInterestBalanceFCY) {
        this.accruedInterestBalanceFCY = accruedInterestBalanceFCY;
    }

    public BigDecimal getAccruedInterestBalanceLCY() {
        return accruedInterestBalanceLCY;
    }

    public void setAccruedInterestBalanceLCY(BigDecimal accruedInterestBalanceLCY) {
        this.accruedInterestBalanceLCY = accruedInterestBalanceLCY;
    }

    public BigDecimal getAccountBalanceFCY() {
        return accountBalanceFCY;
    }

    public void setAccountBalanceFCY(BigDecimal accountBalanceFCY) {
        this.accountBalanceFCY = accountBalanceFCY;
    }

    public BigDecimal getAccountBalanceLCY() {
        return accountBalanceLCY;
    }

    public void setAccountBalanceLCY(BigDecimal accountBalanceLCY) {
        this.accountBalanceLCY = accountBalanceLCY;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getBranchId() {
        return branchId;
    }

    public void setBranchId(BankBranchCodeDTO branchId) {
        this.branchId = branchId;
    }

    public IsoCurrencyCodeDTO getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCodeDTO currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBalanceDTO)) {
            return false;
        }

        AccountBalanceDTO accountBalanceDTO = (AccountBalanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountBalanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBalanceDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", accountContractNumber='" + getAccountContractNumber() + "'" +
            ", accruedInterestBalanceFCY=" + getAccruedInterestBalanceFCY() +
            ", accruedInterestBalanceLCY=" + getAccruedInterestBalanceLCY() +
            ", accountBalanceFCY=" + getAccountBalanceFCY() +
            ", accountBalanceLCY=" + getAccountBalanceLCY() +
            ", bankCode=" + getBankCode() +
            ", branchId=" + getBranchId() +
            ", currencyCode=" + getCurrencyCode() +
            "}";
    }
}
