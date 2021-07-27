package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static io.github.erp.modules.PaymentComputationUtils.currencyUnit;

/**
 * Similar to computation 1 but mostly used for fx transactions so the tax values will have
 * decimal places. The currency has to be provided for this to work.
 */
public class PaymentComputation6 extends BaseComputation implements PaymentComputation, HasWithholdingVATAmount {

    private final TaxRuleInt taxRule;
    private final String currencyCode;

    public PaymentComputation6(TaxRuleInt taxRule, String currencyCode) {
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

        MonetaryAmount withholdingVAT = calculateWithholdingVATAmount(taxRule, invoiceNetOfTax);

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
