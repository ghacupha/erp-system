package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutletStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutletStatusDTO.class);
        OutletStatusDTO outletStatusDTO1 = new OutletStatusDTO();
        outletStatusDTO1.setId(1L);
        OutletStatusDTO outletStatusDTO2 = new OutletStatusDTO();
        assertThat(outletStatusDTO1).isNotEqualTo(outletStatusDTO2);
        outletStatusDTO2.setId(outletStatusDTO1.getId());
        assertThat(outletStatusDTO1).isEqualTo(outletStatusDTO2);
        outletStatusDTO2.setId(2L);
        assertThat(outletStatusDTO1).isNotEqualTo(outletStatusDTO2);
        outletStatusDTO1.setId(null);
        assertThat(outletStatusDTO1).isNotEqualTo(outletStatusDTO2);
    }
}
