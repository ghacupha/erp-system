package io.github.erp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class PaymentCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCategoryDTO.class);
        PaymentCategoryDTO paymentCategoryDTO1 = new PaymentCategoryDTO();
        paymentCategoryDTO1.setId(1L);
        PaymentCategoryDTO paymentCategoryDTO2 = new PaymentCategoryDTO();
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO2.setId(paymentCategoryDTO1.getId());
        assertThat(paymentCategoryDTO1).isEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO2.setId(2L);
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO1.setId(null);
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
    }
}
