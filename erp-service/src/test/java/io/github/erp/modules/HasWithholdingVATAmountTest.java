package io.github.erp.modules;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.assertj.core.api.Assertions.assertThat;

class HasWithholdingVATAmountTest extends PaymentComputationTests {

    @Test
    void calculateRoundedWithholdingVATAmount() {

        HasWithholdingVATAmount hs = new BaseComputation();

        assertThat(hs.calculateRoundedWithholdingVATAmount(taxRule, Money.of(34482.76,BASE_SYSTEM_CURRENCY_CODE)))
            .isEqualTo(Money.of(690, BASE_SYSTEM_CURRENCY_CODE));

    }
}
