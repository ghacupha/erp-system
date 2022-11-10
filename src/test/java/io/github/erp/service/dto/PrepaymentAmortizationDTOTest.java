package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentAmortizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAmortizationDTO.class);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO1 = new PrepaymentAmortizationDTO();
        prepaymentAmortizationDTO1.setId(1L);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO2 = new PrepaymentAmortizationDTO();
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO2.setId(prepaymentAmortizationDTO1.getId());
        assertThat(prepaymentAmortizationDTO1).isEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO2.setId(2L);
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO1.setId(null);
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
    }
}
