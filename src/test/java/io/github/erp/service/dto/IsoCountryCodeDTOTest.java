package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsoCountryCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsoCountryCodeDTO.class);
        IsoCountryCodeDTO isoCountryCodeDTO1 = new IsoCountryCodeDTO();
        isoCountryCodeDTO1.setId(1L);
        IsoCountryCodeDTO isoCountryCodeDTO2 = new IsoCountryCodeDTO();
        assertThat(isoCountryCodeDTO1).isNotEqualTo(isoCountryCodeDTO2);
        isoCountryCodeDTO2.setId(isoCountryCodeDTO1.getId());
        assertThat(isoCountryCodeDTO1).isEqualTo(isoCountryCodeDTO2);
        isoCountryCodeDTO2.setId(2L);
        assertThat(isoCountryCodeDTO1).isNotEqualTo(isoCountryCodeDTO2);
        isoCountryCodeDTO1.setId(null);
        assertThat(isoCountryCodeDTO1).isNotEqualTo(isoCountryCodeDTO2);
    }
}
