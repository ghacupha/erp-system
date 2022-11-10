package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemContentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemContentTypeDTO.class);
        SystemContentTypeDTO systemContentTypeDTO1 = new SystemContentTypeDTO();
        systemContentTypeDTO1.setId(1L);
        SystemContentTypeDTO systemContentTypeDTO2 = new SystemContentTypeDTO();
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO2.setId(systemContentTypeDTO1.getId());
        assertThat(systemContentTypeDTO1).isEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO2.setId(2L);
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO1.setId(null);
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
    }
}
