package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * DTO representing the lease interest paid transfer summary row exposed through the API.
 */
public class LeaseInterestPaidTransferSummaryDTO implements Serializable {

    private String leaseId;

    private String dealerName;

    private String narration;

    private String creditAccount;

    private String debitAccount;

    private BigDecimal interestAmount;

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
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

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseInterestPaidTransferSummaryDTO)) {
            return false;
        }
        LeaseInterestPaidTransferSummaryDTO that = (LeaseInterestPaidTransferSummaryDTO) o;
        return Objects.equals(leaseId, that.leaseId) && Objects.equals(narration, that.narration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaseId, narration);
    }

    @Override
    public String toString() {
        return "LeaseInterestPaidTransferSummaryDTO{" +
            "leaseId='" + getLeaseId() + '\'' +
            ", dealerName='" + getDealerName() + '\'' +
            ", narration='" + getNarration() + '\'' +
            ", creditAccount='" + getCreditAccount() + '\'' +
            ", debitAccount='" + getDebitAccount() + '\'' +
            ", interestAmount=" + getInterestAmount() +
            '}';
    }
}
