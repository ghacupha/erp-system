package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentInvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentInvoice.class);
        PaymentInvoice paymentInvoice1 = new PaymentInvoice();
        paymentInvoice1.setId(1L);
        PaymentInvoice paymentInvoice2 = new PaymentInvoice();
        paymentInvoice2.setId(paymentInvoice1.getId());
        assertThat(paymentInvoice1).isEqualTo(paymentInvoice2);
        paymentInvoice2.setId(2L);
        assertThat(paymentInvoice1).isNotEqualTo(paymentInvoice2);
        paymentInvoice1.setId(null);
        assertThat(paymentInvoice1).isNotEqualTo(paymentInvoice2);
    }
}
