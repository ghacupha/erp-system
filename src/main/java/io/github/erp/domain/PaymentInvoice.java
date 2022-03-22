package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A PaymentInvoice.
 */
@Entity
@Table(name = "payment_invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentinvoice")
public class PaymentInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "invoice_amount", precision = 21, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_invoice__purchase_order",
        joinColumns = @JoinColumn(name = "payment_invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "purchase_order_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "settlementCurrency", "placeholders", "signatories", "vendor" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_payment_invoice__placeholder",
        joinColumns = @JoinColumn(name = "payment_invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_payment_invoice__payment_label",
        joinColumns = @JoinColumn(name = "payment_invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer biller;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentInvoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public PaymentInvoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public PaymentInvoice invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public PaymentInvoice invoiceAmount(BigDecimal invoiceAmount) {
        this.setInvoiceAmount(invoiceAmount);
        return this;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public PaymentInvoice fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public PaymentInvoice compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public PaymentInvoice remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public PaymentInvoice purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public PaymentInvoice addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        return this;
    }

    public PaymentInvoice removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentInvoice placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentInvoice addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PaymentInvoice removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentInvoice paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public PaymentInvoice addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public PaymentInvoice removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public PaymentInvoice settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public Dealer getBiller() {
        return this.biller;
    }

    public void setBiller(Dealer dealer) {
        this.biller = dealer;
    }

    public PaymentInvoice biller(Dealer dealer) {
        this.setBiller(dealer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentInvoice)) {
            return false;
        }
        return id != null && id.equals(((PaymentInvoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentInvoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}