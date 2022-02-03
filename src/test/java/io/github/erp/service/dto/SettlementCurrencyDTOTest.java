package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementCurrencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettlementCurrencyDTO.class);
        SettlementCurrencyDTO settlementCurrencyDTO1 = new SettlementCurrencyDTO();
        settlementCurrencyDTO1.setId(1L);
        SettlementCurrencyDTO settlementCurrencyDTO2 = new SettlementCurrencyDTO();
        assertThat(settlementCurrencyDTO1).isNotEqualTo(settlementCurrencyDTO2);
        settlementCurrencyDTO2.setId(settlementCurrencyDTO1.getId());
        assertThat(settlementCurrencyDTO1).isEqualTo(settlementCurrencyDTO2);
        settlementCurrencyDTO2.setId(2L);
        assertThat(settlementCurrencyDTO1).isNotEqualTo(settlementCurrencyDTO2);
        settlementCurrencyDTO1.setId(null);
        assertThat(settlementCurrencyDTO1).isNotEqualTo(settlementCurrencyDTO2);
    }
}
