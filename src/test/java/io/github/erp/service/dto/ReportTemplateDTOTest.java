package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplateDTO.class);
        ReportTemplateDTO reportTemplateDTO1 = new ReportTemplateDTO();
        reportTemplateDTO1.setId(1L);
        ReportTemplateDTO reportTemplateDTO2 = new ReportTemplateDTO();
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(reportTemplateDTO1.getId());
        assertThat(reportTemplateDTO1).isEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(2L);
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO1.setId(null);
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
    }
}
