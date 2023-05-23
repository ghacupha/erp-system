package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MfbBranchCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MfbBranchCode.class);
        MfbBranchCode mfbBranchCode1 = new MfbBranchCode();
        mfbBranchCode1.setId(1L);
        MfbBranchCode mfbBranchCode2 = new MfbBranchCode();
        mfbBranchCode2.setId(mfbBranchCode1.getId());
        assertThat(mfbBranchCode1).isEqualTo(mfbBranchCode2);
        mfbBranchCode2.setId(2L);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
        mfbBranchCode1.setId(null);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
    }
}
