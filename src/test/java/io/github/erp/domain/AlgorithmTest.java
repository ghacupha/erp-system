package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlgorithmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Algorithm.class);
        Algorithm algorithm1 = new Algorithm();
        algorithm1.setId(1L);
        Algorithm algorithm2 = new Algorithm();
        algorithm2.setId(algorithm1.getId());
        assertThat(algorithm1).isEqualTo(algorithm2);
        algorithm2.setId(2L);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
        algorithm1.setId(null);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
    }
}
