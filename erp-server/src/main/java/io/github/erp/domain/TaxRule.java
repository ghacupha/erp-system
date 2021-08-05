package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

    @JsonIgnoreProperties(
        value = { "ownedInvoices", "dealers", "taxRule", "paymentCalculation", "paymentRequisition" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "taxRule")
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxRule id(Long id) {
        this.id = id;
        return this;
    }

    public Double getTelcoExciseDuty() {
        return this.telcoExciseDuty;
    }

    public TaxRule telcoExciseDuty(Double telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
        return this;
    }

    public void setTelcoExciseDuty(Double telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
    }

    public Double getValueAddedTax() {
        return this.valueAddedTax;
    }

    public TaxRule valueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
        return this;
    }

    public void setValueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public Double getWithholdingVAT() {
        return this.withholdingVAT;
    }

    public TaxRule withholdingVAT(Double withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
        return this;
    }

    public void setWithholdingVAT(Double withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public Double getWithholdingTaxConsultancy() {
        return this.withholdingTaxConsultancy;
    }

    public TaxRule withholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
        return this;
    }

    public void setWithholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
    }

    public Double getWithholdingTaxRent() {
        return this.withholdingTaxRent;
    }

    public TaxRule withholdingTaxRent(Double withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
        return this;
    }

    public void setWithholdingTaxRent(Double withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
    }

    public Double getCateringLevy() {
        return this.cateringLevy;
    }

    public TaxRule cateringLevy(Double cateringLevy) {
        this.cateringLevy = cateringLevy;
        return this;
    }

    public void setCateringLevy(Double cateringLevy) {
        this.cateringLevy = cateringLevy;
    }

    public Double getServiceCharge() {
        return this.serviceCharge;
    }

    public TaxRule serviceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
        return this;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getWithholdingTaxImportedService() {
        return this.withholdingTaxImportedService;
    }

    public TaxRule withholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
        return this;
    }

    public void setWithholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public TaxRule payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setTaxRule(null);
        }
        if (payment != null) {
            payment.setTaxRule(this);
        }
        this.payment = payment;
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
