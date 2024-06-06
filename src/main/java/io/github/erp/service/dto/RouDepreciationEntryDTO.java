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
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RouDepreciationEntry} entity.
 */
public class RouDepreciationEntryDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal depreciationAmount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal outstandingAmount;

    private UUID rouAssetIdentifier;

    @NotNull
    private UUID rouDepreciationIdentifier;

    private Integer sequenceNumber;

    private Boolean activated;

    private Boolean isDeleted;

    private UUID batchJobIdentifier;

    private UUID depreciationAmountStepIdentifier;

    private UUID outstandingAmountStepIdentifier;

    private UUID flagAmortisedStepIdentifier;

    private ZonedDateTime compilationTime;

    private Boolean invalidated;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO creditAccount;

    private AssetCategoryDTO assetCategory;

    private IFRS16LeaseContractDTO leaseContract;

    private RouModelMetadataDTO rouMetadata;

    private LeasePeriodDTO leasePeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public UUID getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(UUID rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUID getRouDepreciationIdentifier() {
        return rouDepreciationIdentifier;
    }

    public void setRouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Boolean getInvalidated() {
        return invalidated;
    }

    public void setInvalidated(Boolean invalidated) {
        this.invalidated = invalidated;
    }

    public TransactionAccountDTO getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(TransactionAccountDTO debitAccount) {
        this.debitAccount = debitAccount;
    }

    public TransactionAccountDTO getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(TransactionAccountDTO creditAccount) {
        this.creditAccount = creditAccount;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public RouModelMetadataDTO getRouMetadata() {
        return rouMetadata;
    }

    public void setRouMetadata(RouModelMetadataDTO rouMetadata) {
        this.rouMetadata = rouMetadata;
    }

    public LeasePeriodDTO getLeasePeriod() {
        return leasePeriod;
    }

    public void setLeasePeriod(LeasePeriodDTO leasePeriod) {
        this.leasePeriod = leasePeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationEntryDTO)) {
            return false;
        }

        RouDepreciationEntryDTO rouDepreciationEntryDTO = (RouDepreciationEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouDepreciationEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            ", rouAssetIdentifier='" + getRouAssetIdentifier() + "'" +
            ", rouDepreciationIdentifier='" + getRouDepreciationIdentifier() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", activated='" + getActivated() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", depreciationAmountStepIdentifier='" + getDepreciationAmountStepIdentifier() + "'" +
            ", outstandingAmountStepIdentifier='" + getOutstandingAmountStepIdentifier() + "'" +
            ", flagAmortisedStepIdentifier='" + getFlagAmortisedStepIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            ", invalidated='" + getInvalidated() + "'" +
            ", debitAccount=" + getDebitAccount() +
            ", creditAccount=" + getCreditAccount() +
            ", assetCategory=" + getAssetCategory() +
            ", leaseContract=" + getLeaseContract() +
            ", rouMetadata=" + getRouMetadata() +
            ", leasePeriod=" + getLeasePeriod() +
            "}";
    }
}
