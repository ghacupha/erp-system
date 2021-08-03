package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CategoryTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

    @JsonIgnoreProperties(
        value = { "ownedInvoices", "dealers", "taxRule", "paymentCategory", "paymentCalculation", "paymentRequisition" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "paymentCategory")
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentCategory id(Long id) {
        this.id = id;
        return this;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public PaymentCategory categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return this.categoryDescription;
    }

    public PaymentCategory categoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
        return this;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public CategoryTypes getCategoryType() {
        return this.categoryType;
    }

    public PaymentCategory categoryType(CategoryTypes categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public void setCategoryType(CategoryTypes categoryType) {
        this.categoryType = categoryType;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public PaymentCategory payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setPaymentCategory(null);
        }
        if (payment != null) {
            payment.setPaymentCategory(this);
        }
        this.payment = payment;
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
