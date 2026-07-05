package io.github.erp.domain;

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
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouDepreciationEntryReportItem.
 */
@Entity
@Table(name = "rou_depreciation_entry_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roudepreciationentryreportitem")
public class RouDepreciationEntryReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "lease_contract_number")
    private String leaseContractNumber;

    @Column(name = "fiscal_period_code")
    private String fiscalPeriodCode;

    @Column(name = "fiscal_period_end_date")
    private LocalDate fiscalPeriodEndDate;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "debit_account_number")
    private String debitAccountNumber;

    @Column(name = "credit_account_number")
    private String creditAccountNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "rou_asset_identifier")
    private String rouAssetIdentifier;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouDepreciationEntryReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return this.leaseContractNumber;
    }

    public RouDepreciationEntryReportItem leaseContractNumber(String leaseContractNumber) {
        this.setLeaseContractNumber(leaseContractNumber);
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getFiscalPeriodCode() {
        return this.fiscalPeriodCode;
    }

    public RouDepreciationEntryReportItem fiscalPeriodCode(String fiscalPeriodCode) {
        this.setFiscalPeriodCode(fiscalPeriodCode);
        return this;
    }

    public void setFiscalPeriodCode(String fiscalPeriodCode) {
        this.fiscalPeriodCode = fiscalPeriodCode;
    }

    public LocalDate getFiscalPeriodEndDate() {
        return this.fiscalPeriodEndDate;
    }

    public RouDepreciationEntryReportItem fiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.setFiscalPeriodEndDate(fiscalPeriodEndDate);
        return this;
    }

    public void setFiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    public String getAssetCategoryName() {
        return this.assetCategoryName;
    }

    public RouDepreciationEntryReportItem assetCategoryName(String assetCategoryName) {
        this.setAssetCategoryName(assetCategoryName);
        return this;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDebitAccountNumber() {
        return this.debitAccountNumber;
    }

    public RouDepreciationEntryReportItem debitAccountNumber(String debitAccountNumber) {
        this.setDebitAccountNumber(debitAccountNumber);
        return this;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public String getCreditAccountNumber() {
        return this.creditAccountNumber;
    }

    public RouDepreciationEntryReportItem creditAccountNumber(String creditAccountNumber) {
        this.setCreditAccountNumber(creditAccountNumber);
        return this;
    }

    public void setCreditAccountNumber(String creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public RouDepreciationEntryReportItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public RouDepreciationEntryReportItem shortTitle(String shortTitle) {
        this.setShortTitle(shortTitle);
        return this;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getRouAssetIdentifier() {
        return this.rouAssetIdentifier;
    }

    public RouDepreciationEntryReportItem rouAssetIdentifier(String rouAssetIdentifier) {
        this.setRouAssetIdentifier(rouAssetIdentifier);
        return this;
    }

    public void setRouAssetIdentifier(String rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public RouDepreciationEntryReportItem sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public RouDepreciationEntryReportItem depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public RouDepreciationEntryReportItem outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationEntryReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouDepreciationEntryReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryReportItem{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", fiscalPeriodCode='" + getFiscalPeriodCode() + "'" +
            ", fiscalPeriodEndDate='" + getFiscalPeriodEndDate() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", debitAccountNumber='" + getDebitAccountNumber() + "'" +
            ", creditAccountNumber='" + getCreditAccountNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortTitle='" + getShortTitle() + "'" +
            ", rouAssetIdentifier='" + getRouAssetIdentifier() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
