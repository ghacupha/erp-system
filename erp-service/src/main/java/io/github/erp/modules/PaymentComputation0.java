package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import org.javamoney.moneta.Money;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

public class PaymentComputation0 implements PaymentComputation {

    private final String currencyCode;

    public PaymentComputation0() {
        this(BASE_SYSTEM_CURRENCY_CODE);
    }

    public PaymentComputation0(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisitionInt) {

        return PaymentCalculationInt.builder()
            .paymentAmount(paymentRequisitionInt.getInvoicedAmount())
            .paymentExpense(paymentRequisitionInt.getInvoicedAmount())
            .withholdingTax(Money.zero(PaymentComputationUtils.currencyUnit(currencyCode)))
            .withholdingVAT(Money.zero(PaymentComputationUtils.currencyUnit(currencyCode)))
            .build();
    }
}
