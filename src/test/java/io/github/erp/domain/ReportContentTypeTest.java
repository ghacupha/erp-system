package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportContentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportContentType.class);
        ReportContentType reportContentType1 = new ReportContentType();
        reportContentType1.setId(1L);
        ReportContentType reportContentType2 = new ReportContentType();
        reportContentType2.setId(reportContentType1.getId());
        assertThat(reportContentType1).isEqualTo(reportContentType2);
        reportContentType2.setId(2L);
        assertThat(reportContentType1).isNotEqualTo(reportContentType2);
        reportContentType1.setId(null);
        assertThat(reportContentType1).isNotEqualTo(reportContentType2);
    }
}
