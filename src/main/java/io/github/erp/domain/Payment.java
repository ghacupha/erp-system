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

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "disbursement_cost", precision = 21, scale = 2)
    private BigDecimal disbursementCost;

    @Column(name = "vatable_amount", precision = 21, scale = 2)
    private BigDecimal vatableAmount;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_currency", nullable = false)
    private CurrencyTypes settlementCurrency;

    @NotNull
    @DecimalMin(value = "1.00")
    @Column(name = "conversion_rate", nullable = false)
    private Double conversionRate;

    @ManyToMany
    @JoinTable(
        name = "rel_payment__payment_label",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "containingPaymentLabel",
            "placeholders",
            "paymentCalculations",
            "paymentCategories",
            "paymentRequisitions",
            "payments",
            "invoices",
            "dealers",
            "signedPayments",
        },
        allowSetters = true
    )
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "dealer", "placeholders" }, allowSetters = true)
    private Set<Invoice> ownedInvoices = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_payment__dealer",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "dealer_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "payments", "paymentRequisitions", "placeholders" },
        allowSetters = true
    )
    private Set<Dealer> dealers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "payments", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payments", "placeholders" }, allowSetters = true)
    private TaxRule taxRule;

    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "paymentCategory", "placeholders" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PaymentCalculation paymentCalculation;

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

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup",
        },
        allowSetters = true
    )
    private Payment paymentGroup;

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

    public BigDecimal getInvoicedAmount() {
        return this.invoicedAmount;
    }

    public Payment invoicedAmount(BigDecimal invoicedAmount) {
        this.setInvoicedAmount(invoicedAmount);
        return this;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getDisbursementCost() {
        return this.disbursementCost;
    }

    public Payment disbursementCost(BigDecimal disbursementCost) {
        this.setDisbursementCost(disbursementCost);
        return this;
    }

    public void setDisbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimal getVatableAmount() {
        return this.vatableAmount;
    }

    public Payment vatableAmount(BigDecimal vatableAmount) {
        this.setVatableAmount(vatableAmount);
        return this;
    }

    public void setVatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
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

    public CurrencyTypes getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public Payment settlementCurrency(CurrencyTypes settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public void setSettlementCurrency(CurrencyTypes settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
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

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Payment paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public Payment addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        paymentLabel.getPayments().add(this);
        return this;
    }

    public Payment removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        paymentLabel.getPayments().remove(this);
        return this;
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

    public Payment getPaymentGroup() {
        return this.paymentGroup;
    }

    public void setPaymentGroup(Payment payment) {
        this.paymentGroup = payment;
    }

    public Payment paymentGroup(Payment payment) {
        this.setPaymentGroup(payment);
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
            ", invoicedAmount=" + getInvoicedAmount() +
            ", disbursementCost=" + getDisbursementCost() +
            ", vatableAmount=" + getVatableAmount() +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", settlementCurrency='" + getSettlementCurrency() + "'" +
            ", conversionRate=" + getConversionRate() +
            "}";
    }
}
