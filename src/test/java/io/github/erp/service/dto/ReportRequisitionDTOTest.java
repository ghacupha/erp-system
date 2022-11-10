package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportRequisitionDTO.class);
        ReportRequisitionDTO reportRequisitionDTO1 = new ReportRequisitionDTO();
        reportRequisitionDTO1.setId(1L);
        ReportRequisitionDTO reportRequisitionDTO2 = new ReportRequisitionDTO();
        assertThat(reportRequisitionDTO1).isNotEqualTo(reportRequisitionDTO2);
        reportRequisitionDTO2.setId(reportRequisitionDTO1.getId());
        assertThat(reportRequisitionDTO1).isEqualTo(reportRequisitionDTO2);
        reportRequisitionDTO2.setId(2L);
        assertThat(reportRequisitionDTO1).isNotEqualTo(reportRequisitionDTO2);
        reportRequisitionDTO1.setId(null);
        assertThat(reportRequisitionDTO1).isNotEqualTo(reportRequisitionDTO2);
    }
}
