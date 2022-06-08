package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkProjectRegisterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkProjectRegisterDTO.class);
        WorkProjectRegisterDTO workProjectRegisterDTO1 = new WorkProjectRegisterDTO();
        workProjectRegisterDTO1.setId(1L);
        WorkProjectRegisterDTO workProjectRegisterDTO2 = new WorkProjectRegisterDTO();
        assertThat(workProjectRegisterDTO1).isNotEqualTo(workProjectRegisterDTO2);
        workProjectRegisterDTO2.setId(workProjectRegisterDTO1.getId());
        assertThat(workProjectRegisterDTO1).isEqualTo(workProjectRegisterDTO2);
        workProjectRegisterDTO2.setId(2L);
        assertThat(workProjectRegisterDTO1).isNotEqualTo(workProjectRegisterDTO2);
        workProjectRegisterDTO1.setId(null);
        assertThat(workProjectRegisterDTO1).isNotEqualTo(workProjectRegisterDTO2);
    }
}
