package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcelReportExportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcelReportExport.class);
        ExcelReportExport excelReportExport1 = new ExcelReportExport();
        excelReportExport1.setId(1L);
        ExcelReportExport excelReportExport2 = new ExcelReportExport();
        excelReportExport2.setId(excelReportExport1.getId());
        assertThat(excelReportExport1).isEqualTo(excelReportExport2);
        excelReportExport2.setId(2L);
        assertThat(excelReportExport1).isNotEqualTo(excelReportExport2);
        excelReportExport1.setId(null);
        assertThat(excelReportExport1).isNotEqualTo(excelReportExport2);
    }
}
