package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DeliveryNote} entity.
 */
public class DeliveryNoteDTO implements Serializable {

    private Long id;

    @NotNull
    private String deliveryNoteNumber;

    @NotNull
    private LocalDate documentDate;

    private String description;

    private String serialNumber;

    private Integer quantity;

    @Lob
    private String remarks;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private DealerDTO receivedBy;

    private Set<BusinessStampDTO> deliveryStamps = new HashSet<>();

    private PurchaseOrderDTO purchaseOrder;

    private DealerDTO supplier;

    private Set<DealerDTO> signatories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryNoteNumber() {
        return deliveryNoteNumber;
    }

    public void setDeliveryNoteNumber(String deliveryNoteNumber) {
        this.deliveryNoteNumber = deliveryNoteNumber;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public DealerDTO getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(DealerDTO receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Set<BusinessStampDTO> getDeliveryStamps() {
        return deliveryStamps;
    }

    public void setDeliveryStamps(Set<BusinessStampDTO> deliveryStamps) {
        this.deliveryStamps = deliveryStamps;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public DealerDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(DealerDTO supplier) {
        this.supplier = supplier;
    }

    public Set<DealerDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<DealerDTO> signatories) {
        this.signatories = signatories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryNoteDTO)) {
            return false;
        }

        DeliveryNoteDTO deliveryNoteDTO = (DeliveryNoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryNoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryNoteDTO{" +
            "id=" + getId() +
            ", deliveryNoteNumber='" + getDeliveryNoteNumber() + "'" +
            ", documentDate='" + getDocumentDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", quantity=" + getQuantity() +
            ", remarks='" + getRemarks() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", receivedBy=" + getReceivedBy() +
            ", deliveryStamps=" + getDeliveryStamps() +
            ", purchaseOrder=" + getPurchaseOrder() +
            ", supplier=" + getSupplier() +
            ", signatories=" + getSignatories() +
            "}";
    }
}
