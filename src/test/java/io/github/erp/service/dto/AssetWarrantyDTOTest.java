package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetWarrantyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetWarrantyDTO.class);
        AssetWarrantyDTO assetWarrantyDTO1 = new AssetWarrantyDTO();
        assetWarrantyDTO1.setId(1L);
        AssetWarrantyDTO assetWarrantyDTO2 = new AssetWarrantyDTO();
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO2.setId(assetWarrantyDTO1.getId());
        assertThat(assetWarrantyDTO1).isEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO2.setId(2L);
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO1.setId(null);
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
    }
}
