package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmortizationSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationSequence.class);
        AmortizationSequence amortizationSequence1 = new AmortizationSequence();
        amortizationSequence1.setId(1L);
        AmortizationSequence amortizationSequence2 = new AmortizationSequence();
        amortizationSequence2.setId(amortizationSequence1.getId());
        assertThat(amortizationSequence1).isEqualTo(amortizationSequence2);
        amortizationSequence2.setId(2L);
        assertThat(amortizationSequence1).isNotEqualTo(amortizationSequence2);
        amortizationSequence1.setId(null);
        assertThat(amortizationSequence1).isNotEqualTo(amortizationSequence2);
    }
}
