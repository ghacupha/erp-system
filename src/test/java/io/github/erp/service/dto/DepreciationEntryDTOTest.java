package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepreciationEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationEntryDTO.class);
        DepreciationEntryDTO depreciationEntryDTO1 = new DepreciationEntryDTO();
        depreciationEntryDTO1.setId(1L);
        DepreciationEntryDTO depreciationEntryDTO2 = new DepreciationEntryDTO();
        assertThat(depreciationEntryDTO1).isNotEqualTo(depreciationEntryDTO2);
        depreciationEntryDTO2.setId(depreciationEntryDTO1.getId());
        assertThat(depreciationEntryDTO1).isEqualTo(depreciationEntryDTO2);
        depreciationEntryDTO2.setId(2L);
        assertThat(depreciationEntryDTO1).isNotEqualTo(depreciationEntryDTO2);
        depreciationEntryDTO1.setId(null);
        assertThat(depreciationEntryDTO1).isNotEqualTo(depreciationEntryDTO2);
    }
}
