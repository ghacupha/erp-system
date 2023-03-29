package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseLiabilityScheduleItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseLiabilityScheduleItemDTO.class);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO1 = new LeaseLiabilityScheduleItemDTO();
        leaseLiabilityScheduleItemDTO1.setId(1L);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO2 = new LeaseLiabilityScheduleItemDTO();
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO2.setId(leaseLiabilityScheduleItemDTO1.getId());
        assertThat(leaseLiabilityScheduleItemDTO1).isEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO2.setId(2L);
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO1.setId(null);
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
    }
}
