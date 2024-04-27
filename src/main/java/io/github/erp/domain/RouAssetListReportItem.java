package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouAssetListReportItem.
 */
@Entity
@Table(name = "rou_asset_list_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rouassetlistreportitem")
public class RouAssetListReportItem implements Serializable {

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

    @Column(name = "lease_term_periods")
    private Integer leaseTermPeriods;

    @Column(name = "rou_model_reference")
    private UUID rouModelReference;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "lease_contract_title")
    private String leaseContractTitle;

    @Column(name = "asset_account_number")
    private String assetAccountNumber;

    @Column(name = "depreciation_account_number")
    private String depreciationAccountNumber;

    @Column(name = "accrued_depreciation_account_number")
    private String accruedDepreciationAccountNumber;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "lease_amount", precision = 21, scale = 2)
    private BigDecimal leaseAmount;

    @Column(name = "lease_contract_serial_number")
    private String leaseContractSerialNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouAssetListReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return this.modelTitle;
    }

    public RouAssetListReportItem modelTitle(String modelTitle) {
        this.setModelTitle(modelTitle);
        return this;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimal getModelVersion() {
        return this.modelVersion;
    }

    public RouAssetListReportItem modelVersion(BigDecimal modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(BigDecimal modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public RouAssetListReportItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeaseTermPeriods() {
        return this.leaseTermPeriods;
    }

    public RouAssetListReportItem leaseTermPeriods(Integer leaseTermPeriods) {
        this.setLeaseTermPeriods(leaseTermPeriods);
        return this;
    }

    public void setLeaseTermPeriods(Integer leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public UUID getRouModelReference() {
        return this.rouModelReference;
    }

    public RouAssetListReportItem rouModelReference(UUID rouModelReference) {
        this.setRouModelReference(rouModelReference);
        return this;
    }

    public void setRouModelReference(UUID rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public RouAssetListReportItem commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public RouAssetListReportItem expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLeaseContractTitle() {
        return this.leaseContractTitle;
    }

    public RouAssetListReportItem leaseContractTitle(String leaseContractTitle) {
        this.setLeaseContractTitle(leaseContractTitle);
        return this;
    }

    public void setLeaseContractTitle(String leaseContractTitle) {
        this.leaseContractTitle = leaseContractTitle;
    }

    public String getAssetAccountNumber() {
        return this.assetAccountNumber;
    }

    public RouAssetListReportItem assetAccountNumber(String assetAccountNumber) {
        this.setAssetAccountNumber(assetAccountNumber);
        return this;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return this.depreciationAccountNumber;
    }

    public RouAssetListReportItem depreciationAccountNumber(String depreciationAccountNumber) {
        this.setDepreciationAccountNumber(depreciationAccountNumber);
        return this;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public String getAccruedDepreciationAccountNumber() {
        return this.accruedDepreciationAccountNumber;
    }

    public RouAssetListReportItem accruedDepreciationAccountNumber(String accruedDepreciationAccountNumber) {
        this.setAccruedDepreciationAccountNumber(accruedDepreciationAccountNumber);
        return this;
    }

    public void setAccruedDepreciationAccountNumber(String accruedDepreciationAccountNumber) {
        this.accruedDepreciationAccountNumber = accruedDepreciationAccountNumber;
    }

    public String getAssetCategoryName() {
        return this.assetCategoryName;
    }

    public RouAssetListReportItem assetCategoryName(String assetCategoryName) {
        this.setAssetCategoryName(assetCategoryName);
        return this;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public BigDecimal getLeaseAmount() {
        return this.leaseAmount;
    }

    public RouAssetListReportItem leaseAmount(BigDecimal leaseAmount) {
        this.setLeaseAmount(leaseAmount);
        return this;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public String getLeaseContractSerialNumber() {
        return this.leaseContractSerialNumber;
    }

    public RouAssetListReportItem leaseContractSerialNumber(String leaseContractSerialNumber) {
        this.setLeaseContractSerialNumber(leaseContractSerialNumber);
        return this;
    }

    public void setLeaseContractSerialNumber(String leaseContractSerialNumber) {
        this.leaseContractSerialNumber = leaseContractSerialNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAssetListReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouAssetListReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetListReportItem{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", leaseTermPeriods=" + getLeaseTermPeriods() +
            ", rouModelReference='" + getRouModelReference() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", leaseContractTitle='" + getLeaseContractTitle() + "'" +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", accruedDepreciationAccountNumber='" + getAccruedDepreciationAccountNumber() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", leaseAmount=" + getLeaseAmount() +
            ", leaseContractSerialNumber='" + getLeaseContractSerialNumber() + "'" +
            "}";
    }
}
