package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class OutletTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutletTypeDTO.class);
        OutletTypeDTO outletTypeDTO1 = new OutletTypeDTO();
        outletTypeDTO1.setId(1L);
        OutletTypeDTO outletTypeDTO2 = new OutletTypeDTO();
        assertThat(outletTypeDTO1).isNotEqualTo(outletTypeDTO2);
        outletTypeDTO2.setId(outletTypeDTO1.getId());
        assertThat(outletTypeDTO1).isEqualTo(outletTypeDTO2);
        outletTypeDTO2.setId(2L);
        assertThat(outletTypeDTO1).isNotEqualTo(outletTypeDTO2);
        outletTypeDTO1.setId(null);
        assertThat(outletTypeDTO1).isNotEqualTo(outletTypeDTO2);
    }
}
