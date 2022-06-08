package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class ReportTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplate.class);
        ReportTemplate reportTemplate1 = new ReportTemplate();
        reportTemplate1.setId(1L);
        ReportTemplate reportTemplate2 = new ReportTemplate();
        reportTemplate2.setId(reportTemplate1.getId());
        assertThat(reportTemplate1).isEqualTo(reportTemplate2);
        reportTemplate2.setId(2L);
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);
        reportTemplate1.setId(null);
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);
    }
}
