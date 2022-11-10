package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PdfReportRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PdfReportRequisitionDTO.class);
        PdfReportRequisitionDTO pdfReportRequisitionDTO1 = new PdfReportRequisitionDTO();
        pdfReportRequisitionDTO1.setId(1L);
        PdfReportRequisitionDTO pdfReportRequisitionDTO2 = new PdfReportRequisitionDTO();
        assertThat(pdfReportRequisitionDTO1).isNotEqualTo(pdfReportRequisitionDTO2);
        pdfReportRequisitionDTO2.setId(pdfReportRequisitionDTO1.getId());
        assertThat(pdfReportRequisitionDTO1).isEqualTo(pdfReportRequisitionDTO2);
        pdfReportRequisitionDTO2.setId(2L);
        assertThat(pdfReportRequisitionDTO1).isNotEqualTo(pdfReportRequisitionDTO2);
        pdfReportRequisitionDTO1.setId(null);
        assertThat(pdfReportRequisitionDTO1).isNotEqualTo(pdfReportRequisitionDTO2);
    }
}
