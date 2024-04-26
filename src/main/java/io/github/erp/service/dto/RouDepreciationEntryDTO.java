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

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO creditAccount;

    private AssetCategoryDTO assetCategory;

    private IFRS16LeaseContractDTO leaseContract;

    private RouModelMetadataDTO rouMetadata;

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
            ", debitAccount=" + getDebitAccount() +
            ", creditAccount=" + getCreditAccount() +
            ", assetCategory=" + getAssetCategory() +
            ", leaseContract=" + getLeaseContract() +
            ", rouMetadata=" + getRouMetadata() +
            "}";
    }
}
