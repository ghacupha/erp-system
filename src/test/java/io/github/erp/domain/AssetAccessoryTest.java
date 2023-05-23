package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetAccessoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAccessory.class);
        AssetAccessory assetAccessory1 = new AssetAccessory();
        assetAccessory1.setId(1L);
        AssetAccessory assetAccessory2 = new AssetAccessory();
        assetAccessory2.setId(assetAccessory1.getId());
        assertThat(assetAccessory1).isEqualTo(assetAccessory2);
        assetAccessory2.setId(2L);
        assertThat(assetAccessory1).isNotEqualTo(assetAccessory2);
        assetAccessory1.setId(null);
        assertThat(assetAccessory1).isNotEqualTo(assetAccessory2);
    }
}
