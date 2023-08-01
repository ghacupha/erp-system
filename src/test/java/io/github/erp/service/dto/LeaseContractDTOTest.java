package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseContractDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseContractDTO.class);
        LeaseContractDTO leaseContractDTO1 = new LeaseContractDTO();
        leaseContractDTO1.setId(1L);
        LeaseContractDTO leaseContractDTO2 = new LeaseContractDTO();
        assertThat(leaseContractDTO1).isNotEqualTo(leaseContractDTO2);
        leaseContractDTO2.setId(leaseContractDTO1.getId());
        assertThat(leaseContractDTO1).isEqualTo(leaseContractDTO2);
        leaseContractDTO2.setId(2L);
        assertThat(leaseContractDTO1).isNotEqualTo(leaseContractDTO2);
        leaseContractDTO1.setId(null);
        assertThat(leaseContractDTO1).isNotEqualTo(leaseContractDTO2);
    }
}
