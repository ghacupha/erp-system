package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A PaymentRequisition.
 */
@Entity
@Table(name = "payment_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentrequisition")
public class PaymentRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "disbursement_cost", precision = 21, scale = 2)
    private BigDecimal disbursementCost;

    @Column(name = "vatable_amount", precision = 21, scale = 2)
    private BigDecimal vatableAmount;

    @JsonIgnoreProperties(
        value = { "ownedInvoices", "dealers", "taxRule", "paymentCalculation", "paymentRequisition" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "paymentRequisition")
    private Payment payment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payments", "paymentRequisitions" }, allowSetters = true)
    private Dealer dealer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentRequisition id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getInvoicedAmount() {
        return this.invoicedAmount;
    }

    public PaymentRequisition invoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
        return this;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getDisbursementCost() {
        return this.disbursementCost;
    }

    public PaymentRequisition disbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
        return this;
    }

    public void setDisbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimal getVatableAmount() {
        return this.vatableAmount;
    }

    public PaymentRequisition vatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
        return this;
    }

    public void setVatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public PaymentRequisition payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setPaymentRequisition(null);
        }
        if (payment != null) {
            payment.setPaymentRequisition(this);
        }
        this.payment = payment;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public PaymentRequisition dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentRequisition)) {
            return false;
        }
        return id != null && id.equals(((PaymentRequisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisition{" +
            "id=" + getId() +
            ", invoicedAmount=" + getInvoicedAmount() +
            ", disbursementCost=" + getDisbursementCost() +
            ", vatableAmount=" + getVatableAmount() +
            "}";
    }
}
