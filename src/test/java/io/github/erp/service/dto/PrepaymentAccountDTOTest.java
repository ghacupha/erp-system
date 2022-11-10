package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAccountDTO.class);
        PrepaymentAccountDTO prepaymentAccountDTO1 = new PrepaymentAccountDTO();
        prepaymentAccountDTO1.setId(1L);
        PrepaymentAccountDTO prepaymentAccountDTO2 = new PrepaymentAccountDTO();
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO2.setId(prepaymentAccountDTO1.getId());
        assertThat(prepaymentAccountDTO1).isEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO2.setId(2L);
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO1.setId(null);
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
    }
}
