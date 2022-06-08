package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationMethod.class);
        DepreciationMethod depreciationMethod1 = new DepreciationMethod();
        depreciationMethod1.setId(1L);
        DepreciationMethod depreciationMethod2 = new DepreciationMethod();
        depreciationMethod2.setId(depreciationMethod1.getId());
        assertThat(depreciationMethod1).isEqualTo(depreciationMethod2);
        depreciationMethod2.setId(2L);
        assertThat(depreciationMethod1).isNotEqualTo(depreciationMethod2);
        depreciationMethod1.setId(null);
        assertThat(depreciationMethod1).isNotEqualTo(depreciationMethod2);
    }
}
