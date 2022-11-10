package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentLabelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentLabelDTO.class);
        PaymentLabelDTO paymentLabelDTO1 = new PaymentLabelDTO();
        paymentLabelDTO1.setId(1L);
        PaymentLabelDTO paymentLabelDTO2 = new PaymentLabelDTO();
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
        paymentLabelDTO2.setId(paymentLabelDTO1.getId());
        assertThat(paymentLabelDTO1).isEqualTo(paymentLabelDTO2);
        paymentLabelDTO2.setId(2L);
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
        paymentLabelDTO1.setId(null);
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
    }
}
