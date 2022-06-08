package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceOutletDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOutletDTO.class);
        ServiceOutletDTO serviceOutletDTO1 = new ServiceOutletDTO();
        serviceOutletDTO1.setId(1L);
        ServiceOutletDTO serviceOutletDTO2 = new ServiceOutletDTO();
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
        serviceOutletDTO2.setId(serviceOutletDTO1.getId());
        assertThat(serviceOutletDTO1).isEqualTo(serviceOutletDTO2);
        serviceOutletDTO2.setId(2L);
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
        serviceOutletDTO1.setId(null);
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
    }
}
