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

    @NotNull
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
            ", ifrs16LeaseContract=" + getIfrs16LeaseContract() +
            ", assetAccount=" + getAssetAccount() +
            ", depreciationAccount=" + getDepreciationAccount() +
            ", accruedDepreciationAccount=" + getAccruedDepreciationAccount() +
            ", assetCategory=" + getAssetCategory() +
            ", documentAttachments=" + getDocumentAttachments() +
            "}";
    }
}
