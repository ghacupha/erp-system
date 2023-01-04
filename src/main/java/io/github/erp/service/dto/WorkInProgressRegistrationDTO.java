package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PaymentInvoiceDTO> paymentInvoices = new HashSet<>();

    private Set<ServiceOutletDTO> serviceOutlets = new HashSet<>();

    private Set<SettlementDTO> settlements = new HashSet<>();

    private Set<PurchaseOrderDTO> purchaseOrders = new HashSet<>();

    private Set<DeliveryNoteDTO> deliveryNotes = new HashSet<>();

    private Set<JobSheetDTO> jobSheets = new HashSet<>();

    private DealerDTO dealer;

    private WorkInProgressRegistrationDTO workInProgressGroup;

    private SettlementCurrencyDTO settlementCurrency;

    private WorkProjectRegisterDTO workProjectRegister;

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

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
            ", placeholders=" + getPlaceholders() +
            ", paymentInvoices=" + getPaymentInvoices() +
            ", serviceOutlets=" + getServiceOutlets() +
            ", settlements=" + getSettlements() +
            ", purchaseOrders=" + getPurchaseOrders() +
            ", deliveryNotes=" + getDeliveryNotes() +
            ", jobSheets=" + getJobSheets() +
            ", dealer=" + getDealer() +
            ", workInProgressGroup=" + getWorkInProgressGroup() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", workProjectRegister=" + getWorkProjectRegister() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
