package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class CountyCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyCode.class);
        CountyCode countyCode1 = new CountyCode();
        countyCode1.setId(1L);
        CountyCode countyCode2 = new CountyCode();
        countyCode2.setId(countyCode1.getId());
        assertThat(countyCode1).isEqualTo(countyCode2);
        countyCode2.setId(2L);
        assertThat(countyCode1).isNotEqualTo(countyCode2);
        countyCode1.setId(null);
        assertThat(countyCode1).isNotEqualTo(countyCode2);
    }
}
