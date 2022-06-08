package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class UniversallyUniqueMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversallyUniqueMapping.class);
        UniversallyUniqueMapping universallyUniqueMapping1 = new UniversallyUniqueMapping();
        universallyUniqueMapping1.setId(1L);
        UniversallyUniqueMapping universallyUniqueMapping2 = new UniversallyUniqueMapping();
        universallyUniqueMapping2.setId(universallyUniqueMapping1.getId());
        assertThat(universallyUniqueMapping1).isEqualTo(universallyUniqueMapping2);
        universallyUniqueMapping2.setId(2L);
        assertThat(universallyUniqueMapping1).isNotEqualTo(universallyUniqueMapping2);
        universallyUniqueMapping1.setId(null);
        assertThat(universallyUniqueMapping1).isNotEqualTo(universallyUniqueMapping2);
    }
}
