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
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    private Long id;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment", "dealer" }, allowSetters = true)
    private Set<Invoice> ownedInvoices = new HashSet<>();

    @ManyToMany(mappedBy = "payments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payments" }, allowSetters = true)
    private Set<Dealer> dealers = new HashSet<>();

    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TaxRule taxRule;

    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private PaymentCategory paymentCategory;

    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private PaymentCalculation paymentCalculation;

    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private PaymentRequisition paymentRequisition;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment id(Long id) {
        this.id = id;
        return this;
    }

    public String getPaymentNumber() {
        return this.paymentNumber;
    }

    public Payment paymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public Payment paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public Payment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Invoice> getOwnedInvoices() {
        return this.ownedInvoices;
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

    public void setOwnedInvoices(Set<Invoice> invoices) {
        if (this.ownedInvoices != null) {
            this.ownedInvoices.forEach(i -> i.setPayment(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setPayment(this));
        }
        this.ownedInvoices = invoices;
    }

    public Set<Dealer> getDealers() {
        return this.dealers;
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

    public void setDealers(Set<Dealer> dealers) {
        if (this.dealers != null) {
            this.dealers.forEach(i -> i.removePayment(this));
        }
        if (dealers != null) {
            dealers.forEach(i -> i.addPayment(this));
        }
        this.dealers = dealers;
    }

    public TaxRule getTaxRule() {
        return this.taxRule;
    }

    public Payment taxRule(TaxRule taxRule) {
        this.setTaxRule(taxRule);
        return this;
    }

    public void setTaxRule(TaxRule taxRule) {
        this.taxRule = taxRule;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public Payment paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public PaymentCalculation getPaymentCalculation() {
        return this.paymentCalculation;
    }

    public Payment paymentCalculation(PaymentCalculation paymentCalculation) {
        this.setPaymentCalculation(paymentCalculation);
        return this;
    }

    public void setPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
    }

    public PaymentRequisition getPaymentRequisition() {
        return this.paymentRequisition;
    }

    public Payment paymentRequisition(PaymentRequisition paymentRequisition) {
        this.setPaymentRequisition(paymentRequisition);
        return this;
    }

    public void setPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisition = paymentRequisition;
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
            "}";
    }
}
