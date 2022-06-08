package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationMethodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationMethodDTO.class);
        DepreciationMethodDTO depreciationMethodDTO1 = new DepreciationMethodDTO();
        depreciationMethodDTO1.setId(1L);
        DepreciationMethodDTO depreciationMethodDTO2 = new DepreciationMethodDTO();
        assertThat(depreciationMethodDTO1).isNotEqualTo(depreciationMethodDTO2);
        depreciationMethodDTO2.setId(depreciationMethodDTO1.getId());
        assertThat(depreciationMethodDTO1).isEqualTo(depreciationMethodDTO2);
        depreciationMethodDTO2.setId(2L);
        assertThat(depreciationMethodDTO1).isNotEqualTo(depreciationMethodDTO2);
        depreciationMethodDTO1.setId(null);
        assertThat(depreciationMethodDTO1).isNotEqualTo(depreciationMethodDTO2);
    }
}
