package io.github.erp.modules;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;

class BaseComputationTest extends PaymentComputationTests {

    private BaseComputation bs;

    @BeforeEach
    void setUp() {
        bs = new BaseComputation();
    }

    @Test
    void calculateWithholdingVATAmount() {

        assertThat(bs.calculateWithholdingVATAmount(taxRule, Money.of(34482.76,BASE_SYSTEM_CURRENCY_CODE)).getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.HALF_EVEN))
            .isEqualTo(BigDecimal.valueOf(689.66));

    }
}
