package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessStatus.class);
        ProcessStatus processStatus1 = new ProcessStatus();
        processStatus1.setId(1L);
        ProcessStatus processStatus2 = new ProcessStatus();
        processStatus2.setId(processStatus1.getId());
        assertThat(processStatus1).isEqualTo(processStatus2);
        processStatus2.setId(2L);
        assertThat(processStatus1).isNotEqualTo(processStatus2);
        processStatus1.setId(null);
        assertThat(processStatus1).isNotEqualTo(processStatus2);
    }
}
