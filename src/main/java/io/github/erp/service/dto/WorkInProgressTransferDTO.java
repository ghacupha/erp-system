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
import io.github.erp.domain.enumeration.WorkInProgressTransferType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WorkInProgressTransfer} entity.
 */
public class WorkInProgressTransferDTO implements Serializable {

    private Long id;

    private String description;

    private String targetAssetNumber;

    @NotNull
    private BigDecimal transferAmount;

    @NotNull
    private LocalDate transferDate;

    @NotNull
    private WorkInProgressTransferType transferType;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private AssetCategoryDTO assetCategory;

    private WorkInProgressRegistrationDTO workInProgressRegistration;

    private ServiceOutletDTO serviceOutlet;

    private SettlementDTO settlement;

    private WorkProjectRegisterDTO workProjectRegister;

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

    public String getTargetAssetNumber() {
        return targetAssetNumber;
    }

    public void setTargetAssetNumber(String targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public WorkInProgressTransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(WorkInProgressTransferType transferType) {
        this.transferType = transferType;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public WorkInProgressRegistrationDTO getWorkInProgressRegistration() {
        return workInProgressRegistration;
    }

    public void setWorkInProgressRegistration(WorkInProgressRegistrationDTO workInProgressRegistration) {
        this.workInProgressRegistration = workInProgressRegistration;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public SettlementDTO getSettlement() {
        return settlement;
    }

    public void setSettlement(SettlementDTO settlement) {
        this.settlement = settlement;
    }

    public WorkProjectRegisterDTO getWorkProjectRegister() {
        return workProjectRegister;
    }

    public void setWorkProjectRegister(WorkProjectRegisterDTO workProjectRegister) {
        this.workProjectRegister = workProjectRegister;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressTransferDTO)) {
            return false;
        }

        WorkInProgressTransferDTO workInProgressTransferDTO = (WorkInProgressTransferDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workInProgressTransferDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransferDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", targetAssetNumber='" + getTargetAssetNumber() + "'" +
            ", transferAmount=" + getTransferAmount() +
            ", transferDate='" + getTransferDate() + "'" +
            ", transferType='" + getTransferType() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", assetCategory=" + getAssetCategory() +
            ", workInProgressRegistration=" + getWorkInProgressRegistration() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", settlement=" + getSettlement() +
            ", workProjectRegister=" + getWorkProjectRegister() +
            "}";
    }
}
