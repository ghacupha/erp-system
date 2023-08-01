package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationBatchSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationBatchSequenceDTO.class);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO1 = new DepreciationBatchSequenceDTO();
        depreciationBatchSequenceDTO1.setId(1L);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO2 = new DepreciationBatchSequenceDTO();
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO2.setId(depreciationBatchSequenceDTO1.getId());
        assertThat(depreciationBatchSequenceDTO1).isEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO2.setId(2L);
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO1.setId(null);
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
    }
}
