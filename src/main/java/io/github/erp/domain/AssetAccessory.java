package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetAccessory.
 */
@Entity
@Table(name = "asset_accessory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetaccessory")
public class AssetAccessory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "asset_details")
    private String assetDetails;

    @Lob
    @Column(name = "comments")
    private byte[] comments;

    @Column(name = "comments_content_type")
    private String commentsContentType;

    @Column(name = "model_number")
    private String modelNumber;

    @Column(name = "serial_number")
    private String serialNumber;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__asset_warranty",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "asset_warranty_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders", "universallyUniqueMappings", "dealer", "warrantyAttachments" }, allowSetters = true)
    private Set<AssetWarranty> assetWarranties = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__placeholder",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__payment_invoices",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
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
        name = "rel_asset_accessory__service_outlet",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "service_outlet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private Set<ServiceOutlet> serviceOutlets = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_asset_accessory__settlement",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__purchase_order",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
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
        name = "rel_asset_accessory__delivery_note",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
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
        name = "rel_asset_accessory__job_sheet",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "job_sheet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels", "businessDocuments" },
        allowSetters = true
    )
    private Set<JobSheet> jobSheets = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer dealer;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__designated_users",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "designated_users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> designatedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_accessory__business_document",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
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
        name = "rel_asset_accessory__universally_unique_mapping",
        joinColumns = @JoinColumn(name = "asset_accessory_id"),
        inverseJoinColumns = @JoinColumn(name = "universally_unique_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> universallyUniqueMappings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet mainServiceOutlet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetAccessory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public AssetAccessory assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDetails() {
        return this.assetDetails;
    }

    public AssetAccessory assetDetails(String assetDetails) {
        this.setAssetDetails(assetDetails);
        return this;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public byte[] getComments() {
        return this.comments;
    }

    public AssetAccessory comments(byte[] comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }

    public String getCommentsContentType() {
        return this.commentsContentType;
    }

    public AssetAccessory commentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
        return this;
    }

    public void setCommentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public AssetAccessory modelNumber(String modelNumber) {
        this.setModelNumber(modelNumber);
        return this;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public AssetAccessory serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Set<AssetWarranty> getAssetWarranties() {
        return this.assetWarranties;
    }

    public void setAssetWarranties(Set<AssetWarranty> assetWarranties) {
        this.assetWarranties = assetWarranties;
    }

    public AssetAccessory assetWarranties(Set<AssetWarranty> assetWarranties) {
        this.setAssetWarranties(assetWarranties);
        return this;
    }

    public AssetAccessory addAssetWarranty(AssetWarranty assetWarranty) {
        this.assetWarranties.add(assetWarranty);
        return this;
    }

    public AssetAccessory removeAssetWarranty(AssetWarranty assetWarranty) {
        this.assetWarranties.remove(assetWarranty);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetAccessory placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AssetAccessory addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AssetAccessory removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public AssetAccessory paymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.setPaymentInvoices(paymentInvoices);
        return this;
    }

    public AssetAccessory addPaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.add(paymentInvoice);
        return this;
    }

    public AssetAccessory removePaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.remove(paymentInvoice);
        return this;
    }

    public Set<ServiceOutlet> getServiceOutlets() {
        return this.serviceOutlets;
    }

    public void setServiceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.serviceOutlets = serviceOutlets;
    }

    public AssetAccessory serviceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.setServiceOutlets(serviceOutlets);
        return this;
    }

    public AssetAccessory addServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.add(serviceOutlet);
        return this;
    }

    public AssetAccessory removeServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.remove(serviceOutlet);
        return this;
    }

    public Set<Settlement> getSettlements() {
        return this.settlements;
    }

    public void setSettlements(Set<Settlement> settlements) {
        this.settlements = settlements;
    }

    public AssetAccessory settlements(Set<Settlement> settlements) {
        this.setSettlements(settlements);
        return this;
    }

    public AssetAccessory addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
        return this;
    }

    public AssetAccessory removeSettlement(Settlement settlement) {
        this.settlements.remove(settlement);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public AssetAccessory assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public AssetAccessory purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public AssetAccessory addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        return this;
    }

    public AssetAccessory removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        return this;
    }

    public Set<DeliveryNote> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public AssetAccessory deliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.setDeliveryNotes(deliveryNotes);
        return this;
    }

    public AssetAccessory addDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.add(deliveryNote);
        return this;
    }

    public AssetAccessory removeDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.remove(deliveryNote);
        return this;
    }

    public Set<JobSheet> getJobSheets() {
        return this.jobSheets;
    }

    public void setJobSheets(Set<JobSheet> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public AssetAccessory jobSheets(Set<JobSheet> jobSheets) {
        this.setJobSheets(jobSheets);
        return this;
    }

    public AssetAccessory addJobSheet(JobSheet jobSheet) {
        this.jobSheets.add(jobSheet);
        return this;
    }

    public AssetAccessory removeJobSheet(JobSheet jobSheet) {
        this.jobSheets.remove(jobSheet);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public AssetAccessory dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public Set<Dealer> getDesignatedUsers() {
        return this.designatedUsers;
    }

    public void setDesignatedUsers(Set<Dealer> dealers) {
        this.designatedUsers = dealers;
    }

    public AssetAccessory designatedUsers(Set<Dealer> dealers) {
        this.setDesignatedUsers(dealers);
        return this;
    }

    public AssetAccessory addDesignatedUsers(Dealer dealer) {
        this.designatedUsers.add(dealer);
        return this;
    }

    public AssetAccessory removeDesignatedUsers(Dealer dealer) {
        this.designatedUsers.remove(dealer);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public AssetAccessory businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public AssetAccessory addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public AssetAccessory removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    public Set<UniversallyUniqueMapping> getUniversallyUniqueMappings() {
        return this.universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public AssetAccessory universallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setUniversallyUniqueMappings(universallyUniqueMappings);
        return this;
    }

    public AssetAccessory addUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.add(universallyUniqueMapping);
        return this;
    }

    public AssetAccessory removeUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.remove(universallyUniqueMapping);
        return this;
    }

    public ServiceOutlet getMainServiceOutlet() {
        return this.mainServiceOutlet;
    }

    public void setMainServiceOutlet(ServiceOutlet serviceOutlet) {
        this.mainServiceOutlet = serviceOutlet;
    }

    public AssetAccessory mainServiceOutlet(ServiceOutlet serviceOutlet) {
        this.setMainServiceOutlet(serviceOutlet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetAccessory)) {
            return false;
        }
        return id != null && id.equals(((AssetAccessory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAccessory{" +
            "id=" + getId() +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", comments='" + getComments() + "'" +
            ", commentsContentType='" + getCommentsContentType() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            "}";
    }
}
