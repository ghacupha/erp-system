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
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RouModelMetadata} entity.
 */
public class RouModelMetadataDTO implements Serializable {

    private Long id;

    @NotNull
    private String modelTitle;

    @NotNull
    private BigDecimal modelVersion;

    private String description;

    private Integer leaseTermPeriods;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal leaseAmount;

    @NotNull
    private UUID rouModelReference;

    private LocalDate commencementDate;

    private LocalDate expirationDate;

    private Boolean hasBeenFullyAmortised;

    private Boolean hasBeenDecommissioned;

    private UUID batchJobIdentifier;

    private UUID depreciationAmountStepIdentifier;

    private UUID outstandingAmountStepIdentifier;

    private UUID flagAmortisedStepIdentifier;

    private ZonedDateTime compilationTime;

    private IFRS16LeaseContractDTO ifrs16LeaseContract;

    private TransactionAccountDTO assetAccount;

    private TransactionAccountDTO depreciationAccount;

    private TransactionAccountDTO accruedDepreciationAccount;

    private AssetCategoryDTO assetCategory;

    private Set<BusinessDocumentDTO> documentAttachments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimal getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(BigDecimal modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeaseTermPeriods() {
        return leaseTermPeriods;
    }

    public void setLeaseTermPeriods(Integer leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public BigDecimal getLeaseAmount() {
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public UUID getRouModelReference() {
        return rouModelReference;
    }

    public void setRouModelReference(UUID rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getHasBeenFullyAmortised() {
        return hasBeenFullyAmortised;
    }

    public void setHasBeenFullyAmortised(Boolean hasBeenFullyAmortised) {
        this.hasBeenFullyAmortised = hasBeenFullyAmortised;
    }

    public Boolean getHasBeenDecommissioned() {
        return hasBeenDecommissioned;
    }

    public void setHasBeenDecommissioned(Boolean hasBeenDecommissioned) {
        this.hasBeenDecommissioned = hasBeenDecommissioned;
    }

    public UUID getBatchJobIdentifier() {
        return batchJobIdentifier;
    }

    public void setBatchJobIdentifier(UUID batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public UUID getDepreciationAmountStepIdentifier() {
        return depreciationAmountStepIdentifier;
    }

    public void setDepreciationAmountStepIdentifier(UUID depreciationAmountStepIdentifier) {
        this.depreciationAmountStepIdentifier = depreciationAmountStepIdentifier;
    }

    public UUID getOutstandingAmountStepIdentifier() {
        return outstandingAmountStepIdentifier;
    }

    public void setOutstandingAmountStepIdentifier(UUID outstandingAmountStepIdentifier) {
        this.outstandingAmountStepIdentifier = outstandingAmountStepIdentifier;
    }

    public UUID getFlagAmortisedStepIdentifier() {
        return flagAmortisedStepIdentifier;
    }

    public void setFlagAmortisedStepIdentifier(UUID flagAmortisedStepIdentifier) {
        this.flagAmortisedStepIdentifier = flagAmortisedStepIdentifier;
    }

    public ZonedDateTime getCompilationTime() {
        return compilationTime;
    }

    public void setCompilationTime(ZonedDateTime compilationTime) {
        this.compilationTime = compilationTime;
    }

    public IFRS16LeaseContractDTO getIfrs16LeaseContract() {
        return ifrs16LeaseContract;
    }

    public void setIfrs16LeaseContract(IFRS16LeaseContractDTO ifrs16LeaseContract) {
        this.ifrs16LeaseContract = ifrs16LeaseContract;
    }

    public TransactionAccountDTO getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(TransactionAccountDTO assetAccount) {
        this.assetAccount = assetAccount;
    }

    public TransactionAccountDTO getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(TransactionAccountDTO depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public TransactionAccountDTO getAccruedDepreciationAccount() {
        return accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccountDTO accruedDepreciationAccount) {
        this.accruedDepreciationAccount = accruedDepreciationAccount;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public Set<BusinessDocumentDTO> getDocumentAttachments() {
        return documentAttachments;
    }

    public void setDocumentAttachments(Set<BusinessDocumentDTO> documentAttachments) {
        this.documentAttachments = documentAttachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouModelMetadataDTO)) {
            return false;
        }

        RouModelMetadataDTO rouModelMetadataDTO = (RouModelMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouModelMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouModelMetadataDTO{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", leaseTermPeriods=" + getLeaseTermPeriods() +
            ", leaseAmount=" + getLeaseAmount() +
            ", rouModelReference='" + getRouModelReference() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", hasBeenFullyAmortised='" + getHasBeenFullyAmortised() + "'" +
            ", hasBeenDecommissioned='" + getHasBeenDecommissioned() + "'" +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", depreciationAmountStepIdentifier='" + getDepreciationAmountStepIdentifier() + "'" +
            ", outstandingAmountStepIdentifier='" + getOutstandingAmountStepIdentifier() + "'" +
            ", flagAmortisedStepIdentifier='" + getFlagAmortisedStepIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            ", ifrs16LeaseContract=" + getIfrs16LeaseContract() +
            ", assetAccount=" + getAssetAccount() +
            ", depreciationAccount=" + getDepreciationAccount() +
            ", accruedDepreciationAccount=" + getAccruedDepreciationAccount() +
            ", assetCategory=" + getAssetCategory() +
            ", documentAttachments=" + getDocumentAttachments() +
            "}";
    }
}
