package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionAccount.class);
        TransactionAccount transactionAccount1 = new TransactionAccount();
        transactionAccount1.setId(1L);
        TransactionAccount transactionAccount2 = new TransactionAccount();
        transactionAccount2.setId(transactionAccount1.getId());
        assertThat(transactionAccount1).isEqualTo(transactionAccount2);
        transactionAccount2.setId(2L);
        assertThat(transactionAccount1).isNotEqualTo(transactionAccount2);
        transactionAccount1.setId(null);
        assertThat(transactionAccount1).isNotEqualTo(transactionAccount2);
    }
}
