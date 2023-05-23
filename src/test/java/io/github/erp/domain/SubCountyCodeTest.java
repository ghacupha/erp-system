package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubCountyCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCountyCode.class);
        SubCountyCode subCountyCode1 = new SubCountyCode();
        subCountyCode1.setId(1L);
        SubCountyCode subCountyCode2 = new SubCountyCode();
        subCountyCode2.setId(subCountyCode1.getId());
        assertThat(subCountyCode1).isEqualTo(subCountyCode2);
        subCountyCode2.setId(2L);
        assertThat(subCountyCode1).isNotEqualTo(subCountyCode2);
        subCountyCode1.setId(null);
        assertThat(subCountyCode1).isNotEqualTo(subCountyCode2);
    }
}
