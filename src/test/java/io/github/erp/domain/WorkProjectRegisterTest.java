package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkProjectRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkProjectRegister.class);
        WorkProjectRegister workProjectRegister1 = new WorkProjectRegister();
        workProjectRegister1.setId(1L);
        WorkProjectRegister workProjectRegister2 = new WorkProjectRegister();
        workProjectRegister2.setId(workProjectRegister1.getId());
        assertThat(workProjectRegister1).isEqualTo(workProjectRegister2);
        workProjectRegister2.setId(2L);
        assertThat(workProjectRegister1).isNotEqualTo(workProjectRegister2);
        workProjectRegister1.setId(null);
        assertThat(workProjectRegister1).isNotEqualTo(workProjectRegister2);
    }
}
