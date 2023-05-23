package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettlementRequisition.class);
        SettlementRequisition settlementRequisition1 = new SettlementRequisition();
        settlementRequisition1.setId(1L);
        SettlementRequisition settlementRequisition2 = new SettlementRequisition();
        settlementRequisition2.setId(settlementRequisition1.getId());
        assertThat(settlementRequisition1).isEqualTo(settlementRequisition2);
        settlementRequisition2.setId(2L);
        assertThat(settlementRequisition1).isNotEqualTo(settlementRequisition2);
        settlementRequisition1.setId(null);
        assertThat(settlementRequisition1).isNotEqualTo(settlementRequisition2);
    }
}
