package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankBranchCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankBranchCodeDTO.class);
        BankBranchCodeDTO bankBranchCodeDTO1 = new BankBranchCodeDTO();
        bankBranchCodeDTO1.setId(1L);
        BankBranchCodeDTO bankBranchCodeDTO2 = new BankBranchCodeDTO();
        assertThat(bankBranchCodeDTO1).isNotEqualTo(bankBranchCodeDTO2);
        bankBranchCodeDTO2.setId(bankBranchCodeDTO1.getId());
        assertThat(bankBranchCodeDTO1).isEqualTo(bankBranchCodeDTO2);
        bankBranchCodeDTO2.setId(2L);
        assertThat(bankBranchCodeDTO1).isNotEqualTo(bankBranchCodeDTO2);
        bankBranchCodeDTO1.setId(null);
        assertThat(bankBranchCodeDTO1).isNotEqualTo(bankBranchCodeDTO2);
    }
}
