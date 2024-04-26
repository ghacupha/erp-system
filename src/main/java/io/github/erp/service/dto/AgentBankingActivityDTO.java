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
