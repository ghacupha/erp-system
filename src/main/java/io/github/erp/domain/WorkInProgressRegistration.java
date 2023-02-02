package io.github.erp.domain;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.5.0
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkInProgressRegistration.
 */
@Entity
@Table(name = "work_in_progress_registration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogressregistration")
public class WorkInProgressRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sequence_number", nullable = false, unique = true)
    private String sequenceNumber;

    @Column(name = "particulars")
    private String particulars;

    @Column(name = "instalment_amount", precision = 21, scale = 2)
    private BigDecimal instalmentAmount;

    @Lob
    @Column(name = "comments")
    private byte[] comments;

    @Column(name = "comments_content_type")
    private String commentsContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__placeholder",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__payment_invoices",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_invoices_id")
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
        name = "rel_work_in_progress_registration__service_outlet",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "service_outlet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private Set<ServiceOutlet> serviceOutlets = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__settlement",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
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
        name = "rel_work_in_progress_registration__purchase_order",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "purchase_order_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "settlementCurrency", "placeholders", "signatories", "vendor", "businessDocuments" },
        allowSetters = true
    )
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__delivery_note",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
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
        name = "rel_work_in_progress_registration__job_sheet",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "job_sheet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels", "businessDocuments" },
        allowSetters = true
    )
    private Set<JobSheet> jobSheets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer dealer;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "paymentInvoices",
            "serviceOutlets",
            "settlements",
            "purchaseOrders",
            "deliveryNotes",
            "jobSheets",
            "dealer",
            "workInProgressGroup",
            "settlementCurrency",
            "workProjectRegister",
            "businessDocuments",
        },
        allowSetters = true
    )
    private WorkInProgressRegistration workInProgressGroup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dealers", "settlementCurrency", "placeholders", "businessDocuments" }, allowSetters = true)
    private WorkProjectRegister workProjectRegister;

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__business_document",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }

    public WorkInProgressRegistration sequenceNumber(String sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public WorkInProgressRegistration particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public BigDecimal getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public WorkInProgressRegistration instalmentAmount(BigDecimal instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public byte[] getComments() {
        return this.comments;
    }

    public WorkInProgressRegistration comments(byte[] comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }

    public String getCommentsContentType() {
        return this.commentsContentType;
    }

    public WorkInProgressRegistration commentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
        return this;
    }

    public void setCommentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public WorkInProgressRegistration placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public WorkInProgressRegistration addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public WorkInProgressRegistration removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public WorkInProgressRegistration paymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.setPaymentInvoices(paymentInvoices);
        return this;
    }

    public WorkInProgressRegistration addPaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.add(paymentInvoice);
        return this;
    }

    public WorkInProgressRegistration removePaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.remove(paymentInvoice);
        return this;
    }

    public Set<ServiceOutlet> getServiceOutlets() {
        return this.serviceOutlets;
    }

    public void setServiceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.serviceOutlets = serviceOutlets;
    }

    public WorkInProgressRegistration serviceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.setServiceOutlets(serviceOutlets);
        return this;
    }

    public WorkInProgressRegistration addServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.add(serviceOutlet);
        return this;
    }

    public WorkInProgressRegistration removeServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.remove(serviceOutlet);
        return this;
    }

    public Set<Settlement> getSettlements() {
        return this.settlements;
    }

    public void setSettlements(Set<Settlement> settlements) {
        this.settlements = settlements;
    }

    public WorkInProgressRegistration settlements(Set<Settlement> settlements) {
        this.setSettlements(settlements);
        return this;
    }

    public WorkInProgressRegistration addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
        return this;
    }

    public WorkInProgressRegistration removeSettlement(Settlement settlement) {
        this.settlements.remove(settlement);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public WorkInProgressRegistration purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public WorkInProgressRegistration addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        return this;
    }

    public WorkInProgressRegistration removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        return this;
    }

    public Set<DeliveryNote> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public WorkInProgressRegistration deliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.setDeliveryNotes(deliveryNotes);
        return this;
    }

    public WorkInProgressRegistration addDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.add(deliveryNote);
        return this;
    }

    public WorkInProgressRegistration removeDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.remove(deliveryNote);
        return this;
    }

    public Set<JobSheet> getJobSheets() {
        return this.jobSheets;
    }

    public void setJobSheets(Set<JobSheet> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public WorkInProgressRegistration jobSheets(Set<JobSheet> jobSheets) {
        this.setJobSheets(jobSheets);
        return this;
    }

    public WorkInProgressRegistration addJobSheet(JobSheet jobSheet) {
        this.jobSheets.add(jobSheet);
        return this;
    }

    public WorkInProgressRegistration removeJobSheet(JobSheet jobSheet) {
        this.jobSheets.remove(jobSheet);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public WorkInProgressRegistration dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public WorkInProgressRegistration getWorkInProgressGroup() {
        return this.workInProgressGroup;
    }

    public void setWorkInProgressGroup(WorkInProgressRegistration workInProgressRegistration) {
        this.workInProgressGroup = workInProgressRegistration;
    }

    public WorkInProgressRegistration workInProgressGroup(WorkInProgressRegistration workInProgressRegistration) {
        this.setWorkInProgressGroup(workInProgressRegistration);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public WorkInProgressRegistration settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public WorkProjectRegister getWorkProjectRegister() {
        return this.workProjectRegister;
    }

    public void setWorkProjectRegister(WorkProjectRegister workProjectRegister) {
        this.workProjectRegister = workProjectRegister;
    }

    public WorkInProgressRegistration workProjectRegister(WorkProjectRegister workProjectRegister) {
        this.setWorkProjectRegister(workProjectRegister);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public WorkInProgressRegistration businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public WorkInProgressRegistration addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public WorkInProgressRegistration removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressRegistration)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressRegistration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressRegistration{" +
            "id=" + getId() +
            ", sequenceNumber='" + getSequenceNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", comments='" + getComments() + "'" +
            ", commentsContentType='" + getCommentsContentType() + "'" +
            "}";
    }
}
