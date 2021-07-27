package io.github.erp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class FixedAssetDepreciationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetDepreciation.class);
        FixedAssetDepreciation fixedAssetDepreciation1 = new FixedAssetDepreciation();
        fixedAssetDepreciation1.setId(1L);
        FixedAssetDepreciation fixedAssetDepreciation2 = new FixedAssetDepreciation();
        fixedAssetDepreciation2.setId(fixedAssetDepreciation1.getId());
        assertThat(fixedAssetDepreciation1).isEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation2.setId(2L);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation1.setId(null);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
    }
}
