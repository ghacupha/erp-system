package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetAccessory} entity.
 */
public class AssetAccessoryDTO implements Serializable {

    private Long id;

    private String assetTag;

    private String assetDetails;

    @Lob
    private byte[] comments;

    private String commentsContentType;
    private String modelNumber;

    private String serialNumber;

    private Set<AssetWarrantyDTO> assetWarranties = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PaymentInvoiceDTO> paymentInvoices = new HashSet<>();

    private Set<ServiceOutletDTO> serviceOutlets = new HashSet<>();

    private Set<SettlementDTO> settlements = new HashSet<>();

    private AssetCategoryDTO assetCategory;

    private Set<PurchaseOrderDTO> purchaseOrders = new HashSet<>();

    private Set<DeliveryNoteDTO> deliveryNotes = new HashSet<>();

    private Set<JobSheetDTO> jobSheets = new HashSet<>();

    private DealerDTO dealer;

    private Set<DealerDTO> designatedUsers = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    private ServiceOutletDTO mainServiceOutlet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public byte[] getComments() {
        return comments;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }

    public String getCommentsContentType() {
        return commentsContentType;
    }

    public void setCommentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Set<AssetWarrantyDTO> getAssetWarranties() {
        return assetWarranties;
    }

    public void setAssetWarranties(Set<AssetWarrantyDTO> assetWarranties) {
        this.assetWarranties = assetWarranties;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PaymentInvoiceDTO> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoiceDTO> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public Set<ServiceOutletDTO> getServiceOutlets() {
        return serviceOutlets;
    }

    public void setServiceOutlets(Set<ServiceOutletDTO> serviceOutlets) {
        this.serviceOutlets = serviceOutlets;
    }

    public Set<SettlementDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(Set<SettlementDTO> settlements) {
        this.settlements = settlements;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public Set<PurchaseOrderDTO> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrderDTO> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<DeliveryNoteDTO> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNoteDTO> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public Set<JobSheetDTO> getJobSheets() {
        return jobSheets;
    }

    public void setJobSheets(Set<JobSheetDTO> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    public Set<DealerDTO> getDesignatedUsers() {
        return designatedUsers;
    }

    public void setDesignatedUsers(Set<DealerDTO> designatedUsers) {
        this.designatedUsers = designatedUsers;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public ServiceOutletDTO getMainServiceOutlet() {
        return mainServiceOutlet;
    }

    public void setMainServiceOutlet(ServiceOutletDTO mainServiceOutlet) {
        this.mainServiceOutlet = mainServiceOutlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetAccessoryDTO)) {
            return false;
        }

        AssetAccessoryDTO assetAccessoryDTO = (AssetAccessoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetAccessoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAccessoryDTO{" +
            "id=" + getId() +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", comments='" + getComments() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", assetWarranties=" + getAssetWarranties() +
            ", placeholders=" + getPlaceholders() +
            ", paymentInvoices=" + getPaymentInvoices() +
            ", serviceOutlets=" + getServiceOutlets() +
            ", settlements=" + getSettlements() +
            ", assetCategory=" + getAssetCategory() +
            ", purchaseOrders=" + getPurchaseOrders() +
            ", deliveryNotes=" + getDeliveryNotes() +
            ", jobSheets=" + getJobSheets() +
            ", dealer=" + getDealer() +
            ", designatedUsers=" + getDesignatedUsers() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            ", mainServiceOutlet=" + getMainServiceOutlet() +
            "}";
    }
}
