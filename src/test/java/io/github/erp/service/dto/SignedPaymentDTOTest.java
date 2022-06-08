package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignedPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignedPaymentDTO.class);
        SignedPaymentDTO signedPaymentDTO1 = new SignedPaymentDTO();
        signedPaymentDTO1.setId(1L);
        SignedPaymentDTO signedPaymentDTO2 = new SignedPaymentDTO();
        assertThat(signedPaymentDTO1).isNotEqualTo(signedPaymentDTO2);
        signedPaymentDTO2.setId(signedPaymentDTO1.getId());
        assertThat(signedPaymentDTO1).isEqualTo(signedPaymentDTO2);
        signedPaymentDTO2.setId(2L);
        assertThat(signedPaymentDTO1).isNotEqualTo(signedPaymentDTO2);
        signedPaymentDTO1.setId(null);
        assertThat(signedPaymentDTO1).isNotEqualTo(signedPaymentDTO2);
    }
}
