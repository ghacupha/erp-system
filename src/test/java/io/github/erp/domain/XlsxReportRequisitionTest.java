package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class XlsxReportRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XlsxReportRequisition.class);
        XlsxReportRequisition xlsxReportRequisition1 = new XlsxReportRequisition();
        xlsxReportRequisition1.setId(1L);
        XlsxReportRequisition xlsxReportRequisition2 = new XlsxReportRequisition();
        xlsxReportRequisition2.setId(xlsxReportRequisition1.getId());
        assertThat(xlsxReportRequisition1).isEqualTo(xlsxReportRequisition2);
        xlsxReportRequisition2.setId(2L);
        assertThat(xlsxReportRequisition1).isNotEqualTo(xlsxReportRequisition2);
        xlsxReportRequisition1.setId(null);
        assertThat(xlsxReportRequisition1).isNotEqualTo(xlsxReportRequisition2);
    }
}
