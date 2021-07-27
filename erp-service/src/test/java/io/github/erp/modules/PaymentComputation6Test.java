package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentComputation6Test extends PaymentComputationTests{

    private PaymentComputation6 computation;

    private PaymentCalculationInt calculation;

    @BeforeEach
    void setUp() {
        computation = new PaymentComputation6(taxRule, "USD");
        calculation = computation.calculate(requisition$);
    }

    @Test
    void calculate() {

        testEquality(calculation.getPaymentExpense(), Money.of(40000, "USD"));
        testEquality(calculation.getWithholdingVAT(), Money.of(689.66, "USD"));
        testEquality(calculation.getWithholdingTax(), Money.of(0, "USD"));
        testEquality(calculation.getPaymentAmount(), Money.of(39310.34, "USD"));
    }
}
