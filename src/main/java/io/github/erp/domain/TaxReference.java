package io.github.erp.domain;

import io.github.erp.domain.enumeration.taxReferenceTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A TaxReference.
 */
@Entity
@Table(name = "tax_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxreference")
public class TaxReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tax_name", unique = true)
    private String taxName;

    @Column(name = "tax_description")
    private String taxDescription;

    @NotNull
    @Column(name = "tax_percentage", nullable = false)
    private Double taxPercentage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_reference_type", nullable = false, unique = true)
    private taxReferenceTypes taxReferenceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxReference id(Long id) {
        this.id = id;
        return this;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public TaxReference taxName(String taxName) {
        this.taxName = taxName;
        return this;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxDescription() {
        return this.taxDescription;
    }

    public TaxReference taxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
        return this;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public Double getTaxPercentage() {
        return this.taxPercentage;
    }

    public TaxReference taxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
        return this;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public taxReferenceTypes getTaxReferenceType() {
        return this.taxReferenceType;
    }

    public TaxReference taxReferenceType(taxReferenceTypes taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
        return this;
    }

    public void setTaxReferenceType(taxReferenceTypes taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxReference)) {
            return false;
        }
        return id != null && id.equals(((TaxReference) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxReference{" +
            "id=" + getId() +
            ", taxName='" + getTaxName() + "'" +
            ", taxDescription='" + getTaxDescription() + "'" +
            ", taxPercentage=" + getTaxPercentage() +
            ", taxReferenceType='" + getTaxReferenceType() + "'" +
            "}";
    }
}
