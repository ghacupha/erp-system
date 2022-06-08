package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAccount.class);
        PrepaymentAccount prepaymentAccount1 = new PrepaymentAccount();
        prepaymentAccount1.setId(1L);
        PrepaymentAccount prepaymentAccount2 = new PrepaymentAccount();
        prepaymentAccount2.setId(prepaymentAccount1.getId());
        assertThat(prepaymentAccount1).isEqualTo(prepaymentAccount2);
        prepaymentAccount2.setId(2L);
        assertThat(prepaymentAccount1).isNotEqualTo(prepaymentAccount2);
        prepaymentAccount1.setId(null);
        assertThat(prepaymentAccount1).isNotEqualTo(prepaymentAccount2);
    }
}
