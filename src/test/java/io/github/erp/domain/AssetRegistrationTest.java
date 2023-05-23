package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegistration.class);
        AssetRegistration assetRegistration1 = new AssetRegistration();
        assetRegistration1.setId(1L);
        AssetRegistration assetRegistration2 = new AssetRegistration();
        assetRegistration2.setId(assetRegistration1.getId());
        assertThat(assetRegistration1).isEqualTo(assetRegistration2);
        assetRegistration2.setId(2L);
        assertThat(assetRegistration1).isNotEqualTo(assetRegistration2);
        assetRegistration1.setId(null);
        assertThat(assetRegistration1).isNotEqualTo(assetRegistration2);
    }
}
