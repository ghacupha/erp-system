package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationPeriodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationPeriodDTO.class);
        DepreciationPeriodDTO depreciationPeriodDTO1 = new DepreciationPeriodDTO();
        depreciationPeriodDTO1.setId(1L);
        DepreciationPeriodDTO depreciationPeriodDTO2 = new DepreciationPeriodDTO();
        assertThat(depreciationPeriodDTO1).isNotEqualTo(depreciationPeriodDTO2);
        depreciationPeriodDTO2.setId(depreciationPeriodDTO1.getId());
        assertThat(depreciationPeriodDTO1).isEqualTo(depreciationPeriodDTO2);
        depreciationPeriodDTO2.setId(2L);
        assertThat(depreciationPeriodDTO1).isNotEqualTo(depreciationPeriodDTO2);
        depreciationPeriodDTO1.setId(null);
        assertThat(depreciationPeriodDTO1).isNotEqualTo(depreciationPeriodDTO2);
    }
}
