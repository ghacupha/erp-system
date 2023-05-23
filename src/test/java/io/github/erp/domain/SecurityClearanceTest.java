package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SecurityClearanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClearance.class);
        SecurityClearance securityClearance1 = new SecurityClearance();
        securityClearance1.setId(1L);
        SecurityClearance securityClearance2 = new SecurityClearance();
        securityClearance2.setId(securityClearance1.getId());
        assertThat(securityClearance1).isEqualTo(securityClearance2);
        securityClearance2.setId(2L);
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);
        securityClearance1.setId(null);
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);
    }
}
