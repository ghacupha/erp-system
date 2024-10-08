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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NbvReportItem.
 */
@Entity
@Table(name = "nbv_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nbvreportitem")
public class NbvReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "nbv_amount", precision = 21, scale = 2)
    private BigDecimal nbvAmount;

    @Column(name = "previous_nbv_amount", precision = 21, scale = 2)
    private BigDecimal previousNBVAmount;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "batch_sequence_number")
    private String batchSequenceNumber;

    @Column(name = "processed_items")
    private Integer processedItems;

    @Column(name = "total_items_processed")
    private Integer totalItemsProcessed;

    @Column(name = "elapsed_months")
    private Integer elapsedMonths;

    @Column(name = "prior_months")
    private Integer priorMonths;

    @Column(name = "useful_life_years", precision = 21, scale = 2)
    private BigDecimal usefulLifeYears;

    @Column(name = "capitalization_date")
    private String capitalizationDate;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "transaction_date")
    private String transactionDate;

    @Column(name = "depreciation_period_start_date")
    private String depreciationPeriodStartDate;

    @Column(name = "depreciation_period_end_date")
    private String depreciationPeriodEndDate;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "depreciation_period_title")
    private String depreciationPeriodTitle;

    @Column(name = "compilation_job_title")
    private String compilationJobTitle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NbvReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public NbvReportItem assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public BigDecimal getNbvAmount() {
        return this.nbvAmount;
    }

    public NbvReportItem nbvAmount(BigDecimal nbvAmount) {
        this.setNbvAmount(nbvAmount);
        return this;
    }

    public void setNbvAmount(BigDecimal nbvAmount) {
        this.nbvAmount = nbvAmount;
    }

    public BigDecimal getPreviousNBVAmount() {
        return this.previousNBVAmount;
    }

    public NbvReportItem previousNBVAmount(BigDecimal previousNBVAmount) {
        this.setPreviousNBVAmount(previousNBVAmount);
        return this;
    }

    public void setPreviousNBVAmount(BigDecimal previousNBVAmount) {
        this.previousNBVAmount = previousNBVAmount;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public NbvReportItem assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getBatchSequenceNumber() {
        return this.batchSequenceNumber;
    }

    public NbvReportItem batchSequenceNumber(String batchSequenceNumber) {
        this.setBatchSequenceNumber(batchSequenceNumber);
        return this;
    }

    public void setBatchSequenceNumber(String batchSequenceNumber) {
        this.batchSequenceNumber = batchSequenceNumber;
    }

    public Integer getProcessedItems() {
        return this.processedItems;
    }

    public NbvReportItem processedItems(Integer processedItems) {
        this.setProcessedItems(processedItems);
        return this;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public Integer getTotalItemsProcessed() {
        return this.totalItemsProcessed;
    }

    public NbvReportItem totalItemsProcessed(Integer totalItemsProcessed) {
        this.setTotalItemsProcessed(totalItemsProcessed);
        return this;
    }

    public void setTotalItemsProcessed(Integer totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
    }

    public Integer getElapsedMonths() {
        return this.elapsedMonths;
    }

    public NbvReportItem elapsedMonths(Integer elapsedMonths) {
        this.setElapsedMonths(elapsedMonths);
        return this;
    }

    public void setElapsedMonths(Integer elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public Integer getPriorMonths() {
        return this.priorMonths;
    }

    public NbvReportItem priorMonths(Integer priorMonths) {
        this.setPriorMonths(priorMonths);
        return this;
    }

    public void setPriorMonths(Integer priorMonths) {
        this.priorMonths = priorMonths;
    }

    public BigDecimal getUsefulLifeYears() {
        return this.usefulLifeYears;
    }

    public NbvReportItem usefulLifeYears(BigDecimal usefulLifeYears) {
        this.setUsefulLifeYears(usefulLifeYears);
        return this;
    }

    public void setUsefulLifeYears(BigDecimal usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public String getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public NbvReportItem capitalizationDate(String capitalizationDate) {
        this.setCapitalizationDate(capitalizationDate);
        return this;
    }

    public void setCapitalizationDate(String capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public String getTransactionNumber() {
        return this.transactionNumber;
    }

    public NbvReportItem transactionNumber(String transactionNumber) {
        this.setTransactionNumber(transactionNumber);
        return this;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public NbvReportItem transactionDate(String transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDepreciationPeriodStartDate() {
        return this.depreciationPeriodStartDate;
    }

    public NbvReportItem depreciationPeriodStartDate(String depreciationPeriodStartDate) {
        this.setDepreciationPeriodStartDate(depreciationPeriodStartDate);
        return this;
    }

    public void setDepreciationPeriodStartDate(String depreciationPeriodStartDate) {
        this.depreciationPeriodStartDate = depreciationPeriodStartDate;
    }

    public String getDepreciationPeriodEndDate() {
        return this.depreciationPeriodEndDate;
    }

    public NbvReportItem depreciationPeriodEndDate(String depreciationPeriodEndDate) {
        this.setDepreciationPeriodEndDate(depreciationPeriodEndDate);
        return this;
    }

    public void setDepreciationPeriodEndDate(String depreciationPeriodEndDate) {
        this.depreciationPeriodEndDate = depreciationPeriodEndDate;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public NbvReportItem serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetCategoryName() {
        return this.assetCategoryName;
    }

    public NbvReportItem assetCategoryName(String assetCategoryName) {
        this.setAssetCategoryName(assetCategoryName);
        return this;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDepreciationPeriodTitle() {
        return this.depreciationPeriodTitle;
    }

    public NbvReportItem depreciationPeriodTitle(String depreciationPeriodTitle) {
        this.setDepreciationPeriodTitle(depreciationPeriodTitle);
        return this;
    }

    public void setDepreciationPeriodTitle(String depreciationPeriodTitle) {
        this.depreciationPeriodTitle = depreciationPeriodTitle;
    }

    public String getCompilationJobTitle() {
        return this.compilationJobTitle;
    }

    public NbvReportItem compilationJobTitle(String compilationJobTitle) {
        this.setCompilationJobTitle(compilationJobTitle);
        return this;
    }

    public void setCompilationJobTitle(String compilationJobTitle) {
        this.compilationJobTitle = compilationJobTitle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NbvReportItem)) {
            return false;
        }
        return id != null && id.equals(((NbvReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvReportItem{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", nbvAmount=" + getNbvAmount() +
            ", previousNBVAmount=" + getPreviousNBVAmount() +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", batchSequenceNumber='" + getBatchSequenceNumber() + "'" +
            ", processedItems=" + getProcessedItems() +
            ", totalItemsProcessed=" + getTotalItemsProcessed() +
            ", elapsedMonths=" + getElapsedMonths() +
            ", priorMonths=" + getPriorMonths() +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", depreciationPeriodStartDate='" + getDepreciationPeriodStartDate() + "'" +
            ", depreciationPeriodEndDate='" + getDepreciationPeriodEndDate() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", depreciationPeriodTitle='" + getDepreciationPeriodTitle() + "'" +
            ", compilationJobTitle='" + getCompilationJobTitle() + "'" +
            "}";
    }
}
