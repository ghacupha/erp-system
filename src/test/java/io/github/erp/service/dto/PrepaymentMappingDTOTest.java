package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMappingDTO.class);
        PrepaymentMappingDTO prepaymentMappingDTO1 = new PrepaymentMappingDTO();
        prepaymentMappingDTO1.setId(1L);
        PrepaymentMappingDTO prepaymentMappingDTO2 = new PrepaymentMappingDTO();
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO2.setId(prepaymentMappingDTO1.getId());
        assertThat(prepaymentMappingDTO1).isEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO2.setId(2L);
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO1.setId(null);
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
    }
}
