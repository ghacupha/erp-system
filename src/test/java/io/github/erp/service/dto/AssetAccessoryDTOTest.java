package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetAccessoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAccessoryDTO.class);
        AssetAccessoryDTO assetAccessoryDTO1 = new AssetAccessoryDTO();
        assetAccessoryDTO1.setId(1L);
        AssetAccessoryDTO assetAccessoryDTO2 = new AssetAccessoryDTO();
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO2.setId(assetAccessoryDTO1.getId());
        assertThat(assetAccessoryDTO1).isEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO2.setId(2L);
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO1.setId(null);
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
    }
}
