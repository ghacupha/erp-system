package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentRequisitionDTO.class);
        PaymentRequisitionDTO paymentRequisitionDTO1 = new PaymentRequisitionDTO();
        paymentRequisitionDTO1.setId(1L);
        PaymentRequisitionDTO paymentRequisitionDTO2 = new PaymentRequisitionDTO();
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO2.setId(paymentRequisitionDTO1.getId());
        assertThat(paymentRequisitionDTO1).isEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO2.setId(2L);
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO1.setId(null);
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
    }
}
