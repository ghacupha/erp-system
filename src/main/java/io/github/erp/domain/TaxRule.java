package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaxRule.
 */
@Entity
@Table(name = "tax_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxrule")
public class TaxRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "telco_excise_duty")
    private Double telcoExciseDuty;

    @Column(name = "value_added_tax")
    private Double valueAddedTax;

    @Column(name = "withholding_vat")
    private Double withholdingVAT;

    @Column(name = "withholding_tax_consultancy")
    private Double withholdingTaxConsultancy;

    @Column(name = "withholding_tax_rent")
    private Double withholdingTaxRent;

    @Column(name = "catering_levy")
    private Double cateringLevy;

    @Column(name = "service_charge")
    private Double serviceCharge;

    @Column(name = "withholding_tax_imported_service")
    private Double withholdingTaxImportedService;

    @OneToMany(mappedBy = "taxRule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealer", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup" },
        allowSetters = true
    )
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_tax_rule__placeholder",
        joinColumns = @JoinColumn(name = "tax_rule_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaxRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTelcoExciseDuty() {
        return this.telcoExciseDuty;
    }

    public TaxRule telcoExciseDuty(Double telcoExciseDuty) {
        this.setTelcoExciseDuty(telcoExciseDuty);
        return this;
    }

    public void setTelcoExciseDuty(Double telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
    }

    public Double getValueAddedTax() {
        return this.valueAddedTax;
    }

    public TaxRule valueAddedTax(Double valueAddedTax) {
        this.setValueAddedTax(valueAddedTax);
        return this;
    }

    public void setValueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public Double getWithholdingVAT() {
        return this.withholdingVAT;
    }

    public TaxRule withholdingVAT(Double withholdingVAT) {
        this.setWithholdingVAT(withholdingVAT);
        return this;
    }

    public void setWithholdingVAT(Double withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public Double getWithholdingTaxConsultancy() {
        return this.withholdingTaxConsultancy;
    }

    public TaxRule withholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.setWithholdingTaxConsultancy(withholdingTaxConsultancy);
        return this;
    }

    public void setWithholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
    }

    public Double getWithholdingTaxRent() {
        return this.withholdingTaxRent;
    }

    public TaxRule withholdingTaxRent(Double withholdingTaxRent) {
        this.setWithholdingTaxRent(withholdingTaxRent);
        return this;
    }

    public void setWithholdingTaxRent(Double withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
    }

    public Double getCateringLevy() {
        return this.cateringLevy;
    }

    public TaxRule cateringLevy(Double cateringLevy) {
        this.setCateringLevy(cateringLevy);
        return this;
    }

    public void setCateringLevy(Double cateringLevy) {
        this.cateringLevy = cateringLevy;
    }

    public Double getServiceCharge() {
        return this.serviceCharge;
    }

    public TaxRule serviceCharge(Double serviceCharge) {
        this.setServiceCharge(serviceCharge);
        return this;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getWithholdingTaxImportedService() {
        return this.withholdingTaxImportedService;
    }

    public TaxRule withholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.setWithholdingTaxImportedService(withholdingTaxImportedService);
        return this;
    }

    public void setWithholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setTaxRule(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setTaxRule(this));
        }
        this.payments = payments;
    }

    public TaxRule payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public TaxRule addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setTaxRule(this);
        return this;
    }

    public TaxRule removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setTaxRule(null);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TaxRule placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TaxRule addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TaxRule removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRule)) {
            return false;
        }
        return id != null && id.equals(((TaxRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRule{" +
            "id=" + getId() +
            ", telcoExciseDuty=" + getTelcoExciseDuty() +
            ", valueAddedTax=" + getValueAddedTax() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTaxConsultancy=" + getWithholdingTaxConsultancy() +
            ", withholdingTaxRent=" + getWithholdingTaxRent() +
            ", cateringLevy=" + getCateringLevy() +
            ", serviceCharge=" + getServiceCharge() +
            ", withholdingTaxImportedService=" + getWithholdingTaxImportedService() +
            "}";
    }
}
