package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class FixedAssetAcquisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetAcquisition.class);
        FixedAssetAcquisition fixedAssetAcquisition1 = new FixedAssetAcquisition();
        fixedAssetAcquisition1.setId(1L);
        FixedAssetAcquisition fixedAssetAcquisition2 = new FixedAssetAcquisition();
        fixedAssetAcquisition2.setId(fixedAssetAcquisition1.getId());
        assertThat(fixedAssetAcquisition1).isEqualTo(fixedAssetAcquisition2);
        fixedAssetAcquisition2.setId(2L);
        assertThat(fixedAssetAcquisition1).isNotEqualTo(fixedAssetAcquisition2);
        fixedAssetAcquisition1.setId(null);
        assertThat(fixedAssetAcquisition1).isNotEqualTo(fixedAssetAcquisition2);
    }
}
