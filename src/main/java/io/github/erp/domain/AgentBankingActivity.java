package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AgentBankingActivity.
 */
@Entity
@Table(name = "agent_banking_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agentbankingactivity")
public class AgentBankingActivity implements Serializable {

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
    @Column(name = "agent_unique_id", nullable = false)
    private String agentUniqueId;

    @NotNull
    @Column(name = "terminal_unique_id", nullable = false)
    private String terminalUniqueId;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_count_of_transactions", nullable = false)
    private Integer totalCountOfTransactions;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_value_of_transactions_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalValueOfTransactionsInLCY;

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
    private BankTransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgentBankingActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public AgentBankingActivity reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getAgentUniqueId() {
        return this.agentUniqueId;
    }

    public AgentBankingActivity agentUniqueId(String agentUniqueId) {
        this.setAgentUniqueId(agentUniqueId);
        return this;
    }

    public void setAgentUniqueId(String agentUniqueId) {
        this.agentUniqueId = agentUniqueId;
    }

    public String getTerminalUniqueId() {
        return this.terminalUniqueId;
    }

    public AgentBankingActivity terminalUniqueId(String terminalUniqueId) {
        this.setTerminalUniqueId(terminalUniqueId);
        return this;
    }

    public void setTerminalUniqueId(String terminalUniqueId) {
        this.terminalUniqueId = terminalUniqueId;
    }

    public Integer getTotalCountOfTransactions() {
        return this.totalCountOfTransactions;
    }

    public AgentBankingActivity totalCountOfTransactions(Integer totalCountOfTransactions) {
        this.setTotalCountOfTransactions(totalCountOfTransactions);
        return this;
    }

    public void setTotalCountOfTransactions(Integer totalCountOfTransactions) {
        this.totalCountOfTransactions = totalCountOfTransactions;
    }

    public BigDecimal getTotalValueOfTransactionsInLCY() {
        return this.totalValueOfTransactionsInLCY;
    }

    public AgentBankingActivity totalValueOfTransactionsInLCY(BigDecimal totalValueOfTransactionsInLCY) {
        this.setTotalValueOfTransactionsInLCY(totalValueOfTransactionsInLCY);
        return this;
    }

    public void setTotalValueOfTransactionsInLCY(BigDecimal totalValueOfTransactionsInLCY) {
        this.totalValueOfTransactionsInLCY = totalValueOfTransactionsInLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public AgentBankingActivity bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(BankBranchCode bankBranchCode) {
        this.branchCode = bankBranchCode;
    }

    public AgentBankingActivity branchCode(BankBranchCode bankBranchCode) {
        this.setBranchCode(bankBranchCode);
        return this;
    }

    public BankTransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(BankTransactionType bankTransactionType) {
        this.transactionType = bankTransactionType;
    }

    public AgentBankingActivity transactionType(BankTransactionType bankTransactionType) {
        this.setTransactionType(bankTransactionType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentBankingActivity)) {
            return false;
        }
        return id != null && id.equals(((AgentBankingActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentBankingActivity{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", agentUniqueId='" + getAgentUniqueId() + "'" +
            ", terminalUniqueId='" + getTerminalUniqueId() + "'" +
            ", totalCountOfTransactions=" + getTotalCountOfTransactions() +
            ", totalValueOfTransactionsInLCY=" + getTotalValueOfTransactionsInLCY() +
            "}";
    }
}
