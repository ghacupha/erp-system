package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UniversallyUniqueMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversallyUniqueMappingDTO.class);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO1 = new UniversallyUniqueMappingDTO();
        universallyUniqueMappingDTO1.setId(1L);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO2 = new UniversallyUniqueMappingDTO();
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO2.setId(universallyUniqueMappingDTO1.getId());
        assertThat(universallyUniqueMappingDTO1).isEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO2.setId(2L);
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO1.setId(null);
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
    }
}
