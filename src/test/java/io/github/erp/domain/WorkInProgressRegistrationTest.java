package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressRegistration.class);
        WorkInProgressRegistration workInProgressRegistration1 = new WorkInProgressRegistration();
        workInProgressRegistration1.setId(1L);
        WorkInProgressRegistration workInProgressRegistration2 = new WorkInProgressRegistration();
        workInProgressRegistration2.setId(workInProgressRegistration1.getId());
        assertThat(workInProgressRegistration1).isEqualTo(workInProgressRegistration2);
        workInProgressRegistration2.setId(2L);
        assertThat(workInProgressRegistration1).isNotEqualTo(workInProgressRegistration2);
        workInProgressRegistration1.setId(null);
        assertThat(workInProgressRegistration1).isNotEqualTo(workInProgressRegistration2);
    }
}
