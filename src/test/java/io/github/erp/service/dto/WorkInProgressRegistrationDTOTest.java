package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressRegistrationDTO.class);
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO1 = new WorkInProgressRegistrationDTO();
        workInProgressRegistrationDTO1.setId(1L);
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO2 = new WorkInProgressRegistrationDTO();
        assertThat(workInProgressRegistrationDTO1).isNotEqualTo(workInProgressRegistrationDTO2);
        workInProgressRegistrationDTO2.setId(workInProgressRegistrationDTO1.getId());
        assertThat(workInProgressRegistrationDTO1).isEqualTo(workInProgressRegistrationDTO2);
        workInProgressRegistrationDTO2.setId(2L);
        assertThat(workInProgressRegistrationDTO1).isNotEqualTo(workInProgressRegistrationDTO2);
        workInProgressRegistrationDTO1.setId(null);
        assertThat(workInProgressRegistrationDTO1).isNotEqualTo(workInProgressRegistrationDTO2);
    }
}
