package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCategory.class);
        AssetCategory assetCategory1 = new AssetCategory();
        assetCategory1.setId(1L);
        AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setId(assetCategory1.getId());
        assertThat(assetCategory1).isEqualTo(assetCategory2);
        assetCategory2.setId(2L);
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);
        assetCategory1.setId(null);
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);
    }
}
