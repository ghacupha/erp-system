package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutletTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutletType.class);
        OutletType outletType1 = new OutletType();
        outletType1.setId(1L);
        OutletType outletType2 = new OutletType();
        outletType2.setId(outletType1.getId());
        assertThat(outletType1).isEqualTo(outletType2);
        outletType2.setId(2L);
        assertThat(outletType1).isNotEqualTo(outletType2);
        outletType1.setId(null);
        assertThat(outletType1).isNotEqualTo(outletType2);
    }
}
