package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.github.erp.modules.PaymentComputationUtils.currencyUnit;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

/**
 * In this payment computation we simply pay what has been requested withholding only
 * the withholding-VAT
 */
public class PaymentComputation1 implements PaymentComputation {

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
            Money.of(PaymentComputationUtils.queryNumerical(paymentRequisition.getInvoicedAmount()).divide(PaymentComputationUtils.onePlusVAT(taxRule), RoundingMode.HALF_EVEN), currencyUnit(currencyCode));

        MonetaryAmount withholdingVAT = invoiceNetOfTax.multiply(taxRule.getWithholdingVAT());

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
