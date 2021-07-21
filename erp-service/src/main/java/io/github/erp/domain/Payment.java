package io.github.erp.domain;

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

    @Column(name = "payment_category")
    private String paymentCategory;

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Invoice> ownedInvoices = new HashSet<>();

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

    public String getDealerName() {
        return dealerName;
    }

    public Payment dealerName(String dealerName) {
        this.dealerName = dealerName;
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public Payment paymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
        return this;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
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
            ", dealerName='" + getDealerName() + "'" +
            ", paymentCategory='" + getPaymentCategory() + "'" +
            "}";
    }
}
