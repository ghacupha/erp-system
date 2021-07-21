package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
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

    @Column(name = "dealer_name")
    private String dealerName;

    @OneToMany(mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<Invoice> ownedInvoices = new HashSet<>();

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

    public String getDealerName() {
        return this.dealerName;
    }

    public Payment dealerName(String dealerName) {
        this.dealerName = dealerName;
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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
            ", dealerName='" + getDealerName() + "'" +
            "}";
    }
}
