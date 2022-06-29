package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcelReportExportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcelReportExportDTO.class);
        ExcelReportExportDTO excelReportExportDTO1 = new ExcelReportExportDTO();
        excelReportExportDTO1.setId(1L);
        ExcelReportExportDTO excelReportExportDTO2 = new ExcelReportExportDTO();
        assertThat(excelReportExportDTO1).isNotEqualTo(excelReportExportDTO2);
        excelReportExportDTO2.setId(excelReportExportDTO1.getId());
        assertThat(excelReportExportDTO1).isEqualTo(excelReportExportDTO2);
        excelReportExportDTO2.setId(2L);
        assertThat(excelReportExportDTO1).isNotEqualTo(excelReportExportDTO2);
        excelReportExportDTO1.setId(null);
        assertThat(excelReportExportDTO1).isNotEqualTo(excelReportExportDTO2);
    }
}
