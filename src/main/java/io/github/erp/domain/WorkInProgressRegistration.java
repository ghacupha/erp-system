package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

    @Column(name = "level_of_completion")
    private Double levelOfCompletion;

    @Column(name = "completed")
    private Boolean completed;

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__placeholder",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "workInProgressGroup",
            "settlementCurrency",
            "workProjectRegister",
            "businessDocuments",
            "assetAccessories",
            "assetWarranties",
            "invoice",
            "outletCode",
            "settlementTransaction",
            "purchaseOrder",
            "deliveryNote",
            "jobSheet",
            "dealer",
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

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__asset_accessory",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "asset_accessory_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "assetWarranties",
            "placeholders",
            "paymentInvoices",
            "serviceOutlets",
            "settlements",
            "assetCategory",
            "purchaseOrders",
            "deliveryNotes",
            "jobSheets",
            "dealer",
            "designatedUsers",
            "businessDocuments",
            "universallyUniqueMappings",
            "mainServiceOutlet",
        },
        allowSetters = true
    )
    private Set<AssetAccessory> assetAccessories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_registration__asset_warranty",
        joinColumns = @JoinColumn(name = "work_in_progress_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "asset_warranty_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders", "universallyUniqueMappings", "dealer", "warrantyAttachments" }, allowSetters = true)
    private Set<AssetWarranty> assetWarranties = new HashSet<>();

    @ManyToOne
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
    private PaymentInvoice invoice;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet outletCode;

    @ManyToOne
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
    private Settlement settlementTransaction;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "settlementCurrency", "placeholders", "signatories", "vendor", "businessDocuments" },
        allowSetters = true
    )
    private PurchaseOrder purchaseOrder;

    @ManyToOne
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
    private DeliveryNote deliveryNote;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels", "businessDocuments" },
        allowSetters = true
    )
    private JobSheet jobSheet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer dealer;

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

    public Double getLevelOfCompletion() {
        return this.levelOfCompletion;
    }

    public WorkInProgressRegistration levelOfCompletion(Double levelOfCompletion) {
        this.setLevelOfCompletion(levelOfCompletion);
        return this;
    }

    public void setLevelOfCompletion(Double levelOfCompletion) {
        this.levelOfCompletion = levelOfCompletion;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public WorkInProgressRegistration completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
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

    public Set<AssetAccessory> getAssetAccessories() {
        return this.assetAccessories;
    }

    public void setAssetAccessories(Set<AssetAccessory> assetAccessories) {
        this.assetAccessories = assetAccessories;
    }

    public WorkInProgressRegistration assetAccessories(Set<AssetAccessory> assetAccessories) {
        this.setAssetAccessories(assetAccessories);
        return this;
    }

    public WorkInProgressRegistration addAssetAccessory(AssetAccessory assetAccessory) {
        this.assetAccessories.add(assetAccessory);
        return this;
    }

    public WorkInProgressRegistration removeAssetAccessory(AssetAccessory assetAccessory) {
        this.assetAccessories.remove(assetAccessory);
        return this;
    }

    public Set<AssetWarranty> getAssetWarranties() {
        return this.assetWarranties;
    }

    public void setAssetWarranties(Set<AssetWarranty> assetWarranties) {
        this.assetWarranties = assetWarranties;
    }

    public WorkInProgressRegistration assetWarranties(Set<AssetWarranty> assetWarranties) {
        this.setAssetWarranties(assetWarranties);
        return this;
    }

    public WorkInProgressRegistration addAssetWarranty(AssetWarranty assetWarranty) {
        this.assetWarranties.add(assetWarranty);
        return this;
    }

    public WorkInProgressRegistration removeAssetWarranty(AssetWarranty assetWarranty) {
        this.assetWarranties.remove(assetWarranty);
        return this;
    }

    public PaymentInvoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(PaymentInvoice paymentInvoice) {
        this.invoice = paymentInvoice;
    }

    public WorkInProgressRegistration invoice(PaymentInvoice paymentInvoice) {
        this.setInvoice(paymentInvoice);
        return this;
    }

    public ServiceOutlet getOutletCode() {
        return this.outletCode;
    }

    public void setOutletCode(ServiceOutlet serviceOutlet) {
        this.outletCode = serviceOutlet;
    }

    public WorkInProgressRegistration outletCode(ServiceOutlet serviceOutlet) {
        this.setOutletCode(serviceOutlet);
        return this;
    }

    public Settlement getSettlementTransaction() {
        return this.settlementTransaction;
    }

    public void setSettlementTransaction(Settlement settlement) {
        this.settlementTransaction = settlement;
    }

    public WorkInProgressRegistration settlementTransaction(Settlement settlement) {
        this.setSettlementTransaction(settlement);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public WorkInProgressRegistration purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public WorkInProgressRegistration deliveryNote(DeliveryNote deliveryNote) {
        this.setDeliveryNote(deliveryNote);
        return this;
    }

    public JobSheet getJobSheet() {
        return this.jobSheet;
    }

    public void setJobSheet(JobSheet jobSheet) {
        this.jobSheet = jobSheet;
    }

    public WorkInProgressRegistration jobSheet(JobSheet jobSheet) {
        this.setJobSheet(jobSheet);
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
            ", levelOfCompletion=" + getLevelOfCompletion() +
            ", completed='" + getCompleted() + "'" +
            "}";
    }
}
