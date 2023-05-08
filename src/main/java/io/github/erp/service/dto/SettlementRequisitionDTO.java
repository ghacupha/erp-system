package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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

import io.github.erp.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SettlementRequisition} entity.
 */
public class SettlementRequisitionDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private UUID serialNumber;

    @NotNull
    private ZonedDateTime timeOfRequisition;

    @NotNull
    private String requisitionNumber;

    @NotNull
    private BigDecimal paymentAmount;

    @NotNull
    private PaymentStatus paymentStatus;

    private String transactionId;

    private LocalDate transactionDate;

    private SettlementCurrencyDTO settlementCurrency;

    private ApplicationUserDTO currentOwner;

    private ApplicationUserDTO nativeOwner;

    private DealerDTO nativeDepartment;

    private DealerDTO biller;

    private Set<PaymentInvoiceDTO> paymentInvoices = new HashSet<>();

    private Set<DeliveryNoteDTO> deliveryNotes = new HashSet<>();

    private Set<JobSheetDTO> jobSheets = new HashSet<>();

    private Set<DealerDTO> signatures = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> applicationMappings = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<SettlementDTO> settlements = new HashSet<>();

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

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ZonedDateTime getTimeOfRequisition() {
        return timeOfRequisition;
    }

    public void setTimeOfRequisition(ZonedDateTime timeOfRequisition) {
        this.timeOfRequisition = timeOfRequisition;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public ApplicationUserDTO getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(ApplicationUserDTO currentOwner) {
        this.currentOwner = currentOwner;
    }

    public ApplicationUserDTO getNativeOwner() {
        return nativeOwner;
    }

    public void setNativeOwner(ApplicationUserDTO nativeOwner) {
        this.nativeOwner = nativeOwner;
    }

    public DealerDTO getNativeDepartment() {
        return nativeDepartment;
    }

    public void setNativeDepartment(DealerDTO nativeDepartment) {
        this.nativeDepartment = nativeDepartment;
    }

    public DealerDTO getBiller() {
        return biller;
    }

    public void setBiller(DealerDTO biller) {
        this.biller = biller;
    }

    public Set<PaymentInvoiceDTO> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoiceDTO> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
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

    public Set<DealerDTO> getSignatures() {
        return signatures;
    }

    public void setSignatures(Set<DealerDTO> signatures) {
        this.signatures = signatures;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public Set<UniversallyUniqueMappingDTO> getApplicationMappings() {
        return applicationMappings;
    }

    public void setApplicationMappings(Set<UniversallyUniqueMappingDTO> applicationMappings) {
        this.applicationMappings = applicationMappings;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<SettlementDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(Set<SettlementDTO> settlements) {
        this.settlements = settlements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementRequisitionDTO)) {
            return false;
        }

        SettlementRequisitionDTO settlementRequisitionDTO = (SettlementRequisitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settlementRequisitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementRequisitionDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", timeOfRequisition='" + getTimeOfRequisition() + "'" +
            ", requisitionNumber='" + getRequisitionNumber() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", currentOwner=" + getCurrentOwner() +
            ", nativeOwner=" + getNativeOwner() +
            ", nativeDepartment=" + getNativeDepartment() +
            ", biller=" + getBiller() +
            ", paymentInvoices=" + getPaymentInvoices() +
            ", deliveryNotes=" + getDeliveryNotes() +
            ", jobSheets=" + getJobSheets() +
            ", signatures=" + getSignatures() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", applicationMappings=" + getApplicationMappings() +
            ", placeholders=" + getPlaceholders() +
            ", settlements=" + getSettlements() +
            "}";
    }
}
