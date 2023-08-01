package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationJob.class);
        DepreciationJob depreciationJob1 = new DepreciationJob();
        depreciationJob1.setId(1L);
        DepreciationJob depreciationJob2 = new DepreciationJob();
        depreciationJob2.setId(depreciationJob1.getId());
        assertThat(depreciationJob1).isEqualTo(depreciationJob2);
        depreciationJob2.setId(2L);
        assertThat(depreciationJob1).isNotEqualTo(depreciationJob2);
        depreciationJob1.setId(null);
        assertThat(depreciationJob1).isNotEqualTo(depreciationJob2);
    }
}
