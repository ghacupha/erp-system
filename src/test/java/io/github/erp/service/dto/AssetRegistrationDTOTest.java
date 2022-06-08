package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class AssetRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegistrationDTO.class);
        AssetRegistrationDTO assetRegistrationDTO1 = new AssetRegistrationDTO();
        assetRegistrationDTO1.setId(1L);
        AssetRegistrationDTO assetRegistrationDTO2 = new AssetRegistrationDTO();
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO2.setId(assetRegistrationDTO1.getId());
        assertThat(assetRegistrationDTO1).isEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO2.setId(2L);
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO1.setId(null);
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
    }
}
