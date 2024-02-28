package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WorkInProgressRegistration} entity.
 */
public class WorkInProgressRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    private String sequenceNumber;

    private String particulars;

    private BigDecimal instalmentAmount;

    @Lob
    private byte[] comments;

    private String commentsContentType;
    private Double levelOfCompletion;

    private Boolean completed;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private WorkInProgressRegistrationDTO workInProgressGroup;

    private SettlementCurrencyDTO settlementCurrency;

    private WorkProjectRegisterDTO workProjectRegister;

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<AssetAccessoryDTO> assetAccessories = new HashSet<>();

    private Set<AssetWarrantyDTO> assetWarranties = new HashSet<>();

    private PaymentInvoiceDTO invoice;

    private ServiceOutletDTO outletCode;

    private SettlementDTO settlementTransaction;

    private PurchaseOrderDTO purchaseOrder;

    private DeliveryNoteDTO deliveryNote;

    private JobSheetDTO jobSheet;

    private DealerDTO dealer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public BigDecimal getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
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

    public Double getLevelOfCompletion() {
        return levelOfCompletion;
    }

    public void setLevelOfCompletion(Double levelOfCompletion) {
        this.levelOfCompletion = levelOfCompletion;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public WorkInProgressRegistrationDTO getWorkInProgressGroup() {
        return workInProgressGroup;
    }

    public void setWorkInProgressGroup(WorkInProgressRegistrationDTO workInProgressGroup) {
        this.workInProgressGroup = workInProgressGroup;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public WorkProjectRegisterDTO getWorkProjectRegister() {
        return workProjectRegister;
    }

    public void setWorkProjectRegister(WorkProjectRegisterDTO workProjectRegister) {
        this.workProjectRegister = workProjectRegister;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public Set<AssetAccessoryDTO> getAssetAccessories() {
        return assetAccessories;
    }

    public void setAssetAccessories(Set<AssetAccessoryDTO> assetAccessories) {
        this.assetAccessories = assetAccessories;
    }

    public Set<AssetWarrantyDTO> getAssetWarranties() {
        return assetWarranties;
    }

    public void setAssetWarranties(Set<AssetWarrantyDTO> assetWarranties) {
        this.assetWarranties = assetWarranties;
    }

    public PaymentInvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(PaymentInvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public ServiceOutletDTO getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(ServiceOutletDTO outletCode) {
        this.outletCode = outletCode;
    }

    public SettlementDTO getSettlementTransaction() {
        return settlementTransaction;
    }

    public void setSettlementTransaction(SettlementDTO settlementTransaction) {
        this.settlementTransaction = settlementTransaction;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public DeliveryNoteDTO getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNoteDTO deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public JobSheetDTO getJobSheet() {
        return jobSheet;
    }

    public void setJobSheet(JobSheetDTO jobSheet) {
        this.jobSheet = jobSheet;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressRegistrationDTO)) {
            return false;
        }

        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = (WorkInProgressRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workInProgressRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressRegistrationDTO{" +
            "id=" + getId() +
            ", sequenceNumber='" + getSequenceNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", comments='" + getComments() + "'" +
            ", levelOfCompletion=" + getLevelOfCompletion() +
            ", completed='" + getCompleted() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", workInProgressGroup=" + getWorkInProgressGroup() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", workProjectRegister=" + getWorkProjectRegister() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", assetAccessories=" + getAssetAccessories() +
            ", assetWarranties=" + getAssetWarranties() +
            ", invoice=" + getInvoice() +
            ", outletCode=" + getOutletCode() +
            ", settlementTransaction=" + getSettlementTransaction() +
            ", purchaseOrder=" + getPurchaseOrder() +
            ", deliveryNote=" + getDeliveryNote() +
            ", jobSheet=" + getJobSheet() +
            ", dealer=" + getDealer() +
            "}";
    }
}
