package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class OutletStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutletStatus.class);
        OutletStatus outletStatus1 = new OutletStatus();
        outletStatus1.setId(1L);
        OutletStatus outletStatus2 = new OutletStatus();
        outletStatus2.setId(outletStatus1.getId());
        assertThat(outletStatus1).isEqualTo(outletStatus2);
        outletStatus2.setId(2L);
        assertThat(outletStatus1).isNotEqualTo(outletStatus2);
        outletStatus1.setId(null);
        assertThat(outletStatus1).isNotEqualTo(outletStatus2);
    }
}
