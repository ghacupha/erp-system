package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AgentBankingActivity} entity. This class is used
 * in {@link io.github.erp.web.rest.AgentBankingActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agent-banking-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgentBankingActivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter agentUniqueId;

    private StringFilter terminalUniqueId;

    private IntegerFilter totalCountOfTransactions;

    private BigDecimalFilter totalValueOfTransactionsInLCY;

    private LongFilter bankCodeId;

    private LongFilter branchCodeId;

    private LongFilter transactionTypeId;

    private Boolean distinct;

    public AgentBankingActivityCriteria() {}

    public AgentBankingActivityCriteria(AgentBankingActivityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.agentUniqueId = other.agentUniqueId == null ? null : other.agentUniqueId.copy();
        this.terminalUniqueId = other.terminalUniqueId == null ? null : other.terminalUniqueId.copy();
        this.totalCountOfTransactions = other.totalCountOfTransactions == null ? null : other.totalCountOfTransactions.copy();
        this.totalValueOfTransactionsInLCY =
            other.totalValueOfTransactionsInLCY == null ? null : other.totalValueOfTransactionsInLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchCodeId = other.branchCodeId == null ? null : other.branchCodeId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AgentBankingActivityCriteria copy() {
        return new AgentBankingActivityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public StringFilter getAgentUniqueId() {
        return agentUniqueId;
    }

    public StringFilter agentUniqueId() {
        if (agentUniqueId == null) {
            agentUniqueId = new StringFilter();
        }
        return agentUniqueId;
    }

    public void setAgentUniqueId(StringFilter agentUniqueId) {
        this.agentUniqueId = agentUniqueId;
    }

    public StringFilter getTerminalUniqueId() {
        return terminalUniqueId;
    }

    public StringFilter terminalUniqueId() {
        if (terminalUniqueId == null) {
            terminalUniqueId = new StringFilter();
        }
        return terminalUniqueId;
    }

    public void setTerminalUniqueId(StringFilter terminalUniqueId) {
        this.terminalUniqueId = terminalUniqueId;
    }

    public IntegerFilter getTotalCountOfTransactions() {
        return totalCountOfTransactions;
    }

    public IntegerFilter totalCountOfTransactions() {
        if (totalCountOfTransactions == null) {
            totalCountOfTransactions = new IntegerFilter();
        }
        return totalCountOfTransactions;
    }

    public void setTotalCountOfTransactions(IntegerFilter totalCountOfTransactions) {
        this.totalCountOfTransactions = totalCountOfTransactions;
    }

    public BigDecimalFilter getTotalValueOfTransactionsInLCY() {
        return totalValueOfTransactionsInLCY;
    }

    public BigDecimalFilter totalValueOfTransactionsInLCY() {
        if (totalValueOfTransactionsInLCY == null) {
            totalValueOfTransactionsInLCY = new BigDecimalFilter();
        }
        return totalValueOfTransactionsInLCY;
    }

    public void setTotalValueOfTransactionsInLCY(BigDecimalFilter totalValueOfTransactionsInLCY) {
        this.totalValueOfTransactionsInLCY = totalValueOfTransactionsInLCY;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getBranchCodeId() {
        return branchCodeId;
    }

    public LongFilter branchCodeId() {
        if (branchCodeId == null) {
            branchCodeId = new LongFilter();
        }
        return branchCodeId;
    }

    public void setBranchCodeId(LongFilter branchCodeId) {
        this.branchCodeId = branchCodeId;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public LongFilter transactionTypeId() {
        if (transactionTypeId == null) {
            transactionTypeId = new LongFilter();
        }
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AgentBankingActivityCriteria that = (AgentBankingActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(agentUniqueId, that.agentUniqueId) &&
            Objects.equals(terminalUniqueId, that.terminalUniqueId) &&
            Objects.equals(totalCountOfTransactions, that.totalCountOfTransactions) &&
            Objects.equals(totalValueOfTransactionsInLCY, that.totalValueOfTransactionsInLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchCodeId, that.branchCodeId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            agentUniqueId,
            terminalUniqueId,
            totalCountOfTransactions,
            totalValueOfTransactionsInLCY,
            bankCodeId,
            branchCodeId,
            transactionTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentBankingActivityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (agentUniqueId != null ? "agentUniqueId=" + agentUniqueId + ", " : "") +
            (terminalUniqueId != null ? "terminalUniqueId=" + terminalUniqueId + ", " : "") +
            (totalCountOfTransactions != null ? "totalCountOfTransactions=" + totalCountOfTransactions + ", " : "") +
            (totalValueOfTransactionsInLCY != null ? "totalValueOfTransactionsInLCY=" + totalValueOfTransactionsInLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchCodeId != null ? "branchCodeId=" + branchCodeId + ", " : "") +
            (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
