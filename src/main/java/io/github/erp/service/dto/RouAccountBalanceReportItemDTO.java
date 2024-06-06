package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.RouAccountBalanceReportItem} entity.
 */
public class RouAccountBalanceReportItemDTO implements Serializable {

    private Long id;

    private String assetAccountName;

    private String assetAccountNumber;

    private String depreciationAccountNumber;

    private BigDecimal totalLeaseAmount;

    private BigDecimal accruedDepreciationAmount;

    private BigDecimal currentPeriodDepreciationAmount;

    private BigDecimal netBookValue;

    private LocalDate fiscalPeriodEndDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetAccountName() {
        return assetAccountName;
    }

    public void setAssetAccountName(String assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public String getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public BigDecimal getTotalLeaseAmount() {
        return totalLeaseAmount;
    }

    public void setTotalLeaseAmount(BigDecimal totalLeaseAmount) {
        this.totalLeaseAmount = totalLeaseAmount;
    }

    public BigDecimal getAccruedDepreciationAmount() {
        return accruedDepreciationAmount;
    }

    public void setAccruedDepreciationAmount(BigDecimal accruedDepreciationAmount) {
        this.accruedDepreciationAmount = accruedDepreciationAmount;
    }

    public BigDecimal getCurrentPeriodDepreciationAmount() {
        return currentPeriodDepreciationAmount;
    }

    public void setCurrentPeriodDepreciationAmount(BigDecimal currentPeriodDepreciationAmount) {
        this.currentPeriodDepreciationAmount = currentPeriodDepreciationAmount;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getFiscalPeriodEndDate() {
        return fiscalPeriodEndDate;
    }

    public void setFiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAccountBalanceReportItemDTO)) {
            return false;
        }

        RouAccountBalanceReportItemDTO rouAccountBalanceReportItemDTO = (RouAccountBalanceReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouAccountBalanceReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAccountBalanceReportItemDTO{" +
            "id=" + getId() +
            ", assetAccountName='" + getAssetAccountName() + "'" +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", totalLeaseAmount=" + getTotalLeaseAmount() +
            ", accruedDepreciationAmount=" + getAccruedDepreciationAmount() +
            ", currentPeriodDepreciationAmount=" + getCurrentPeriodDepreciationAmount() +
            ", netBookValue=" + getNetBookValue() +
            ", fiscalPeriodEndDate='" + getFiscalPeriodEndDate() + "'" +
            "}";
    }
}
