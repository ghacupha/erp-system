package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationEntry.class);
        DepreciationEntry depreciationEntry1 = new DepreciationEntry();
        depreciationEntry1.setId(1L);
        DepreciationEntry depreciationEntry2 = new DepreciationEntry();
        depreciationEntry2.setId(depreciationEntry1.getId());
        assertThat(depreciationEntry1).isEqualTo(depreciationEntry2);
        depreciationEntry2.setId(2L);
        assertThat(depreciationEntry1).isNotEqualTo(depreciationEntry2);
        depreciationEntry1.setId(null);
        assertThat(depreciationEntry1).isNotEqualTo(depreciationEntry2);
    }
}
