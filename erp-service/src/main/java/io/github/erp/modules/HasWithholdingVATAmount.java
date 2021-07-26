package io.github.erp.modules;

import io.github.erp.modules.models.TaxRuleInt;

import javax.money.MonetaryAmount;

public interface HasWithholdingVATAmount {

    MonetaryAmount calculateWithholdingVATAmount(TaxRuleInt taxRule, MonetaryAmount invoiceNetOfTax);
}
