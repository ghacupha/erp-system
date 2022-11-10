package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessStatusDTO.class);
        ProcessStatusDTO processStatusDTO1 = new ProcessStatusDTO();
        processStatusDTO1.setId(1L);
        ProcessStatusDTO processStatusDTO2 = new ProcessStatusDTO();
        assertThat(processStatusDTO1).isNotEqualTo(processStatusDTO2);
        processStatusDTO2.setId(processStatusDTO1.getId());
        assertThat(processStatusDTO1).isEqualTo(processStatusDTO2);
        processStatusDTO2.setId(2L);
        assertThat(processStatusDTO1).isNotEqualTo(processStatusDTO2);
        processStatusDTO1.setId(null);
        assertThat(processStatusDTO1).isNotEqualTo(processStatusDTO2);
    }
}
