package io.github.erp.modules;

import io.github.erp.modules.models.TaxRuleInt;

import javax.money.MonetaryAmount;

public class BaseComputation implements HasWithholdingVATAmount{

    @Override
    public MonetaryAmount calculateWithholdingVATAmount(TaxRuleInt taxRule, MonetaryAmount invoiceNetOfTax) {
        return invoiceNetOfTax.multiply(taxRule.getWithholdingVAT());
    }
}
