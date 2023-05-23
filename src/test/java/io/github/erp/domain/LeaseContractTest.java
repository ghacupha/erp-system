package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaseContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseContract.class);
        LeaseContract leaseContract1 = new LeaseContract();
        leaseContract1.setId(1L);
        LeaseContract leaseContract2 = new LeaseContract();
        leaseContract2.setId(leaseContract1.getId());
        assertThat(leaseContract1).isEqualTo(leaseContract2);
        leaseContract2.setId(2L);
        assertThat(leaseContract1).isNotEqualTo(leaseContract2);
        leaseContract1.setId(null);
        assertThat(leaseContract1).isNotEqualTo(leaseContract2);
    }
}
