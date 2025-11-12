package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.util.Objects;

/**
 * Projection DTO representing the lease liability interest expense summary report row.
 */
public class LeaseLiabilityInterestExpenseSummaryDTO implements Serializable {

    private String leaseNumber;

    private String dealerName;

    private String narration;

    private String creditAccount;

    private String debitAccount;

    private BigDecimal interestExpense;

    private BigDecimal cumulativeAnnual;

    private BigDecimal cumulativeLastMonth;

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        this.leaseNumber = leaseNumber;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public BigDecimal getInterestExpense() {
        return interestExpense;
    }

    public void setInterestExpense(BigDecimal interestExpense) {
        this.interestExpense = interestExpense;
    }

    public BigDecimal getCumulativeAnnual() {
        return cumulativeAnnual;
    }

    public void setCumulativeAnnual(BigDecimal cumulativeAnnual) {
        this.cumulativeAnnual = cumulativeAnnual;
    }

    public BigDecimal getCumulativeLastMonth() {
        return cumulativeLastMonth;
    }

    public void setCumulativeLastMonth(BigDecimal cumulativeLastMonth) {
        this.cumulativeLastMonth = cumulativeLastMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityInterestExpenseSummaryDTO)) {
            return false;
        }
        LeaseLiabilityInterestExpenseSummaryDTO that = (LeaseLiabilityInterestExpenseSummaryDTO) o;
        return Objects.equals(leaseNumber, that.leaseNumber) && Objects.equals(narration, that.narration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaseNumber, narration);
    }

    @Override
    public String toString() {
        return "LeaseLiabilityInterestExpenseSummaryDTO{" +
            "leaseNumber='" + getLeaseNumber() + '\'' +
            ", dealerName='" + getDealerName() + '\'' +
            ", narration='" + getNarration() + '\'' +
            ", creditAccount='" + getCreditAccount() + '\'' +
            ", debitAccount='" + getDebitAccount() + '\'' +
            ", interestExpense=" + getInterestExpense() +
            ", cumulativeAnnual=" + getCumulativeAnnual() +
            ", cumulativeLastMonth=" + getCumulativeLastMonth() +
            '}';
    }
}
