package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportStatusDTO.class);
        ReportStatusDTO reportStatusDTO1 = new ReportStatusDTO();
        reportStatusDTO1.setId(1L);
        ReportStatusDTO reportStatusDTO2 = new ReportStatusDTO();
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
        reportStatusDTO2.setId(reportStatusDTO1.getId());
        assertThat(reportStatusDTO1).isEqualTo(reportStatusDTO2);
        reportStatusDTO2.setId(2L);
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
        reportStatusDTO1.setId(null);
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
    }
}
