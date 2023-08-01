package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationJobDTO.class);
        DepreciationJobDTO depreciationJobDTO1 = new DepreciationJobDTO();
        depreciationJobDTO1.setId(1L);
        DepreciationJobDTO depreciationJobDTO2 = new DepreciationJobDTO();
        assertThat(depreciationJobDTO1).isNotEqualTo(depreciationJobDTO2);
        depreciationJobDTO2.setId(depreciationJobDTO1.getId());
        assertThat(depreciationJobDTO1).isEqualTo(depreciationJobDTO2);
        depreciationJobDTO2.setId(2L);
        assertThat(depreciationJobDTO1).isNotEqualTo(depreciationJobDTO2);
        depreciationJobDTO1.setId(null);
        assertThat(depreciationJobDTO1).isNotEqualTo(depreciationJobDTO2);
    }
}
