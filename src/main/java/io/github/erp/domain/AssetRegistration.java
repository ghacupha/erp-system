package io.github.erp.domain;

/*-
 * Erp System - Mark II No 26 (Baruch Series) Server ver 0.0.6-SNAPSHOT
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
 * A AssetRegistration.
 */
@Entity
@Table(name = "asset_registration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetregistration")
public class AssetRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "asset_number", nullable = false, unique = true)
    private String assetNumber;

    @NotNull
    @Column(name = "asset_tag", nullable = false, unique = true)
    private String assetTag;

    @Column(name = "asset_details")
    private String assetDetails;

    @NotNull
    @Column(name = "asset_cost", precision = 21, scale = 2, nullable = false)
    private BigDecimal assetCost;

    @Lob
    @Column(name = "comments")
    private byte[] comments;

    @Column(name = "comments_content_type")
    private String commentsContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_registration__placeholder",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_registration__payment_invoices",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_invoices_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "purchaseOrders", "placeholders", "paymentLabels", "settlementCurrency", "biller", "deliveryNotes", "jobSheets" },
        allowSetters = true
    )
    private Set<PaymentInvoice> paymentInvoices = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_asset_registration__service_outlet",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
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
        name = "rel_asset_registration__settlement",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
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
        name = "rel_asset_registration__purchase_order",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "purchase_order_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "settlementCurrency", "placeholders", "signatories", "vendor" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_registration__delivery_note",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_note_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "placeholders", "receivedBy", "deliveryStamps", "purchaseOrder", "supplier", "signatories", "otherPurchaseOrders" },
        allowSetters = true
    )
    private Set<DeliveryNote> deliveryNotes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asset_registration__job_sheet",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "job_sheet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels" },
        allowSetters = true
    )
    private Set<JobSheet> jobSheets = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer dealer;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_registration__designated_users",
        joinColumns = @JoinColumn(name = "asset_registration_id"),
        inverseJoinColumns = @JoinColumn(name = "designated_users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> designatedUsers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public AssetRegistration assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public AssetRegistration assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDetails() {
        return this.assetDetails;
    }

    public AssetRegistration assetDetails(String assetDetails) {
        this.setAssetDetails(assetDetails);
        return this;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimal getAssetCost() {
        return this.assetCost;
    }

    public AssetRegistration assetCost(BigDecimal assetCost) {
        this.setAssetCost(assetCost);
        return this;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public byte[] getComments() {
        return this.comments;
    }

    public AssetRegistration comments(byte[] comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }

    public String getCommentsContentType() {
        return this.commentsContentType;
    }

    public AssetRegistration commentsContentType(String commentsContentType) {
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

    public AssetRegistration placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AssetRegistration addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AssetRegistration removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public AssetRegistration paymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.setPaymentInvoices(paymentInvoices);
        return this;
    }

    public AssetRegistration addPaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.add(paymentInvoice);
        return this;
    }

    public AssetRegistration removePaymentInvoices(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.remove(paymentInvoice);
        return this;
    }

    public Set<ServiceOutlet> getServiceOutlets() {
        return this.serviceOutlets;
    }

    public void setServiceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.serviceOutlets = serviceOutlets;
    }

    public AssetRegistration serviceOutlets(Set<ServiceOutlet> serviceOutlets) {
        this.setServiceOutlets(serviceOutlets);
        return this;
    }

    public AssetRegistration addServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.add(serviceOutlet);
        return this;
    }

    public AssetRegistration removeServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlets.remove(serviceOutlet);
        return this;
    }

    public Set<Settlement> getSettlements() {
        return this.settlements;
    }

    public void setSettlements(Set<Settlement> settlements) {
        this.settlements = settlements;
    }

    public AssetRegistration settlements(Set<Settlement> settlements) {
        this.setSettlements(settlements);
        return this;
    }

    public AssetRegistration addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
        return this;
    }

    public AssetRegistration removeSettlement(Settlement settlement) {
        this.settlements.remove(settlement);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public AssetRegistration assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public AssetRegistration purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public AssetRegistration addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        return this;
    }

    public AssetRegistration removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        return this;
    }

    public Set<DeliveryNote> getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public AssetRegistration deliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.setDeliveryNotes(deliveryNotes);
        return this;
    }

    public AssetRegistration addDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.add(deliveryNote);
        return this;
    }

    public AssetRegistration removeDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNotes.remove(deliveryNote);
        return this;
    }

    public Set<JobSheet> getJobSheets() {
        return this.jobSheets;
    }

    public void setJobSheets(Set<JobSheet> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public AssetRegistration jobSheets(Set<JobSheet> jobSheets) {
        this.setJobSheets(jobSheets);
        return this;
    }

    public AssetRegistration addJobSheet(JobSheet jobSheet) {
        this.jobSheets.add(jobSheet);
        return this;
    }

    public AssetRegistration removeJobSheet(JobSheet jobSheet) {
        this.jobSheets.remove(jobSheet);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public AssetRegistration dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public Set<Dealer> getDesignatedUsers() {
        return this.designatedUsers;
    }

    public void setDesignatedUsers(Set<Dealer> dealers) {
        this.designatedUsers = dealers;
    }

    public AssetRegistration designatedUsers(Set<Dealer> dealers) {
        this.setDesignatedUsers(dealers);
        return this;
    }

    public AssetRegistration addDesignatedUsers(Dealer dealer) {
        this.designatedUsers.add(dealer);
        return this;
    }

    public AssetRegistration removeDesignatedUsers(Dealer dealer) {
        this.designatedUsers.remove(dealer);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public AssetRegistration settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRegistration)) {
            return false;
        }
        return id != null && id.equals(((AssetRegistration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegistration{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", assetCost=" + getAssetCost() +
            ", comments='" + getComments() + "'" +
            ", commentsContentType='" + getCommentsContentType() + "'" +
            "}";
    }
}
