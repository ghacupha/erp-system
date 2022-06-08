package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTokenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageToken.class);
        MessageToken messageToken1 = new MessageToken();
        messageToken1.setId(1L);
        MessageToken messageToken2 = new MessageToken();
        messageToken2.setId(messageToken1.getId());
        assertThat(messageToken1).isEqualTo(messageToken2);
        messageToken2.setId(2L);
        assertThat(messageToken1).isNotEqualTo(messageToken2);
        messageToken1.setId(null);
        assertThat(messageToken1).isNotEqualTo(messageToken2);
    }
}
