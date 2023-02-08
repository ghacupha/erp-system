package io.github.erp.domain;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SettlementRequisition.
 */
@Entity
@Table(name = "settlement_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "settlementrequisition")
public class SettlementRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private UUID serialNumber;

    @NotNull
    @Column(name = "time_of_requisition", nullable = false)
    private ZonedDateTime timeOfRequisition;

    @NotNull
    @Column(name = "requisition_number", nullable = false)
    private String requisitionNumber;

    @NotNull
    @Column(name = "payment_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser currentOwner;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser nativeOwner;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer nativeDepartment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer biller;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__payment_invoice",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_invoice_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "purchaseOrders",
            "placeholders",
            "paymentLabels",
            "settlementCurrency",
            "biller",
            "deliveryNotes",
            "jobSheets",
            "businessDocuments",
        },
        allowSetters = true
    )
    private Set<PaymentInvoice> paymentInvoices = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__delivery_note",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_note_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "receivedBy",
            "deliveryStamps",
            "purchaseOrder",
            "supplier",
            "signatories",
            "otherPurchaseOrders",
            "businessDocuments",
        },
        allowSetters = true
    )
    private Set<DeliveryNote> deliveryNotes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__job_sheet",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "job_sheet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels", "businessDocuments" },
        allowSetters = true
    )
    private Set<JobSheet> jobSheets = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__signatures",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "signatures_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> signatures = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__business_document",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__application_mapping",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "application_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> applicationMappings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__placeholder",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__settlement",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "settlement_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "settlementCurrency",
            "paymentLabels",
            "paymentCategory",
            "groupSettlement",
            "biller",
            "paymentInvoices",
            "signatories",
            "businessDocuments",
        },
        allowSetters = true
    )
    private Set<Settlement> settlements = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_requisition__other_beneficiaries",
        joinColumns = @JoinColumn(name = "settlement_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "other_beneficiaries_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> otherBeneficiaries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SettlementRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public SettlementRequisition description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getSerialNumber() {
        return this.serialNumber;
    }

    public SettlementRequisition serialNumber(UUID serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ZonedDateTime getTimeOfRequisition() {
        return this.timeOfRequisition;
    }

    public SettlementRequisition timeOfRequisition(ZonedDateTime timeOfRequisition) {
        this.setTimeOfRequisition(timeOfRequisition);
        return this;
    }

    public void setTimeOfRequisition(ZonedDateTime timeOfRequisition) {
        this.timeOfRequisition = timeOfRequisition;
    }

    public String getRequisitionNumber() {
        return this.requisitionNumber;
    }

    public SettlementRequisition requisitionNumber(String requisitionNumber) {
        this.setRequisitionNumber(requisitionNumber);
        return this;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public SettlementRequisition paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public SettlementRequisition paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public SettlementRequisition remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public SettlementRequisition settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public ApplicationUser getCurrentOwner() {
        return this.currentOwner;
    }

    public void setCurrentOwner(ApplicationUser applicationUser) {
        this.currentOwner = applicationUser;
    }

    public SettlementRequisition currentOwner(ApplicationUser applicationUser) {
        this.setCurrentOwner(applicationUser);
        return this;
    }

    public ApplicationUser getNativeOwner() {
        return this.nativeOwner;
    }

    public void setNativeOwner(ApplicationUser applicationUser) {
        this.nativeOwner = applicationUser;
    }

    public SettlementRequisition nativeOwner(ApplicationUser applicationUser) {
        this.setNativeOwner(applicationUser);
        return this;
    }

    public Dealer getNativeDepartment() {
        return this.nativeDepartment;
    }

    public void setNativeDepartment(Dealer dealer) {
        this.nativeDepartment = dealer;
    }

    public SettlementRequisition nativeDepartment(Dealer dealer) {
        this.setNativeDepartment(dealer);
        return this;
    }

    public Dealer getBiller() {
        return this.biller;
    }

    public void setBiller(Dealer dealer) {
        this.biller = dealer;
    }

    public SettlementRequisition biller(Dealer dealer) {
        this.setBiller(dealer);
        return this;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public SettlementRequisition paymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.setPaymentInvoices(paymentInvoices);
        return this;
    }

    public SettlementRequisition addPaymentInvoice(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.add(paymentInvoice);
        return this;
    }

    public SettlementRequisition removePaymentInvoice(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.remove(paymentInvoice);
        return this;
    }

    public Set<DeliveryNote> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public SettlementRequisition deliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.setDeliveryNotes(deliveryNotes);
        return this;
    }

    public SettlementRequisition addDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.add(deliveryNote);
        return this;
    }

    public SettlementRequisition removeDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.remove(deliveryNote);
        return this;
    }

    public Set<JobSheet> getJobSheets() {
        return this.jobSheets;
    }

    public void setJobSheets(Set<JobSheet> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public SettlementRequisition jobSheets(Set<JobSheet> jobSheets) {
        this.setJobSheets(jobSheets);
        return this;
    }

    public SettlementRequisition addJobSheet(JobSheet jobSheet) {
        this.jobSheets.add(jobSheet);
        return this;
    }

    public SettlementRequisition removeJobSheet(JobSheet jobSheet) {
        this.jobSheets.remove(jobSheet);
        return this;
    }

    public Set<Dealer> getSignatures() {
        return this.signatures;
    }

    public void setSignatures(Set<Dealer> dealers) {
        this.signatures = dealers;
    }

    public SettlementRequisition signatures(Set<Dealer> dealers) {
        this.setSignatures(dealers);
        return this;
    }

    public SettlementRequisition addSignatures(Dealer dealer) {
        this.signatures.add(dealer);
        return this;
    }

    public SettlementRequisition removeSignatures(Dealer dealer) {
        this.signatures.remove(dealer);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public SettlementRequisition businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public SettlementRequisition addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public SettlementRequisition removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    public Set<UniversallyUniqueMapping> getApplicationMappings() {
        return this.applicationMappings;
    }

    public void setApplicationMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.applicationMappings = universallyUniqueMappings;
    }

    public SettlementRequisition applicationMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setApplicationMappings(universallyUniqueMappings);
        return this;
    }

    public SettlementRequisition addApplicationMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationMappings.add(universallyUniqueMapping);
        return this;
    }

    public SettlementRequisition removeApplicationMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationMappings.remove(universallyUniqueMapping);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SettlementRequisition placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SettlementRequisition addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SettlementRequisition removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<Settlement> getSettlements() {
        return this.settlements;
    }

    public void setSettlements(Set<Settlement> settlements) {
        this.settlements = settlements;
    }

    public SettlementRequisition settlements(Set<Settlement> settlements) {
        this.setSettlements(settlements);
        return this;
    }

    public SettlementRequisition addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
        return this;
    }

    public SettlementRequisition removeSettlement(Settlement settlement) {
        this.settlements.remove(settlement);
        return this;
    }

    public Set<Dealer> getOtherBeneficiaries() {
        return this.otherBeneficiaries;
    }

    public void setOtherBeneficiaries(Set<Dealer> dealers) {
        this.otherBeneficiaries = dealers;
    }

    public SettlementRequisition otherBeneficiaries(Set<Dealer> dealers) {
        this.setOtherBeneficiaries(dealers);
        return this;
    }

    public SettlementRequisition addOtherBeneficiaries(Dealer dealer) {
        this.otherBeneficiaries.add(dealer);
        return this;
    }

    public SettlementRequisition removeOtherBeneficiaries(Dealer dealer) {
        this.otherBeneficiaries.remove(dealer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementRequisition)) {
            return false;
        }
        return id != null && id.equals(((SettlementRequisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementRequisition{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", timeOfRequisition='" + getTimeOfRequisition() + "'" +
            ", requisitionNumber='" + getRequisitionNumber() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
