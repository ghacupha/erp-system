package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MfbBranchCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MfbBranchCodeDTO.class);
        MfbBranchCodeDTO mfbBranchCodeDTO1 = new MfbBranchCodeDTO();
        mfbBranchCodeDTO1.setId(1L);
        MfbBranchCodeDTO mfbBranchCodeDTO2 = new MfbBranchCodeDTO();
        assertThat(mfbBranchCodeDTO1).isNotEqualTo(mfbBranchCodeDTO2);
        mfbBranchCodeDTO2.setId(mfbBranchCodeDTO1.getId());
        assertThat(mfbBranchCodeDTO1).isEqualTo(mfbBranchCodeDTO2);
        mfbBranchCodeDTO2.setId(2L);
        assertThat(mfbBranchCodeDTO1).isNotEqualTo(mfbBranchCodeDTO2);
        mfbBranchCodeDTO1.setId(null);
        assertThat(mfbBranchCodeDTO1).isNotEqualTo(mfbBranchCodeDTO2);
    }
}
