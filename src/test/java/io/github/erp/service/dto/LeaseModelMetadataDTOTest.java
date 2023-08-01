package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseModelMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseModelMetadataDTO.class);
        LeaseModelMetadataDTO leaseModelMetadataDTO1 = new LeaseModelMetadataDTO();
        leaseModelMetadataDTO1.setId(1L);
        LeaseModelMetadataDTO leaseModelMetadataDTO2 = new LeaseModelMetadataDTO();
        assertThat(leaseModelMetadataDTO1).isNotEqualTo(leaseModelMetadataDTO2);
        leaseModelMetadataDTO2.setId(leaseModelMetadataDTO1.getId());
        assertThat(leaseModelMetadataDTO1).isEqualTo(leaseModelMetadataDTO2);
        leaseModelMetadataDTO2.setId(2L);
        assertThat(leaseModelMetadataDTO1).isNotEqualTo(leaseModelMetadataDTO2);
        leaseModelMetadataDTO1.setId(null);
        assertThat(leaseModelMetadataDTO1).isNotEqualTo(leaseModelMetadataDTO2);
    }
}
