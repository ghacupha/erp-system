package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyTypes currency;

    @NotNull
    @DecimalMin(value = "1.00")
    @Column(name = "conversion_rate", nullable = false)
    private Double conversionRate;

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment", "dealer", "placeholders" }, allowSetters = true)
    private Set<Invoice> ownedInvoices = new HashSet<>();

    @ManyToMany(mappedBy = "payments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payments", "paymentRequisitions", "placeholders" }, allowSetters = true)
    private Set<Dealer> dealers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentCalculations", "payments", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

    @JsonIgnoreProperties(value = { "payment", "placeholders" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TaxRule taxRule;

    @JsonIgnoreProperties(value = { "payment", "paymentCategory", "placeholders" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PaymentCalculation paymentCalculation;

    @JsonIgnoreProperties(value = { "payment", "dealer", "placeholders" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PaymentRequisition paymentRequisition;

    @ManyToMany
    @JoinTable(
        name = "rel_payment__placeholder",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "containingPlaceholder",
            "dealers",
            "fileTypes",
            "fileUploads",
            "fixedAssetAcquisitions",
            "fixedAssetDepreciations",
            "fixedAssetNetBookValues",
            "invoices",
            "messageTokens",
            "payments",
            "paymentCalculations",
            "paymentRequisitions",
            "paymentCategories",
            "taxReferences",
            "taxRules",
        },
        allowSetters = true
    )
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return this.paymentNumber;
    }

    public Payment paymentNumber(String paymentNumber) {
        this.setPaymentNumber(paymentNumber);
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public Payment paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public Payment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyTypes getCurrency() {
        return this.currency;
    }

    public Payment currency(CurrencyTypes currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(CurrencyTypes currency) {
        this.currency = currency;
    }

    public Double getConversionRate() {
        return this.conversionRate;
    }

    public Payment conversionRate(Double conversionRate) {
        this.setConversionRate(conversionRate);
        return this;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Set<Invoice> getOwnedInvoices() {
        return this.ownedInvoices;
    }

    public void setOwnedInvoices(Set<Invoice> invoices) {
        if (this.ownedInvoices != null) {
            this.ownedInvoices.forEach(i -> i.setPayment(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setPayment(this));
        }
        this.ownedInvoices = invoices;
    }

    public Payment ownedInvoices(Set<Invoice> invoices) {
        this.setOwnedInvoices(invoices);
        return this;
    }

    public Payment addOwnedInvoice(Invoice invoice) {
        this.ownedInvoices.add(invoice);
        invoice.setPayment(this);
        return this;
    }

    public Payment removeOwnedInvoice(Invoice invoice) {
        this.ownedInvoices.remove(invoice);
        invoice.setPayment(null);
        return this;
    }

    public Set<Dealer> getDealers() {
        return this.dealers;
    }

    public void setDealers(Set<Dealer> dealers) {
        if (this.dealers != null) {
            this.dealers.forEach(i -> i.removePayment(this));
        }
        if (dealers != null) {
            dealers.forEach(i -> i.addPayment(this));
        }
        this.dealers = dealers;
    }

    public Payment dealers(Set<Dealer> dealers) {
        this.setDealers(dealers);
        return this;
    }

    public Payment addDealer(Dealer dealer) {
        this.dealers.add(dealer);
        dealer.getPayments().add(this);
        return this;
    }

    public Payment removeDealer(Dealer dealer) {
        this.dealers.remove(dealer);
        dealer.getPayments().remove(this);
        return this;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Payment paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public TaxRule getTaxRule() {
        return this.taxRule;
    }

    public void setTaxRule(TaxRule taxRule) {
        this.taxRule = taxRule;
    }

    public Payment taxRule(TaxRule taxRule) {
        this.setTaxRule(taxRule);
        return this;
    }

    public PaymentCalculation getPaymentCalculation() {
        return this.paymentCalculation;
    }

    public void setPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
    }

    public Payment paymentCalculation(PaymentCalculation paymentCalculation) {
        this.setPaymentCalculation(paymentCalculation);
        return this;
    }

    public PaymentRequisition getPaymentRequisition() {
        return this.paymentRequisition;
    }

    public void setPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisition = paymentRequisition;
    }

    public Payment paymentRequisition(PaymentRequisition paymentRequisition) {
        this.setPaymentRequisition(paymentRequisition);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Payment placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public Payment addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        placeholder.getPayments().add(this);
        return this;
    }

    public Payment removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        placeholder.getPayments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", conversionRate=" + getConversionRate() +
            "}";
    }
}
