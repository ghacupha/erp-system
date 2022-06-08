package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCategory.class);
        PaymentCategory paymentCategory1 = new PaymentCategory();
        paymentCategory1.setId(1L);
        PaymentCategory paymentCategory2 = new PaymentCategory();
        paymentCategory2.setId(paymentCategory1.getId());
        assertThat(paymentCategory1).isEqualTo(paymentCategory2);
        paymentCategory2.setId(2L);
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);
        paymentCategory1.setId(null);
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);
    }
}
