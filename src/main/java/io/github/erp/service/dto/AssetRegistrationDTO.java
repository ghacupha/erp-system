package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetRegistration} entity.
 */
public class AssetRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    private String assetNumber;

    @NotNull
    private String assetTag;

    private String assetDetails;

    @NotNull
    private BigDecimal assetCost;

    @Lob
    private byte[] comments;

    private String commentsContentType;
    private String modelNumber;

    private String serialNumber;

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

    private SettlementCurrencyDTO settlementCurrency;

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
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

    public BigDecimal getAssetCost() {
        return assetCost;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
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

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRegistrationDTO)) {
            return false;
        }

        AssetRegistrationDTO assetRegistrationDTO = (AssetRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegistrationDTO{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", assetCost=" + getAssetCost() +
            ", comments='" + getComments() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
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
            ", settlementCurrency=" + getSettlementCurrency() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            "}";
    }
}
