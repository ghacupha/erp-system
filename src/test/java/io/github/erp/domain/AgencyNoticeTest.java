package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyNoticeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyNotice.class);
        AgencyNotice agencyNotice1 = new AgencyNotice();
        agencyNotice1.setId(1L);
        AgencyNotice agencyNotice2 = new AgencyNotice();
        agencyNotice2.setId(agencyNotice1.getId());
        assertThat(agencyNotice1).isEqualTo(agencyNotice2);
        agencyNotice2.setId(2L);
        assertThat(agencyNotice1).isNotEqualTo(agencyNotice2);
        agencyNotice1.setId(null);
        assertThat(agencyNotice1).isNotEqualTo(agencyNotice2);
    }
}
