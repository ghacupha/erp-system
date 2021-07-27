package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PaymentComputation0Test extends PaymentComputationTests {

    private PaymentComputation0 computation;

    private PaymentCalculationInt calculation;

    @BeforeEach
    void setUp() {
        computation = new PaymentComputation0();
        calculation = computation.calculate(requisition);
    }

    @Test
    void calculate() {
        assertThat(calculation.getPaymentExpense()).isEqualTo(Money.of(40000, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getWithholdingVAT()).isEqualTo(Money.of(0, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getWithholdingTax()).isEqualTo(Money.of(0, BASE_SYSTEM_CURRENCY_CODE));
        assertThat(calculation.getPaymentAmount()).isEqualTo(Money.of(40000, BASE_SYSTEM_CURRENCY_CODE));
    }
}
