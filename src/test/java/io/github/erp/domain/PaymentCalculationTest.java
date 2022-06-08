package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCalculationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCalculation.class);
        PaymentCalculation paymentCalculation1 = new PaymentCalculation();
        paymentCalculation1.setId(1L);
        PaymentCalculation paymentCalculation2 = new PaymentCalculation();
        paymentCalculation2.setId(paymentCalculation1.getId());
        assertThat(paymentCalculation1).isEqualTo(paymentCalculation2);
        paymentCalculation2.setId(2L);
        assertThat(paymentCalculation1).isNotEqualTo(paymentCalculation2);
        paymentCalculation1.setId(null);
        assertThat(paymentCalculation1).isNotEqualTo(paymentCalculation2);
    }
}
