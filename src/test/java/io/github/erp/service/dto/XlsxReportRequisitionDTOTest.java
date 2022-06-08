package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class XlsxReportRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XlsxReportRequisitionDTO.class);
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO1 = new XlsxReportRequisitionDTO();
        xlsxReportRequisitionDTO1.setId(1L);
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO2 = new XlsxReportRequisitionDTO();
        assertThat(xlsxReportRequisitionDTO1).isNotEqualTo(xlsxReportRequisitionDTO2);
        xlsxReportRequisitionDTO2.setId(xlsxReportRequisitionDTO1.getId());
        assertThat(xlsxReportRequisitionDTO1).isEqualTo(xlsxReportRequisitionDTO2);
        xlsxReportRequisitionDTO2.setId(2L);
        assertThat(xlsxReportRequisitionDTO1).isNotEqualTo(xlsxReportRequisitionDTO2);
        xlsxReportRequisitionDTO1.setId(null);
        assertThat(xlsxReportRequisitionDTO1).isNotEqualTo(xlsxReportRequisitionDTO2);
    }
}
