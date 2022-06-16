package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportRequisition.class);
        ReportRequisition reportRequisition1 = new ReportRequisition();
        reportRequisition1.setId(1L);
        ReportRequisition reportRequisition2 = new ReportRequisition();
        reportRequisition2.setId(reportRequisition1.getId());
        assertThat(reportRequisition1).isEqualTo(reportRequisition2);
        reportRequisition2.setId(2L);
        assertThat(reportRequisition1).isNotEqualTo(reportRequisition2);
        reportRequisition1.setId(null);
        assertThat(reportRequisition1).isNotEqualTo(reportRequisition2);
    }
}
