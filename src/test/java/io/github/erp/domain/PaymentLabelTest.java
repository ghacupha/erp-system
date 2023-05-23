package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentLabel.class);
        PaymentLabel paymentLabel1 = new PaymentLabel();
        paymentLabel1.setId(1L);
        PaymentLabel paymentLabel2 = new PaymentLabel();
        paymentLabel2.setId(paymentLabel1.getId());
        assertThat(paymentLabel1).isEqualTo(paymentLabel2);
        paymentLabel2.setId(2L);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
        paymentLabel1.setId(null);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
    }
}
