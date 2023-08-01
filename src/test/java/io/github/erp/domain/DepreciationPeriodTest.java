package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationPeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationPeriod.class);
        DepreciationPeriod depreciationPeriod1 = new DepreciationPeriod();
        depreciationPeriod1.setId(1L);
        DepreciationPeriod depreciationPeriod2 = new DepreciationPeriod();
        depreciationPeriod2.setId(depreciationPeriod1.getId());
        assertThat(depreciationPeriod1).isEqualTo(depreciationPeriod2);
        depreciationPeriod2.setId(2L);
        assertThat(depreciationPeriod1).isNotEqualTo(depreciationPeriod2);
        depreciationPeriod1.setId(null);
        assertThat(depreciationPeriod1).isNotEqualTo(depreciationPeriod2);
    }
}
