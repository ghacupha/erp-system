package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubCountyCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCountyCodeDTO.class);
        SubCountyCodeDTO subCountyCodeDTO1 = new SubCountyCodeDTO();
        subCountyCodeDTO1.setId(1L);
        SubCountyCodeDTO subCountyCodeDTO2 = new SubCountyCodeDTO();
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO2.setId(subCountyCodeDTO1.getId());
        assertThat(subCountyCodeDTO1).isEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO2.setId(2L);
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO1.setId(null);
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
    }
}
