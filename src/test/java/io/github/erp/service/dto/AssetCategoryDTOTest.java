package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class AssetCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCategoryDTO.class);
        AssetCategoryDTO assetCategoryDTO1 = new AssetCategoryDTO();
        assetCategoryDTO1.setId(1L);
        AssetCategoryDTO assetCategoryDTO2 = new AssetCategoryDTO();
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
        assetCategoryDTO2.setId(assetCategoryDTO1.getId());
        assertThat(assetCategoryDTO1).isEqualTo(assetCategoryDTO2);
        assetCategoryDTO2.setId(2L);
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
        assetCategoryDTO1.setId(null);
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
    }
}
