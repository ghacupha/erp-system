package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.TaxRule} entity.
 */
public class TaxRuleDTO implements Serializable {

    private Long id;

    private Double telcoExciseDuty;

    private Double valueAddedTax;

    private Double withholdingVAT;

    private Double withholdingTaxConsultancy;

    private Double withholdingTaxRent;

    private Double cateringLevy;

    private Double serviceCharge;

    private Double withholdingTaxImportedService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTelcoExciseDuty() {
        return telcoExciseDuty;
    }

    public void setTelcoExciseDuty(Double telcoExciseDuty) {
        this.telcoExciseDuty = telcoExciseDuty;
    }

    public Double getValueAddedTax() {
        return valueAddedTax;
    }

    public void setValueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public Double getWithholdingVAT() {
        return withholdingVAT;
    }

    public void setWithholdingVAT(Double withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public Double getWithholdingTaxConsultancy() {
        return withholdingTaxConsultancy;
    }

    public void setWithholdingTaxConsultancy(Double withholdingTaxConsultancy) {
        this.withholdingTaxConsultancy = withholdingTaxConsultancy;
    }

    public Double getWithholdingTaxRent() {
        return withholdingTaxRent;
    }

    public void setWithholdingTaxRent(Double withholdingTaxRent) {
        this.withholdingTaxRent = withholdingTaxRent;
    }

    public Double getCateringLevy() {
        return cateringLevy;
    }

    public void setCateringLevy(Double cateringLevy) {
        this.cateringLevy = cateringLevy;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getWithholdingTaxImportedService() {
        return withholdingTaxImportedService;
    }

    public void setWithholdingTaxImportedService(Double withholdingTaxImportedService) {
        this.withholdingTaxImportedService = withholdingTaxImportedService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRuleDTO)) {
            return false;
        }

        TaxRuleDTO taxRuleDTO = (TaxRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taxRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRuleDTO{" +
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
