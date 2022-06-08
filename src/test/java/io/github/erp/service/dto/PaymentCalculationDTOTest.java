package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCalculationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCalculationDTO.class);
        PaymentCalculationDTO paymentCalculationDTO1 = new PaymentCalculationDTO();
        paymentCalculationDTO1.setId(1L);
        PaymentCalculationDTO paymentCalculationDTO2 = new PaymentCalculationDTO();
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO2.setId(paymentCalculationDTO1.getId());
        assertThat(paymentCalculationDTO1).isEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO2.setId(2L);
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO1.setId(null);
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
    }
}
