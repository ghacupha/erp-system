package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessStampTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessStamp.class);
        BusinessStamp businessStamp1 = new BusinessStamp();
        businessStamp1.setId(1L);
        BusinessStamp businessStamp2 = new BusinessStamp();
        businessStamp2.setId(businessStamp1.getId());
        assertThat(businessStamp1).isEqualTo(businessStamp2);
        businessStamp2.setId(2L);
        assertThat(businessStamp1).isNotEqualTo(businessStamp2);
        businessStamp1.setId(null);
        assertThat(businessStamp1).isNotEqualTo(businessStamp2);
    }
}
