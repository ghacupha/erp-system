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
 * DTO representing the lease liability outstanding summary row exposed through the API.
 */
public class LeaseLiabilityOutstandingSummaryDTO implements Serializable {

    private String leaseId;

    private String dealerName;

    private String liabilityAccount;

    private String interestPayableAccount;

    private BigDecimal leasePrincipal;

    private BigDecimal interestPayable;

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

    public String getLiabilityAccount() {
        return liabilityAccount;
    }

    public void setLiabilityAccount(String liabilityAccount) {
        this.liabilityAccount = liabilityAccount;
    }

    public String getInterestPayableAccount() {
        return interestPayableAccount;
    }

    public void setInterestPayableAccount(String interestPayableAccount) {
        this.interestPayableAccount = interestPayableAccount;
    }

    public BigDecimal getLeasePrincipal() {
        return leasePrincipal;
    }

    public void setLeasePrincipal(BigDecimal leasePrincipal) {
        this.leasePrincipal = leasePrincipal;
    }

    public BigDecimal getInterestPayable() {
        return interestPayable;
    }

    public void setInterestPayable(BigDecimal interestPayable) {
        this.interestPayable = interestPayable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityOutstandingSummaryDTO)) {
            return false;
        }
        LeaseLiabilityOutstandingSummaryDTO that = (LeaseLiabilityOutstandingSummaryDTO) o;
        return Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaseId);
    }

    @Override
    public String toString() {
        return "LeaseLiabilityOutstandingSummaryDTO{" +
            "leaseId='" + getLeaseId() + '\'' +
            ", dealerName='" + getDealerName() + '\'' +
            ", liabilityAccount='" + getLiabilityAccount() + '\'' +
            ", interestPayableAccount='" + getInterestPayableAccount() + '\'' +
            ", leasePrincipal=" + getLeasePrincipal() +
            ", interestPayable=" + getInterestPayable() +
            '}';
    }
}
