package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class JobSheetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSheetDTO.class);
        JobSheetDTO jobSheetDTO1 = new JobSheetDTO();
        jobSheetDTO1.setId(1L);
        JobSheetDTO jobSheetDTO2 = new JobSheetDTO();
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
        jobSheetDTO2.setId(jobSheetDTO1.getId());
        assertThat(jobSheetDTO1).isEqualTo(jobSheetDTO2);
        jobSheetDTO2.setId(2L);
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
        jobSheetDTO1.setId(null);
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
    }
}
