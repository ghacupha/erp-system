package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A PaymentCalculation.
 */
@Entity
@Table(name = "payment_calculation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentcalculation")
public class PaymentCalculation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_category")
    private String paymentCategory;

    @Column(name = "payment_expense", precision = 21, scale = 2)
    private BigDecimal paymentExpense;

    @Column(name = "withholding_vat", precision = 21, scale = 2)
    private BigDecimal withholdingVAT;

    @Column(name = "withholding_tax", precision = 21, scale = 2)
    private BigDecimal withholdingTax;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @OneToOne(mappedBy = "paymentCalculation")
    @JsonIgnore
    private Payment payment;

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

    public PaymentCalculation paymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public PaymentCalculation paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public PaymentCalculation paymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
        return this;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public BigDecimal getPaymentExpense() {
        return paymentExpense;
    }

    public PaymentCalculation paymentExpense(BigDecimal paymentExpense) {
        this.paymentExpense = paymentExpense;
        return this;
    }

    public void setPaymentExpense(BigDecimal paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimal getWithholdingVAT() {
        return withholdingVAT;
    }

    public PaymentCalculation withholdingVAT(BigDecimal withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
        return this;
    }

    public void setWithholdingVAT(BigDecimal withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public PaymentCalculation withholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
        return this;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public PaymentCalculation paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Payment getPayment() {
        return payment;
    }

    public PaymentCalculation payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCalculation)) {
            return false;
        }
        return id != null && id.equals(((PaymentCalculation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculation{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentCategory='" + getPaymentCategory() + "'" +
            ", paymentExpense=" + getPaymentExpense() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTax=" + getWithholdingTax() +
            ", paymentAmount=" + getPaymentAmount() +
            "}";
    }
}
