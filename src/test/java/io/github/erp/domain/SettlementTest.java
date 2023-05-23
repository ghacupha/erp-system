package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Settlement.class);
        Settlement settlement1 = new Settlement();
        settlement1.setId(1L);
        Settlement settlement2 = new Settlement();
        settlement2.setId(settlement1.getId());
        assertThat(settlement1).isEqualTo(settlement2);
        settlement2.setId(2L);
        assertThat(settlement1).isNotEqualTo(settlement2);
        settlement1.setId(null);
        assertThat(settlement1).isNotEqualTo(settlement2);
    }
}
