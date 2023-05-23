package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseLiabilityScheduleItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseLiabilityScheduleItem.class);
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem1 = new LeaseLiabilityScheduleItem();
        leaseLiabilityScheduleItem1.setId(1L);
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem2 = new LeaseLiabilityScheduleItem();
        leaseLiabilityScheduleItem2.setId(leaseLiabilityScheduleItem1.getId());
        assertThat(leaseLiabilityScheduleItem1).isEqualTo(leaseLiabilityScheduleItem2);
        leaseLiabilityScheduleItem2.setId(2L);
        assertThat(leaseLiabilityScheduleItem1).isNotEqualTo(leaseLiabilityScheduleItem2);
        leaseLiabilityScheduleItem1.setId(null);
        assertThat(leaseLiabilityScheduleItem1).isNotEqualTo(leaseLiabilityScheduleItem2);
    }
}
