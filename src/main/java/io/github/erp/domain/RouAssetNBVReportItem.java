package io.github.erp.domain;

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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouAssetNBVReportItem.
 */
@Entity
@Table(name = "rou_assetnbvreport_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rouassetnbvreportitem")
public class RouAssetNBVReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "model_title")
    private String modelTitle;

    @Column(name = "model_version", precision = 21, scale = 2)
    private BigDecimal modelVersion;

    @Column(name = "description")
    private String description;

    @Column(name = "rou_model_reference")
    private String rouModelReference;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "asset_account_number")
    private String assetAccountNumber;

    @Column(name = "depreciation_account_number")
    private String depreciationAccountNumber;

    @Column(name = "fiscal_period_end_date")
    private LocalDate fiscalPeriodEndDate;

    @Column(name = "lease_amount", precision = 21, scale = 2)
    private BigDecimal leaseAmount;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouAssetNBVReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return this.modelTitle;
    }

    public RouAssetNBVReportItem modelTitle(String modelTitle) {
        this.setModelTitle(modelTitle);
        return this;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimal getModelVersion() {
        return this.modelVersion;
    }

    public RouAssetNBVReportItem modelVersion(BigDecimal modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(BigDecimal modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public RouAssetNBVReportItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouModelReference() {
        return this.rouModelReference;
    }

    public RouAssetNBVReportItem rouModelReference(String rouModelReference) {
        this.setRouModelReference(rouModelReference);
        return this;
    }

    public void setRouModelReference(String rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public RouAssetNBVReportItem commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public RouAssetNBVReportItem expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAssetCategoryName() {
        return this.assetCategoryName;
    }

    public RouAssetNBVReportItem assetCategoryName(String assetCategoryName) {
        this.setAssetCategoryName(assetCategoryName);
        return this;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getAssetAccountNumber() {
        return this.assetAccountNumber;
    }

    public RouAssetNBVReportItem assetAccountNumber(String assetAccountNumber) {
        this.setAssetAccountNumber(assetAccountNumber);
        return this;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return this.depreciationAccountNumber;
    }

    public RouAssetNBVReportItem depreciationAccountNumber(String depreciationAccountNumber) {
        this.setDepreciationAccountNumber(depreciationAccountNumber);
        return this;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public LocalDate getFiscalPeriodEndDate() {
        return this.fiscalPeriodEndDate;
    }

    public RouAssetNBVReportItem fiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.setFiscalPeriodEndDate(fiscalPeriodEndDate);
        return this;
    }

    public void setFiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    public BigDecimal getLeaseAmount() {
        return this.leaseAmount;
    }

    public RouAssetNBVReportItem leaseAmount(BigDecimal leaseAmount) {
        this.setLeaseAmount(leaseAmount);
        return this;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public RouAssetNBVReportItem netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAssetNBVReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouAssetNBVReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetNBVReportItem{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", rouModelReference='" + getRouModelReference() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", fiscalPeriodEndDate='" + getFiscalPeriodEndDate() + "'" +
            ", leaseAmount=" + getLeaseAmount() +
            ", netBookValue=" + getNetBookValue() +
            "}";
    }
}
