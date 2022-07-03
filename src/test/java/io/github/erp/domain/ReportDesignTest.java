package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDesignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDesign.class);
        ReportDesign reportDesign1 = new ReportDesign();
        reportDesign1.setId(1L);
        ReportDesign reportDesign2 = new ReportDesign();
        reportDesign2.setId(reportDesign1.getId());
        assertThat(reportDesign1).isEqualTo(reportDesign2);
        reportDesign2.setId(2L);
        assertThat(reportDesign1).isNotEqualTo(reportDesign2);
        reportDesign1.setId(null);
        assertThat(reportDesign1).isNotEqualTo(reportDesign2);
    }
}
