package io.github.erp.modules;

import io.github.erp.modules.models.TaxRuleInt;

import javax.money.MonetaryAmount;

import static io.github.erp.modules.PaymentComputationUtils.roundToZeroDP;

/**
 * Marker for computation that contains a withholding VAT amount computation
 */
public interface HasWithholdingVATAmount {

    default MonetaryAmount calculateRoundedWithholdingVATAmount(TaxRuleInt taxRule, MonetaryAmount invoiceNetOfTax) {

        return roundToZeroDP(calculateWithholdingVATAmount(taxRule,invoiceNetOfTax));
    }

    MonetaryAmount calculateWithholdingVATAmount(TaxRuleInt taxRule, MonetaryAmount invoiceNetOfTax);
}
