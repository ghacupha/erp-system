package io.github.erp.domain;

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
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouAccountBalanceReportItem.
 */
@Entity
@Table(name = "rou_account_balance_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rouaccountbalancereportitem")
public class RouAccountBalanceReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_account_name")
    private String assetAccountName;

    @Column(name = "asset_account_number")
    private String assetAccountNumber;

    @Column(name = "depreciation_account_number")
    private String depreciationAccountNumber;

    @Column(name = "total_lease_amount", precision = 21, scale = 2)
    private BigDecimal totalLeaseAmount;

    @Column(name = "accrued_depreciation_amount", precision = 21, scale = 2)
    private BigDecimal accruedDepreciationAmount;

    @Column(name = "current_period_depreciation_amount", precision = 21, scale = 2)
    private BigDecimal currentPeriodDepreciationAmount;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "fiscal_period_end_date")
    private LocalDate fiscalPeriodEndDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouAccountBalanceReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetAccountName() {
        return this.assetAccountName;
    }

    public RouAccountBalanceReportItem assetAccountName(String assetAccountName) {
        this.setAssetAccountName(assetAccountName);
        return this;
    }

    public void setAssetAccountName(String assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public String getAssetAccountNumber() {
        return this.assetAccountNumber;
    }

    public RouAccountBalanceReportItem assetAccountNumber(String assetAccountNumber) {
        this.setAssetAccountNumber(assetAccountNumber);
        return this;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return this.depreciationAccountNumber;
    }

    public RouAccountBalanceReportItem depreciationAccountNumber(String depreciationAccountNumber) {
        this.setDepreciationAccountNumber(depreciationAccountNumber);
        return this;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public BigDecimal getTotalLeaseAmount() {
        return this.totalLeaseAmount;
    }

    public RouAccountBalanceReportItem totalLeaseAmount(BigDecimal totalLeaseAmount) {
        this.setTotalLeaseAmount(totalLeaseAmount);
        return this;
    }

    public void setTotalLeaseAmount(BigDecimal totalLeaseAmount) {
        this.totalLeaseAmount = totalLeaseAmount;
    }

    public BigDecimal getAccruedDepreciationAmount() {
        return this.accruedDepreciationAmount;
    }

    public RouAccountBalanceReportItem accruedDepreciationAmount(BigDecimal accruedDepreciationAmount) {
        this.setAccruedDepreciationAmount(accruedDepreciationAmount);
        return this;
    }

    public void setAccruedDepreciationAmount(BigDecimal accruedDepreciationAmount) {
        this.accruedDepreciationAmount = accruedDepreciationAmount;
    }

    public BigDecimal getCurrentPeriodDepreciationAmount() {
        return this.currentPeriodDepreciationAmount;
    }

    public RouAccountBalanceReportItem currentPeriodDepreciationAmount(BigDecimal currentPeriodDepreciationAmount) {
        this.setCurrentPeriodDepreciationAmount(currentPeriodDepreciationAmount);
        return this;
    }

    public void setCurrentPeriodDepreciationAmount(BigDecimal currentPeriodDepreciationAmount) {
        this.currentPeriodDepreciationAmount = currentPeriodDepreciationAmount;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public RouAccountBalanceReportItem netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getFiscalPeriodEndDate() {
        return this.fiscalPeriodEndDate;
    }

    public RouAccountBalanceReportItem fiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.setFiscalPeriodEndDate(fiscalPeriodEndDate);
        return this;
    }

    public void setFiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAccountBalanceReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouAccountBalanceReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAccountBalanceReportItem{" +
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
