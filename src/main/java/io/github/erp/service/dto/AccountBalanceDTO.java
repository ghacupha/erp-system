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
