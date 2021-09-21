package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CategoryTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentCategory.
 */
@Entity
@Table(name = "payment_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentcategory")
public class PaymentCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, unique = true)
    private CategoryTypes categoryType;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_category__payment_label",
        joinColumns = @JoinColumn(name = "payment_category_id"),
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

    @OneToMany(mappedBy = "paymentCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "paymentCategory", "placeholders" }, allowSetters = true)
    private Set<PaymentCalculation> paymentCalculations = new HashSet<>();

    @OneToMany(mappedBy = "paymentCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup",
        },
        allowSetters = true
    )
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_payment_category__placeholder",
        joinColumns = @JoinColumn(name = "payment_category_id"),
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

    public PaymentCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public PaymentCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return this.categoryDescription;
    }

    public PaymentCategory categoryDescription(String categoryDescription) {
        this.setCategoryDescription(categoryDescription);
        return this;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public CategoryTypes getCategoryType() {
        return this.categoryType;
    }

    public PaymentCategory categoryType(CategoryTypes categoryType) {
        this.setCategoryType(categoryType);
        return this;
    }

    public void setCategoryType(CategoryTypes categoryType) {
        this.categoryType = categoryType;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentCategory paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public PaymentCategory addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        paymentLabel.getPaymentCategories().add(this);
        return this;
    }

    public PaymentCategory removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        paymentLabel.getPaymentCategories().remove(this);
        return this;
    }

    public Set<PaymentCalculation> getPaymentCalculations() {
        return this.paymentCalculations;
    }

    public void setPaymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        if (this.paymentCalculations != null) {
            this.paymentCalculations.forEach(i -> i.setPaymentCategory(null));
        }
        if (paymentCalculations != null) {
            paymentCalculations.forEach(i -> i.setPaymentCategory(this));
        }
        this.paymentCalculations = paymentCalculations;
    }

    public PaymentCategory paymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        this.setPaymentCalculations(paymentCalculations);
        return this;
    }

    public PaymentCategory addPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.add(paymentCalculation);
        paymentCalculation.setPaymentCategory(this);
        return this;
    }

    public PaymentCategory removePaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.remove(paymentCalculation);
        paymentCalculation.setPaymentCategory(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPaymentCategory(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setPaymentCategory(this));
        }
        this.payments = payments;
    }

    public PaymentCategory payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public PaymentCategory addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setPaymentCategory(this);
        return this;
    }

    public PaymentCategory removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setPaymentCategory(null);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentCategory placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentCategory addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        placeholder.getPaymentCategories().add(this);
        return this;
    }

    public PaymentCategory removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        placeholder.getPaymentCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCategory)) {
            return false;
        }
        return id != null && id.equals(((PaymentCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", categoryDescription='" + getCategoryDescription() + "'" +
            ", categoryType='" + getCategoryType() + "'" +
            "}";
    }
}
