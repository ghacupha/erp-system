package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmortizationRecurrenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationRecurrence.class);
        AmortizationRecurrence amortizationRecurrence1 = new AmortizationRecurrence();
        amortizationRecurrence1.setId(1L);
        AmortizationRecurrence amortizationRecurrence2 = new AmortizationRecurrence();
        amortizationRecurrence2.setId(amortizationRecurrence1.getId());
        assertThat(amortizationRecurrence1).isEqualTo(amortizationRecurrence2);
        amortizationRecurrence2.setId(2L);
        assertThat(amortizationRecurrence1).isNotEqualTo(amortizationRecurrence2);
        amortizationRecurrence1.setId(null);
        assertThat(amortizationRecurrence1).isNotEqualTo(amortizationRecurrence2);
    }
}
