package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressTransferDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressTransferDTO.class);
        WorkInProgressTransferDTO workInProgressTransferDTO1 = new WorkInProgressTransferDTO();
        workInProgressTransferDTO1.setId(1L);
        WorkInProgressTransferDTO workInProgressTransferDTO2 = new WorkInProgressTransferDTO();
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO2.setId(workInProgressTransferDTO1.getId());
        assertThat(workInProgressTransferDTO1).isEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO2.setId(2L);
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO1.setId(null);
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
    }
}
