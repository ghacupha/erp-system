package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetWarrantyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetWarranty.class);
        AssetWarranty assetWarranty1 = new AssetWarranty();
        assetWarranty1.setId(1L);
        AssetWarranty assetWarranty2 = new AssetWarranty();
        assetWarranty2.setId(assetWarranty1.getId());
        assertThat(assetWarranty1).isEqualTo(assetWarranty2);
        assetWarranty2.setId(2L);
        assertThat(assetWarranty1).isNotEqualTo(assetWarranty2);
        assetWarranty1.setId(null);
        assertThat(assetWarranty1).isNotEqualTo(assetWarranty2);
    }
}
