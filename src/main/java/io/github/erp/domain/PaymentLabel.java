package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentLabel.
 */
@Entity
@Table(name = "payment_label")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentlabel")
public class PaymentLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
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
    private PaymentLabel containingPaymentLabel;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_label__placeholder",
        joinColumns = @JoinColumn(name = "payment_label_id"),
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

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "paymentCategory", "placeholders" }, allowSetters = true)
    private Set<PaymentCalculation> paymentCalculations = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "payments", "placeholders" }, allowSetters = true)
    private Set<PaymentCategory> paymentCategories = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealer", "placeholders" }, allowSetters = true)
    private Set<PaymentRequisition> paymentRequisitions = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup",
        },
        allowSetters = true
    )
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "dealer", "placeholders" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "payments", "paymentRequisitions", "placeholders" },
        allowSetters = true
    )
    private Set<Dealer> dealers = new HashSet<>();

    @ManyToMany(mappedBy = "paymentLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealers", "paymentCategory", "placeholders", "signedPaymentGroup" },
        allowSetters = true
    )
    private Set<SignedPayment> signedPayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentLabel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public PaymentLabel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return this.comments;
    }

    public PaymentLabel comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public PaymentLabel getContainingPaymentLabel() {
        return this.containingPaymentLabel;
    }

    public void setContainingPaymentLabel(PaymentLabel paymentLabel) {
        this.containingPaymentLabel = paymentLabel;
    }

    public PaymentLabel containingPaymentLabel(PaymentLabel paymentLabel) {
        this.setContainingPaymentLabel(paymentLabel);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentLabel placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentLabel addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PaymentLabel removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentCalculation> getPaymentCalculations() {
        return this.paymentCalculations;
    }

    public void setPaymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        if (this.paymentCalculations != null) {
            this.paymentCalculations.forEach(i -> i.removePaymentLabel(this));
        }
        if (paymentCalculations != null) {
            paymentCalculations.forEach(i -> i.addPaymentLabel(this));
        }
        this.paymentCalculations = paymentCalculations;
    }

    public PaymentLabel paymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        this.setPaymentCalculations(paymentCalculations);
        return this;
    }

    public PaymentLabel addPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.add(paymentCalculation);
        paymentCalculation.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removePaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.remove(paymentCalculation);
        paymentCalculation.getPaymentLabels().remove(this);
        return this;
    }

    public Set<PaymentCategory> getPaymentCategories() {
        return this.paymentCategories;
    }

    public void setPaymentCategories(Set<PaymentCategory> paymentCategories) {
        if (this.paymentCategories != null) {
            this.paymentCategories.forEach(i -> i.removePaymentLabel(this));
        }
        if (paymentCategories != null) {
            paymentCategories.forEach(i -> i.addPaymentLabel(this));
        }
        this.paymentCategories = paymentCategories;
    }

    public PaymentLabel paymentCategories(Set<PaymentCategory> paymentCategories) {
        this.setPaymentCategories(paymentCategories);
        return this;
    }

    public PaymentLabel addPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.add(paymentCategory);
        paymentCategory.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removePaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.remove(paymentCategory);
        paymentCategory.getPaymentLabels().remove(this);
        return this;
    }

    public Set<PaymentRequisition> getPaymentRequisitions() {
        return this.paymentRequisitions;
    }

    public void setPaymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        if (this.paymentRequisitions != null) {
            this.paymentRequisitions.forEach(i -> i.removePaymentLabel(this));
        }
        if (paymentRequisitions != null) {
            paymentRequisitions.forEach(i -> i.addPaymentLabel(this));
        }
        this.paymentRequisitions = paymentRequisitions;
    }

    public PaymentLabel paymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        this.setPaymentRequisitions(paymentRequisitions);
        return this;
    }

    public PaymentLabel addPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.add(paymentRequisition);
        paymentRequisition.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removePaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.remove(paymentRequisition);
        paymentRequisition.getPaymentLabels().remove(this);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.removePaymentLabel(this));
        }
        if (payments != null) {
            payments.forEach(i -> i.addPaymentLabel(this));
        }
        this.payments = payments;
    }

    public PaymentLabel payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public PaymentLabel addPayment(Payment payment) {
        this.payments.add(payment);
        payment.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.getPaymentLabels().remove(this);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.removePaymentLabel(this));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.addPaymentLabel(this));
        }
        this.invoices = invoices;
    }

    public PaymentLabel invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public PaymentLabel addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.getPaymentLabels().remove(this);
        return this;
    }

    public Set<Dealer> getDealers() {
        return this.dealers;
    }

    public void setDealers(Set<Dealer> dealers) {
        if (this.dealers != null) {
            this.dealers.forEach(i -> i.removePaymentLabel(this));
        }
        if (dealers != null) {
            dealers.forEach(i -> i.addPaymentLabel(this));
        }
        this.dealers = dealers;
    }

    public PaymentLabel dealers(Set<Dealer> dealers) {
        this.setDealers(dealers);
        return this;
    }

    public PaymentLabel addDealer(Dealer dealer) {
        this.dealers.add(dealer);
        dealer.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removeDealer(Dealer dealer) {
        this.dealers.remove(dealer);
        dealer.getPaymentLabels().remove(this);
        return this;
    }

    public Set<SignedPayment> getSignedPayments() {
        return this.signedPayments;
    }

    public void setSignedPayments(Set<SignedPayment> signedPayments) {
        if (this.signedPayments != null) {
            this.signedPayments.forEach(i -> i.removePaymentLabel(this));
        }
        if (signedPayments != null) {
            signedPayments.forEach(i -> i.addPaymentLabel(this));
        }
        this.signedPayments = signedPayments;
    }

    public PaymentLabel signedPayments(Set<SignedPayment> signedPayments) {
        this.setSignedPayments(signedPayments);
        return this;
    }

    public PaymentLabel addSignedPayment(SignedPayment signedPayment) {
        this.signedPayments.add(signedPayment);
        signedPayment.getPaymentLabels().add(this);
        return this;
    }

    public PaymentLabel removeSignedPayment(SignedPayment signedPayment) {
        this.signedPayments.remove(signedPayment);
        signedPayment.getPaymentLabels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentLabel)) {
            return false;
        }
        return id != null && id.equals(((PaymentLabel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentLabel{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
