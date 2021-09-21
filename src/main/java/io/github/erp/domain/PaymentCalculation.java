package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_expense", precision = 21, scale = 2)
    private BigDecimal paymentExpense;

    @Column(name = "withholding_vat", precision = 21, scale = 2)
    private BigDecimal withholdingVAT;

    @Column(name = "withholding_tax", precision = 21, scale = 2)
    private BigDecimal withholdingTax;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_calculation__payment_label",
        joinColumns = @JoinColumn(name = "payment_calculation_id"),
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

    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup",
        },
        allowSetters = true
    )
    @OneToOne(mappedBy = "paymentCalculation")
    private Payment payment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "payments", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_calculation__placeholder",
        joinColumns = @JoinColumn(name = "payment_calculation_id"),
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

    public PaymentCalculation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPaymentExpense() {
        return this.paymentExpense;
    }

    public PaymentCalculation paymentExpense(BigDecimal paymentExpense) {
        this.setPaymentExpense(paymentExpense);
        return this;
    }

    public void setPaymentExpense(BigDecimal paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimal getWithholdingVAT() {
        return this.withholdingVAT;
    }

    public PaymentCalculation withholdingVAT(BigDecimal withholdingVAT) {
        this.setWithholdingVAT(withholdingVAT);
        return this;
    }

    public void setWithholdingVAT(BigDecimal withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimal getWithholdingTax() {
        return this.withholdingTax;
    }

    public PaymentCalculation withholdingTax(BigDecimal withholdingTax) {
        this.setWithholdingTax(withholdingTax);
        return this;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public PaymentCalculation paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentCalculation paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public PaymentCalculation addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        paymentLabel.getPaymentCalculations().add(this);
        return this;
    }

    public PaymentCalculation removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        paymentLabel.getPaymentCalculations().remove(this);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setPaymentCalculation(null);
        }
        if (payment != null) {
            payment.setPaymentCalculation(this);
        }
        this.payment = payment;
    }

    public PaymentCalculation payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public PaymentCalculation paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentCalculation placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentCalculation addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        placeholder.getPaymentCalculations().add(this);
        return this;
    }

    public PaymentCalculation removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        placeholder.getPaymentCalculations().remove(this);
        return this;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculation{" +
            "id=" + getId() +
            ", paymentExpense=" + getPaymentExpense() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTax=" + getWithholdingTax() +
            ", paymentAmount=" + getPaymentAmount() +
            "}";
    }
}
