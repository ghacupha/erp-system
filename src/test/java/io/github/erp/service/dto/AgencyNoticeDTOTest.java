package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyNoticeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyNoticeDTO.class);
        AgencyNoticeDTO agencyNoticeDTO1 = new AgencyNoticeDTO();
        agencyNoticeDTO1.setId(1L);
        AgencyNoticeDTO agencyNoticeDTO2 = new AgencyNoticeDTO();
        assertThat(agencyNoticeDTO1).isNotEqualTo(agencyNoticeDTO2);
        agencyNoticeDTO2.setId(agencyNoticeDTO1.getId());
        assertThat(agencyNoticeDTO1).isEqualTo(agencyNoticeDTO2);
        agencyNoticeDTO2.setId(2L);
        assertThat(agencyNoticeDTO1).isNotEqualTo(agencyNoticeDTO2);
        agencyNoticeDTO1.setId(null);
        assertThat(agencyNoticeDTO1).isNotEqualTo(agencyNoticeDTO2);
    }
}
