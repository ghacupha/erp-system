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
    @Column(name = "id")
    private Long id;

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "disbursement_cost", precision = 21, scale = 2)
    private BigDecimal disbursementCost;

    @Column(name = "vatable_amount", precision = 21, scale = 2)
    private BigDecimal vatableAmount;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_requisition__payment_label",
        joinColumns = @JoinColumn(name = "payment_requisition_id"),
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

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "payments", "paymentRequisitions", "placeholders" },
        allowSetters = true
    )
    private Dealer dealer;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_requisition__placeholder",
        joinColumns = @JoinColumn(name = "payment_requisition_id"),
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

    public PaymentRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInvoicedAmount() {
        return this.invoicedAmount;
    }

    public PaymentRequisition invoicedAmount(BigDecimal invoicedAmount) {
        this.setInvoicedAmount(invoicedAmount);
        return this;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getDisbursementCost() {
        return this.disbursementCost;
    }

    public PaymentRequisition disbursementCost(BigDecimal disbursementCost) {
        this.setDisbursementCost(disbursementCost);
        return this;
    }

    public void setDisbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimal getVatableAmount() {
        return this.vatableAmount;
    }

    public PaymentRequisition vatableAmount(BigDecimal vatableAmount) {
        this.setVatableAmount(vatableAmount);
        return this;
    }

    public void setVatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentRequisition paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public PaymentRequisition addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        paymentLabel.getPaymentRequisitions().add(this);
        return this;
    }

    public PaymentRequisition removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        paymentLabel.getPaymentRequisitions().remove(this);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public PaymentRequisition dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentRequisition placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentRequisition addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        placeholder.getPaymentRequisitions().add(this);
        return this;
    }

    public PaymentRequisition removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        placeholder.getPaymentRequisitions().remove(this);
        return this;
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
