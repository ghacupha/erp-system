package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentAmortizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAmortization.class);
        PrepaymentAmortization prepaymentAmortization1 = new PrepaymentAmortization();
        prepaymentAmortization1.setId(1L);
        PrepaymentAmortization prepaymentAmortization2 = new PrepaymentAmortization();
        prepaymentAmortization2.setId(prepaymentAmortization1.getId());
        assertThat(prepaymentAmortization1).isEqualTo(prepaymentAmortization2);
        prepaymentAmortization2.setId(2L);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
        prepaymentAmortization1.setId(null);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
    }
}
