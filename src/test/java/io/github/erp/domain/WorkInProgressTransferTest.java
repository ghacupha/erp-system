package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressTransfer.class);
        WorkInProgressTransfer workInProgressTransfer1 = new WorkInProgressTransfer();
        workInProgressTransfer1.setId(1L);
        WorkInProgressTransfer workInProgressTransfer2 = new WorkInProgressTransfer();
        workInProgressTransfer2.setId(workInProgressTransfer1.getId());
        assertThat(workInProgressTransfer1).isEqualTo(workInProgressTransfer2);
        workInProgressTransfer2.setId(2L);
        assertThat(workInProgressTransfer1).isNotEqualTo(workInProgressTransfer2);
        workInProgressTransfer1.setId(null);
        assertThat(workInProgressTransfer1).isNotEqualTo(workInProgressTransfer2);
    }
}
