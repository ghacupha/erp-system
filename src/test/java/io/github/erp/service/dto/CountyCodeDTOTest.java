package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class CountyCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyCodeDTO.class);
        CountyCodeDTO countyCodeDTO1 = new CountyCodeDTO();
        countyCodeDTO1.setId(1L);
        CountyCodeDTO countyCodeDTO2 = new CountyCodeDTO();
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
        countyCodeDTO2.setId(countyCodeDTO1.getId());
        assertThat(countyCodeDTO1).isEqualTo(countyCodeDTO2);
        countyCodeDTO2.setId(2L);
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
        countyCodeDTO1.setId(null);
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
    }
}
