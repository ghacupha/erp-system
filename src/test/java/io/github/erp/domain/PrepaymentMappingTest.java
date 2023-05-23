package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMapping.class);
        PrepaymentMapping prepaymentMapping1 = new PrepaymentMapping();
        prepaymentMapping1.setId(1L);
        PrepaymentMapping prepaymentMapping2 = new PrepaymentMapping();
        prepaymentMapping2.setId(prepaymentMapping1.getId());
        assertThat(prepaymentMapping1).isEqualTo(prepaymentMapping2);
        prepaymentMapping2.setId(2L);
        assertThat(prepaymentMapping1).isNotEqualTo(prepaymentMapping2);
        prepaymentMapping1.setId(null);
        assertThat(prepaymentMapping1).isNotEqualTo(prepaymentMapping2);
    }
}
