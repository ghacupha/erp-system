package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
 * A DTO for the {@link io.github.erp.domain.AgentBankingActivity} entity.
 */
public class AgentBankingActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String agentUniqueId;

    @NotNull
    private String terminalUniqueId;

    @NotNull
    @Min(value = 0)
    private Integer totalCountOfTransactions;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalValueOfTransactionsInLCY;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchCode;

    private BankTransactionTypeDTO transactionType;

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

    public String getAgentUniqueId() {
        return agentUniqueId;
    }

    public void setAgentUniqueId(String agentUniqueId) {
        this.agentUniqueId = agentUniqueId;
    }

    public String getTerminalUniqueId() {
        return terminalUniqueId;
    }

    public void setTerminalUniqueId(String terminalUniqueId) {
        this.terminalUniqueId = terminalUniqueId;
    }

    public Integer getTotalCountOfTransactions() {
        return totalCountOfTransactions;
    }

    public void setTotalCountOfTransactions(Integer totalCountOfTransactions) {
        this.totalCountOfTransactions = totalCountOfTransactions;
    }

    public BigDecimal getTotalValueOfTransactionsInLCY() {
        return totalValueOfTransactionsInLCY;
    }

    public void setTotalValueOfTransactionsInLCY(BigDecimal totalValueOfTransactionsInLCY) {
        this.totalValueOfTransactionsInLCY = totalValueOfTransactionsInLCY;
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

    public BankTransactionTypeDTO getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(BankTransactionTypeDTO transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentBankingActivityDTO)) {
            return false;
        }

        AgentBankingActivityDTO agentBankingActivityDTO = (AgentBankingActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentBankingActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentBankingActivityDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", agentUniqueId='" + getAgentUniqueId() + "'" +
            ", terminalUniqueId='" + getTerminalUniqueId() + "'" +
            ", totalCountOfTransactions=" + getTotalCountOfTransactions() +
            ", totalValueOfTransactionsInLCY=" + getTotalValueOfTransactionsInLCY() +
            ", bankCode=" + getBankCode() +
            ", branchCode=" + getBranchCode() +
            ", transactionType=" + getTransactionType() +
            "}";
    }
}
