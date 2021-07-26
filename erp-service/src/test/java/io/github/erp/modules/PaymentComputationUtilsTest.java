package io.github.erp.modules;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.github.erp.modules.PaymentComputationUtils.onePlusVAT;
import static io.github.erp.modules.PaymentComputationUtils.queryNumerical;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PaymentComputationUtilsTest extends PaymentComputationTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void onePlusVATTest() {

        assertThat(onePlusVAT(taxRule)).isEqualTo(BigDecimal.valueOf(1.16));
    }

    @Test
    void queryNumericalTest() {

        assertThat(queryNumerical(requisition.getInvoicedAmount())).isEqualTo(Money.of(40000.00,BASE_SYSTEM_CURRENCY_CODE));
    }
}
