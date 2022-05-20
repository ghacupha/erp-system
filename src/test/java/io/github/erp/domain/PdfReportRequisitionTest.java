package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PdfReportRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PdfReportRequisition.class);
        PdfReportRequisition pdfReportRequisition1 = new PdfReportRequisition();
        pdfReportRequisition1.setId(1L);
        PdfReportRequisition pdfReportRequisition2 = new PdfReportRequisition();
        pdfReportRequisition2.setId(pdfReportRequisition1.getId());
        assertThat(pdfReportRequisition1).isEqualTo(pdfReportRequisition2);
        pdfReportRequisition2.setId(2L);
        assertThat(pdfReportRequisition1).isNotEqualTo(pdfReportRequisition2);
        pdfReportRequisition1.setId(null);
        assertThat(pdfReportRequisition1).isNotEqualTo(pdfReportRequisition2);
    }
}
