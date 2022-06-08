package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionCode.class);
        InstitutionCode institutionCode1 = new InstitutionCode();
        institutionCode1.setId(1L);
        InstitutionCode institutionCode2 = new InstitutionCode();
        institutionCode2.setId(institutionCode1.getId());
        assertThat(institutionCode1).isEqualTo(institutionCode2);
        institutionCode2.setId(2L);
        assertThat(institutionCode1).isNotEqualTo(institutionCode2);
        institutionCode1.setId(null);
        assertThat(institutionCode1).isNotEqualTo(institutionCode2);
    }
}
