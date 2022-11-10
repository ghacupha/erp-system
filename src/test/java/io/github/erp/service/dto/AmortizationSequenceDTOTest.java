package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmortizationSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationSequenceDTO.class);
        AmortizationSequenceDTO amortizationSequenceDTO1 = new AmortizationSequenceDTO();
        amortizationSequenceDTO1.setId(1L);
        AmortizationSequenceDTO amortizationSequenceDTO2 = new AmortizationSequenceDTO();
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO2.setId(amortizationSequenceDTO1.getId());
        assertThat(amortizationSequenceDTO1).isEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO2.setId(2L);
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO1.setId(null);
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
    }
}
