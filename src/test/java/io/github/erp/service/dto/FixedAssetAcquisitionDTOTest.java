package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class FixedAssetAcquisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetAcquisitionDTO.class);
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO1 = new FixedAssetAcquisitionDTO();
        fixedAssetAcquisitionDTO1.setId(1L);
        FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO2 = new FixedAssetAcquisitionDTO();
        assertThat(fixedAssetAcquisitionDTO1).isNotEqualTo(fixedAssetAcquisitionDTO2);
        fixedAssetAcquisitionDTO2.setId(fixedAssetAcquisitionDTO1.getId());
        assertThat(fixedAssetAcquisitionDTO1).isEqualTo(fixedAssetAcquisitionDTO2);
        fixedAssetAcquisitionDTO2.setId(2L);
        assertThat(fixedAssetAcquisitionDTO1).isNotEqualTo(fixedAssetAcquisitionDTO2);
        fixedAssetAcquisitionDTO1.setId(null);
        assertThat(fixedAssetAcquisitionDTO1).isNotEqualTo(fixedAssetAcquisitionDTO2);
    }
}
