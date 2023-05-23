package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementCurrencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettlementCurrency.class);
        SettlementCurrency settlementCurrency1 = new SettlementCurrency();
        settlementCurrency1.setId(1L);
        SettlementCurrency settlementCurrency2 = new SettlementCurrency();
        settlementCurrency2.setId(settlementCurrency1.getId());
        assertThat(settlementCurrency1).isEqualTo(settlementCurrency2);
        settlementCurrency2.setId(2L);
        assertThat(settlementCurrency1).isNotEqualTo(settlementCurrency2);
        settlementCurrency1.setId(null);
        assertThat(settlementCurrency1).isNotEqualTo(settlementCurrency2);
    }
}
