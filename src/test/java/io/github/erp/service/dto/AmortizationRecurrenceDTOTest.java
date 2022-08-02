package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmortizationRecurrenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationRecurrenceDTO.class);
        AmortizationRecurrenceDTO amortizationRecurrenceDTO1 = new AmortizationRecurrenceDTO();
        amortizationRecurrenceDTO1.setId(1L);
        AmortizationRecurrenceDTO amortizationRecurrenceDTO2 = new AmortizationRecurrenceDTO();
        assertThat(amortizationRecurrenceDTO1).isNotEqualTo(amortizationRecurrenceDTO2);
        amortizationRecurrenceDTO2.setId(amortizationRecurrenceDTO1.getId());
        assertThat(amortizationRecurrenceDTO1).isEqualTo(amortizationRecurrenceDTO2);
        amortizationRecurrenceDTO2.setId(2L);
        assertThat(amortizationRecurrenceDTO1).isNotEqualTo(amortizationRecurrenceDTO2);
        amortizationRecurrenceDTO1.setId(null);
        assertThat(amortizationRecurrenceDTO1).isNotEqualTo(amortizationRecurrenceDTO2);
    }
}
