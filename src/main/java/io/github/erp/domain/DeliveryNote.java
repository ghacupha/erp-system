package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DeliveryNote.
 */
@Entity
@Table(name = "delivery_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deliverynote")
public class DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "delivery_note_number", nullable = false, unique = true)
    private String deliveryNoteNumber;

    @NotNull
    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @Column(name = "description")
    private String description;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "quantity")
    private Integer quantity;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @JoinTable(
        name = "rel_delivery_note__placeholder",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer receivedBy;

    @ManyToMany
    @JoinTable(
        name = "rel_delivery_note__delivery_stamps",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_stamps_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stampHolder", "placeholders" }, allowSetters = true)
    private Set<BusinessStamp> deliveryStamps = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "settlementCurrency", "placeholders", "signatories", "vendor" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer supplier;

    @ManyToMany
    @JoinTable(
        name = "rel_delivery_note__signatories",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "signatories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> signatories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryNote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryNoteNumber() {
        return this.deliveryNoteNumber;
    }

    public DeliveryNote deliveryNoteNumber(String deliveryNoteNumber) {
        this.setDeliveryNoteNumber(deliveryNoteNumber);
        return this;
    }

    public void setDeliveryNoteNumber(String deliveryNoteNumber) {
        this.deliveryNoteNumber = deliveryNoteNumber;
    }

    public LocalDate getDocumentDate() {
        return this.documentDate;
    }

    public DeliveryNote documentDate(LocalDate documentDate) {
        this.setDocumentDate(documentDate);
        return this;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getDescription() {
        return this.description;
    }

    public DeliveryNote description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public DeliveryNote serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public DeliveryNote quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public DeliveryNote remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public DeliveryNote placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public DeliveryNote addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public DeliveryNote removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Dealer getReceivedBy() {
        return this.receivedBy;
    }

    public void setReceivedBy(Dealer dealer) {
        this.receivedBy = dealer;
    }

    public DeliveryNote receivedBy(Dealer dealer) {
        this.setReceivedBy(dealer);
        return this;
    }

    public Set<BusinessStamp> getDeliveryStamps() {
        return this.deliveryStamps;
    }

    public void setDeliveryStamps(Set<BusinessStamp> businessStamps) {
        this.deliveryStamps = businessStamps;
    }

    public DeliveryNote deliveryStamps(Set<BusinessStamp> businessStamps) {
        this.setDeliveryStamps(businessStamps);
        return this;
    }

    public DeliveryNote addDeliveryStamps(BusinessStamp businessStamp) {
        this.deliveryStamps.add(businessStamp);
        return this;
    }

    public DeliveryNote removeDeliveryStamps(BusinessStamp businessStamp) {
        this.deliveryStamps.remove(businessStamp);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public DeliveryNote purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    public Dealer getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Dealer dealer) {
        this.supplier = dealer;
    }

    public DeliveryNote supplier(Dealer dealer) {
        this.setSupplier(dealer);
        return this;
    }

    public Set<Dealer> getSignatories() {
        return this.signatories;
    }

    public void setSignatories(Set<Dealer> dealers) {
        this.signatories = dealers;
    }

    public DeliveryNote signatories(Set<Dealer> dealers) {
        this.setSignatories(dealers);
        return this;
    }

    public DeliveryNote addSignatories(Dealer dealer) {
        this.signatories.add(dealer);
        return this;
    }

    public DeliveryNote removeSignatories(Dealer dealer) {
        this.signatories.remove(dealer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryNote)) {
            return false;
        }
        return id != null && id.equals(((DeliveryNote) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryNote{" +
            "id=" + getId() +
            ", deliveryNoteNumber='" + getDeliveryNoteNumber() + "'" +
            ", documentDate='" + getDocumentDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", quantity=" + getQuantity() +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}