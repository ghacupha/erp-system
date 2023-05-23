package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemContentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemContentType.class);
        SystemContentType systemContentType1 = new SystemContentType();
        systemContentType1.setId(1L);
        SystemContentType systemContentType2 = new SystemContentType();
        systemContentType2.setId(systemContentType1.getId());
        assertThat(systemContentType1).isEqualTo(systemContentType2);
        systemContentType2.setId(2L);
        assertThat(systemContentType1).isNotEqualTo(systemContentType2);
        systemContentType1.setId(null);
        assertThat(systemContentType1).isNotEqualTo(systemContentType2);
    }
}
