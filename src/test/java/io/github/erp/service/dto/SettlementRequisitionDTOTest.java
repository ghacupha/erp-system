package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettlementRequisitionDTO.class);
        SettlementRequisitionDTO settlementRequisitionDTO1 = new SettlementRequisitionDTO();
        settlementRequisitionDTO1.setId(1L);
        SettlementRequisitionDTO settlementRequisitionDTO2 = new SettlementRequisitionDTO();
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO2.setId(settlementRequisitionDTO1.getId());
        assertThat(settlementRequisitionDTO1).isEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO2.setId(2L);
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO1.setId(null);
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
    }
}
