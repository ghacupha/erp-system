package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemModuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemModuleDTO.class);
        SystemModuleDTO systemModuleDTO1 = new SystemModuleDTO();
        systemModuleDTO1.setId(1L);
        SystemModuleDTO systemModuleDTO2 = new SystemModuleDTO();
        assertThat(systemModuleDTO1).isNotEqualTo(systemModuleDTO2);
        systemModuleDTO2.setId(systemModuleDTO1.getId());
        assertThat(systemModuleDTO1).isEqualTo(systemModuleDTO2);
        systemModuleDTO2.setId(2L);
        assertThat(systemModuleDTO1).isNotEqualTo(systemModuleDTO2);
        systemModuleDTO1.setId(null);
        assertThat(systemModuleDTO1).isNotEqualTo(systemModuleDTO2);
    }
}
