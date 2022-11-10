package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SecurityClearanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClearanceDTO.class);
        SecurityClearanceDTO securityClearanceDTO1 = new SecurityClearanceDTO();
        securityClearanceDTO1.setId(1L);
        SecurityClearanceDTO securityClearanceDTO2 = new SecurityClearanceDTO();
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
        securityClearanceDTO2.setId(securityClearanceDTO1.getId());
        assertThat(securityClearanceDTO1).isEqualTo(securityClearanceDTO2);
        securityClearanceDTO2.setId(2L);
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
        securityClearanceDTO1.setId(null);
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
    }
}
