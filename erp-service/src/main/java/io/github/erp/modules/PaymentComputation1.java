package io.github.erp.modules;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static io.github.erp.modules.PaymentComputationUtils.currencyUnit;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

/**
 * In this payment computation we simply pay what has been requested withholding only
 * the withholding-VAT
 */
public class PaymentComputation1 extends BaseComputation implements PaymentComputation, HasWithholdingVATAmount {

    private final TaxRuleInt taxRule;
    private final String currencyCode;

    public PaymentComputation1(TaxRuleInt taxRule) {
        this(taxRule, BASE_SYSTEM_CURRENCY_CODE);
    }

    public PaymentComputation1(TaxRuleInt taxRule, String currencyCode) {
        this.taxRule = taxRule;
        this.currencyCode = currencyCode;
    }

    @Override
    public PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisition) {

        return calculate(paymentRequisition, taxRule, currencyCode);
    }


    private PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisition, TaxRuleInt taxRule, String currencyCode) {

        MonetaryAmount invoiceNetOfTax =
            Money.of(PaymentComputationUtils.queryNumerical(paymentRequisition.getInvoicedAmount().divide(PaymentComputationUtils.onePlusVAT(taxRule))), currencyUnit(currencyCode));

        MonetaryAmount withholdingVAT = calculateRoundedWithholdingVATAmount(taxRule, invoiceNetOfTax);

        MonetaryAmount withholdingTax = Money.of(BigDecimal.ZERO, PaymentComputationUtils.currencyUnit(currencyCode));

        MonetaryAmount netPayable =
            paymentRequisition.getInvoicedAmount().subtract(withholdingVAT).subtract(withholdingTax);

        MonetaryAmount expense = PaymentComputationUtils.requisitionEquivalentExpense(paymentRequisition, currencyCode);

        return PaymentCalculationInt.builder()
            .paymentAmount(netPayable)
            .paymentExpense(expense)
            .withholdingTax(withholdingTax)
            .withholdingVAT(withholdingVAT)
            .build();
    }
}
