package io.github.erp.modules.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This object holds the applicable tax regulation rates for a given payment
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxRuleInt implements Serializable {

    private BigDecimal telcoExciseDuty;

    private BigDecimal valueAddedTax;

    private BigDecimal withholdingVAT;

    private BigDecimal withholdingTaxConsultancy;

    private BigDecimal withholdingTaxRent;

    private BigDecimal cateringLevy;

    private BigDecimal serviceCharge;

    private BigDecimal withholdingTaxImportedService;
}
