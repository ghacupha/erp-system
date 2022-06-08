package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class InstitutionCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionCodeDTO.class);
        InstitutionCodeDTO institutionCodeDTO1 = new InstitutionCodeDTO();
        institutionCodeDTO1.setId(1L);
        InstitutionCodeDTO institutionCodeDTO2 = new InstitutionCodeDTO();
        assertThat(institutionCodeDTO1).isNotEqualTo(institutionCodeDTO2);
        institutionCodeDTO2.setId(institutionCodeDTO1.getId());
        assertThat(institutionCodeDTO1).isEqualTo(institutionCodeDTO2);
        institutionCodeDTO2.setId(2L);
        assertThat(institutionCodeDTO1).isNotEqualTo(institutionCodeDTO2);
        institutionCodeDTO1.setId(null);
        assertThat(institutionCodeDTO1).isNotEqualTo(institutionCodeDTO2);
    }
}
