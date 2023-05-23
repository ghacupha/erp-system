package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseModelMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseModelMetadata.class);
        LeaseModelMetadata leaseModelMetadata1 = new LeaseModelMetadata();
        leaseModelMetadata1.setId(1L);
        LeaseModelMetadata leaseModelMetadata2 = new LeaseModelMetadata();
        leaseModelMetadata2.setId(leaseModelMetadata1.getId());
        assertThat(leaseModelMetadata1).isEqualTo(leaseModelMetadata2);
        leaseModelMetadata2.setId(2L);
        assertThat(leaseModelMetadata1).isNotEqualTo(leaseModelMetadata2);
        leaseModelMetadata1.setId(null);
        assertThat(leaseModelMetadata1).isNotEqualTo(leaseModelMetadata2);
    }
}
