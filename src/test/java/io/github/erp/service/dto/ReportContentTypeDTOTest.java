package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportContentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportContentTypeDTO.class);
        ReportContentTypeDTO reportContentTypeDTO1 = new ReportContentTypeDTO();
        reportContentTypeDTO1.setId(1L);
        ReportContentTypeDTO reportContentTypeDTO2 = new ReportContentTypeDTO();
        assertThat(reportContentTypeDTO1).isNotEqualTo(reportContentTypeDTO2);
        reportContentTypeDTO2.setId(reportContentTypeDTO1.getId());
        assertThat(reportContentTypeDTO1).isEqualTo(reportContentTypeDTO2);
        reportContentTypeDTO2.setId(2L);
        assertThat(reportContentTypeDTO1).isNotEqualTo(reportContentTypeDTO2);
        reportContentTypeDTO1.setId(null);
        assertThat(reportContentTypeDTO1).isNotEqualTo(reportContentTypeDTO2);
    }
}
