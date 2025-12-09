package io.github.erp.erp.leases.liability.enumeration;

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
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo. Id. CLASS)
public class LiabilityEnumerationResponse implements Serializable {

    private Long liabilityEnumerationId;
    private Long leaseAmortizationCalculationId;
    private Integer numberOfPeriods;
    private String periodicity;
    private BigDecimal totalPresentValue;
    private BigDecimal discountRatePerPeriod;

    public Long getLiabilityEnumerationId() {
        return liabilityEnumerationId;
    }

    public void setLiabilityEnumerationId(Long liabilityEnumerationId) {
        this.liabilityEnumerationId = liabilityEnumerationId;
    }

    public Long getLeaseAmortizationCalculationId() {
        return leaseAmortizationCalculationId;
    }

    public void setLeaseAmortizationCalculationId(Long leaseAmortizationCalculationId) {
        this.leaseAmortizationCalculationId = leaseAmortizationCalculationId;
    }

    public Integer getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        this.numberOfPeriods = numberOfPeriods;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public BigDecimal getTotalPresentValue() {
        return totalPresentValue;
    }

    public void setTotalPresentValue(BigDecimal totalPresentValue) {
        this.totalPresentValue = totalPresentValue;
    }

    public BigDecimal getDiscountRatePerPeriod() {
        return discountRatePerPeriod;
    }

    public void setDiscountRatePerPeriod(BigDecimal discountRatePerPeriod) {
        this.discountRatePerPeriod = discountRatePerPeriod;
    }
}
