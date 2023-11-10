package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
    @DecimalMin(value = "0")
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
