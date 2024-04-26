package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationEntry} entity.
 */
public class DepreciationEntryDTO implements Serializable {

    private Long id;

    private ZonedDateTime postedAt;

    private BigDecimal depreciationAmount;

    private Long assetNumber;

    private UUID depreciationPeriodIdentifier;

    private UUID depreciationJobIdentifier;

    private UUID fiscalMonthIdentifier;

    private UUID fiscalQuarterIdentifier;

    private Integer batchSequenceNumber;

    private String processedItems;

    private Integer totalItemsProcessed;

    private Long elapsedMonths;

    private Long priorMonths;

    private BigDecimal usefulLifeYears;

    private BigDecimal previousNBV;

    private BigDecimal netBookValue;

    private LocalDate depreciationPeriodStartDate;

    private LocalDate depreciationPeriodEndDate;

    private LocalDate capitalizationDate;

    private ServiceOutletDTO serviceOutlet;

    private AssetCategoryDTO assetCategory;

    private DepreciationMethodDTO depreciationMethod;

    private AssetRegistrationDTO assetRegistration;

    private DepreciationPeriodDTO depreciationPeriod;

    private FiscalMonthDTO fiscalMonth;

    private FiscalQuarterDTO fiscalQuarter;

    private FiscalYearDTO fiscalYear;

    private DepreciationJobDTO depreciationJob;

    private DepreciationBatchSequenceDTO depreciationBatchSequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(ZonedDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public Long getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public UUID getDepreciationPeriodIdentifier() {
        return depreciationPeriodIdentifier;
    }

    public void setDepreciationPeriodIdentifier(UUID depreciationPeriodIdentifier) {
        this.depreciationPeriodIdentifier = depreciationPeriodIdentifier;
    }

    public UUID getDepreciationJobIdentifier() {
        return depreciationJobIdentifier;
    }

    public void setDepreciationJobIdentifier(UUID depreciationJobIdentifier) {
        this.depreciationJobIdentifier = depreciationJobIdentifier;
    }

    public UUID getFiscalMonthIdentifier() {
        return fiscalMonthIdentifier;
    }

    public void setFiscalMonthIdentifier(UUID fiscalMonthIdentifier) {
        this.fiscalMonthIdentifier = fiscalMonthIdentifier;
    }

    public UUID getFiscalQuarterIdentifier() {
        return fiscalQuarterIdentifier;
    }

    public void setFiscalQuarterIdentifier(UUID fiscalQuarterIdentifier) {
        this.fiscalQuarterIdentifier = fiscalQuarterIdentifier;
    }

    public Integer getBatchSequenceNumber() {
        return batchSequenceNumber;
    }

    public void setBatchSequenceNumber(Integer batchSequenceNumber) {
        this.batchSequenceNumber = batchSequenceNumber;
    }

    public String getProcessedItems() {
        return processedItems;
    }

    public void setProcessedItems(String processedItems) {
        this.processedItems = processedItems;
    }

    public Integer getTotalItemsProcessed() {
        return totalItemsProcessed;
    }

    public void setTotalItemsProcessed(Integer totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
    }

    public Long getElapsedMonths() {
        return elapsedMonths;
    }

    public void setElapsedMonths(Long elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public Long getPriorMonths() {
        return priorMonths;
    }

    public void setPriorMonths(Long priorMonths) {
        this.priorMonths = priorMonths;
    }

    public BigDecimal getUsefulLifeYears() {
        return usefulLifeYears;
    }

    public void setUsefulLifeYears(BigDecimal usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimal getPreviousNBV() {
        return previousNBV;
    }

    public void setPreviousNBV(BigDecimal previousNBV) {
        this.previousNBV = previousNBV;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getDepreciationPeriodStartDate() {
        return depreciationPeriodStartDate;
    }

    public void setDepreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
        this.depreciationPeriodStartDate = depreciationPeriodStartDate;
    }

    public LocalDate getDepreciationPeriodEndDate() {
        return depreciationPeriodEndDate;
    }

    public void setDepreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
        this.depreciationPeriodEndDate = depreciationPeriodEndDate;
    }

    public LocalDate getCapitalizationDate() {
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public DepreciationMethodDTO getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethodDTO depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public AssetRegistrationDTO getAssetRegistration() {
        return assetRegistration;
    }

    public void setAssetRegistration(AssetRegistrationDTO assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public DepreciationPeriodDTO getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriodDTO depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public FiscalQuarterDTO getFiscalQuarter() {
        return fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarterDTO fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public FiscalYearDTO getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYearDTO fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public DepreciationJobDTO getDepreciationJob() {
        return depreciationJob;
    }

    public void setDepreciationJob(DepreciationJobDTO depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    public DepreciationBatchSequenceDTO getDepreciationBatchSequence() {
        return depreciationBatchSequence;
    }

    public void setDepreciationBatchSequence(DepreciationBatchSequenceDTO depreciationBatchSequence) {
        this.depreciationBatchSequence = depreciationBatchSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationEntryDTO)) {
            return false;
        }

        DepreciationEntryDTO depreciationEntryDTO = (DepreciationEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryDTO{" +
            "id=" + getId() +
            ", postedAt='" + getPostedAt() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", assetNumber=" + getAssetNumber() +
            ", depreciationPeriodIdentifier='" + getDepreciationPeriodIdentifier() + "'" +
            ", depreciationJobIdentifier='" + getDepreciationJobIdentifier() + "'" +
            ", fiscalMonthIdentifier='" + getFiscalMonthIdentifier() + "'" +
            ", fiscalQuarterIdentifier='" + getFiscalQuarterIdentifier() + "'" +
            ", batchSequenceNumber=" + getBatchSequenceNumber() +
            ", processedItems='" + getProcessedItems() + "'" +
            ", totalItemsProcessed=" + getTotalItemsProcessed() +
            ", elapsedMonths=" + getElapsedMonths() +
            ", priorMonths=" + getPriorMonths() +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", previousNBV=" + getPreviousNBV() +
            ", netBookValue=" + getNetBookValue() +
            ", depreciationPeriodStartDate='" + getDepreciationPeriodStartDate() + "'" +
            ", depreciationPeriodEndDate='" + getDepreciationPeriodEndDate() + "'" +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", serviceOutlet=" + getServiceOutlet() +
            ", assetCategory=" + getAssetCategory() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", assetRegistration=" + getAssetRegistration() +
            ", depreciationPeriod=" + getDepreciationPeriod() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", fiscalQuarter=" + getFiscalQuarter() +
            ", fiscalYear=" + getFiscalYear() +
            ", depreciationJob=" + getDepreciationJob() +
            ", depreciationBatchSequence=" + getDepreciationBatchSequence() +
            "}";
    }
}
