package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemModuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemModule.class);
        SystemModule systemModule1 = new SystemModule();
        systemModule1.setId(1L);
        SystemModule systemModule2 = new SystemModule();
        systemModule2.setId(systemModule1.getId());
        assertThat(systemModule1).isEqualTo(systemModule2);
        systemModule2.setId(2L);
        assertThat(systemModule1).isNotEqualTo(systemModule2);
        systemModule1.setId(null);
        assertThat(systemModule1).isNotEqualTo(systemModule2);
    }
}
