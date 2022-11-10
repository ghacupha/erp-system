package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessStampDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessStampDTO.class);
        BusinessStampDTO businessStampDTO1 = new BusinessStampDTO();
        businessStampDTO1.setId(1L);
        BusinessStampDTO businessStampDTO2 = new BusinessStampDTO();
        assertThat(businessStampDTO1).isNotEqualTo(businessStampDTO2);
        businessStampDTO2.setId(businessStampDTO1.getId());
        assertThat(businessStampDTO1).isEqualTo(businessStampDTO2);
        businessStampDTO2.setId(2L);
        assertThat(businessStampDTO1).isNotEqualTo(businessStampDTO2);
        businessStampDTO1.setId(null);
        assertThat(businessStampDTO1).isNotEqualTo(businessStampDTO2);
    }
}
