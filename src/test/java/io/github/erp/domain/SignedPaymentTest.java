package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class SignedPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignedPayment.class);
        SignedPayment signedPayment1 = new SignedPayment();
        signedPayment1.setId(1L);
        SignedPayment signedPayment2 = new SignedPayment();
        signedPayment2.setId(signedPayment1.getId());
        assertThat(signedPayment1).isEqualTo(signedPayment2);
        signedPayment2.setId(2L);
        assertThat(signedPayment1).isNotEqualTo(signedPayment2);
        signedPayment1.setId(null);
        assertThat(signedPayment1).isNotEqualTo(signedPayment2);
    }
}
