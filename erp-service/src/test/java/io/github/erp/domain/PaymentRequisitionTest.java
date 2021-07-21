package io.github.erp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class PaymentRequisitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentRequisition.class);
        PaymentRequisition paymentRequisition1 = new PaymentRequisition();
        paymentRequisition1.setId(1L);
        PaymentRequisition paymentRequisition2 = new PaymentRequisition();
        paymentRequisition2.setId(paymentRequisition1.getId());
        assertThat(paymentRequisition1).isEqualTo(paymentRequisition2);
        paymentRequisition2.setId(2L);
        assertThat(paymentRequisition1).isNotEqualTo(paymentRequisition2);
        paymentRequisition1.setId(null);
        assertThat(paymentRequisition1).isNotEqualTo(paymentRequisition2);
    }
}
