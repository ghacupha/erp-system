package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentInvoiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentInvoiceDTO.class);
        PaymentInvoiceDTO paymentInvoiceDTO1 = new PaymentInvoiceDTO();
        paymentInvoiceDTO1.setId(1L);
        PaymentInvoiceDTO paymentInvoiceDTO2 = new PaymentInvoiceDTO();
        assertThat(paymentInvoiceDTO1).isNotEqualTo(paymentInvoiceDTO2);
        paymentInvoiceDTO2.setId(paymentInvoiceDTO1.getId());
        assertThat(paymentInvoiceDTO1).isEqualTo(paymentInvoiceDTO2);
        paymentInvoiceDTO2.setId(2L);
        assertThat(paymentInvoiceDTO1).isNotEqualTo(paymentInvoiceDTO2);
        paymentInvoiceDTO1.setId(null);
        assertThat(paymentInvoiceDTO1).isNotEqualTo(paymentInvoiceDTO2);
    }
}
