package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationBatchSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationBatchSequence.class);
        DepreciationBatchSequence depreciationBatchSequence1 = new DepreciationBatchSequence();
        depreciationBatchSequence1.setId(1L);
        DepreciationBatchSequence depreciationBatchSequence2 = new DepreciationBatchSequence();
        depreciationBatchSequence2.setId(depreciationBatchSequence1.getId());
        assertThat(depreciationBatchSequence1).isEqualTo(depreciationBatchSequence2);
        depreciationBatchSequence2.setId(2L);
        assertThat(depreciationBatchSequence1).isNotEqualTo(depreciationBatchSequence2);
        depreciationBatchSequence1.setId(null);
        assertThat(depreciationBatchSequence1).isNotEqualTo(depreciationBatchSequence2);
    }
}
