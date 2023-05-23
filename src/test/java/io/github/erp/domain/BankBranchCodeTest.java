package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankBranchCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankBranchCode.class);
        BankBranchCode bankBranchCode1 = new BankBranchCode();
        bankBranchCode1.setId(1L);
        BankBranchCode bankBranchCode2 = new BankBranchCode();
        bankBranchCode2.setId(bankBranchCode1.getId());
        assertThat(bankBranchCode1).isEqualTo(bankBranchCode2);
        bankBranchCode2.setId(2L);
        assertThat(bankBranchCode1).isNotEqualTo(bankBranchCode2);
        bankBranchCode1.setId(null);
        assertThat(bankBranchCode1).isNotEqualTo(bankBranchCode2);
    }
}
