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
 * Projection DTO representing the lease liability maturity summary report row.
 */
public class LeaseLiabilityMaturitySummaryDTO implements Serializable {

    private String leaseId;

    private String dealerName;

    private BigDecimal currentPeriod;

    private BigDecimal nextTwelveMonths;

    private BigDecimal beyondTwelveMonths;

    private BigDecimal totalUndiscounted;

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

    public BigDecimal getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(BigDecimal currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public BigDecimal getNextTwelveMonths() {
        return nextTwelveMonths;
    }

    public void setNextTwelveMonths(BigDecimal nextTwelveMonths) {
        this.nextTwelveMonths = nextTwelveMonths;
    }

    public BigDecimal getBeyondTwelveMonths() {
        return beyondTwelveMonths;
    }

    public void setBeyondTwelveMonths(BigDecimal beyondTwelveMonths) {
        this.beyondTwelveMonths = beyondTwelveMonths;
    }

    public BigDecimal getTotalUndiscounted() {
        return totalUndiscounted;
    }

    public void setTotalUndiscounted(BigDecimal totalUndiscounted) {
        this.totalUndiscounted = totalUndiscounted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityMaturitySummaryDTO)) {
            return false;
        }
        LeaseLiabilityMaturitySummaryDTO that = (LeaseLiabilityMaturitySummaryDTO) o;
        return Objects.equals(leaseId, that.leaseId) && Objects.equals(dealerName, that.dealerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaseId, dealerName);
    }

    @Override
    public String toString() {
        return "LeaseLiabilityMaturitySummaryDTO{" +
            "leaseId='" + getLeaseId() + '\'' +
            ", dealerName='" + getDealerName() + '\'' +
            ", currentPeriod=" + getCurrentPeriod() +
            ", nextTwelveMonths=" + getNextTwelveMonths() +
            ", beyondTwelveMonths=" + getBeyondTwelveMonths() +
            ", totalUndiscounted=" + getTotalUndiscounted() +
            '}';
    }
}
