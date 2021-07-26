package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

class PaymentComputation1Test extends PaymentComputationTests  {

    private PaymentComputation computation;

    @BeforeEach
    void setUp() {
        computation = new PaymentComputation1(taxRule);
    }

    @Test
    void calculate() {
        PaymentCalculationInt calculation = computation.calculate(requisition);

        assertThat(calculation.getPaymentExpense()).isEqualTo(Money.of(40000, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getWithholdingVAT()).isEqualTo(Money.of(690, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getWithholdingTax()).isEqualTo(Money.of(0, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getPaymentAmount()).isEqualTo(Money.of(39310, BASE_SYSTEM_CURRENCY_CODE));
    }
}
