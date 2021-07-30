package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Invoice> ownedInvoices = new HashSet<>();

    @OneToOne

    @MapsId
    @JoinColumn(name = "id")
    private PaymentCalculation paymentCalculation;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private PaymentRequisition paymentRequisition;

    @ManyToMany(mappedBy = "payments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Dealer> dealers = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private TaxRule taxRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public Payment paymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public Payment paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public Payment paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return description;
    }

    public Payment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Invoice> getOwnedInvoices() {
        return ownedInvoices;
    }

    public Payment ownedInvoices(Set<Invoice> invoices) {
        this.ownedInvoices = invoices;
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
        this.ownedInvoices = invoices;
    }

    public PaymentCalculation getPaymentCalculation() {
        return paymentCalculation;
    }

    public Payment paymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
        return this;
    }

    public void setPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
    }

    public PaymentRequisition getPaymentRequisition() {
        return paymentRequisition;
    }

    public Payment paymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisition = paymentRequisition;
        return this;
    }

    public void setPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisition = paymentRequisition;
    }

    public Set<Dealer> getDealers() {
        return dealers;
    }

    public Payment dealers(Set<Dealer> dealers) {
        this.dealers = dealers;
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
        this.dealers = dealers;
    }

    public TaxRule getTaxRule() {
        return taxRule;
    }

    public Payment taxRule(TaxRule taxRule) {
        this.taxRule = taxRule;
        return this;
    }

    public void setTaxRule(TaxRule taxRule) {
        this.taxRule = taxRule;
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
        return 31;
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
