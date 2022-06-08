package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentMarshallingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMarshalling.class);
        PrepaymentMarshalling prepaymentMarshalling1 = new PrepaymentMarshalling();
        prepaymentMarshalling1.setId(1L);
        PrepaymentMarshalling prepaymentMarshalling2 = new PrepaymentMarshalling();
        prepaymentMarshalling2.setId(prepaymentMarshalling1.getId());
        assertThat(prepaymentMarshalling1).isEqualTo(prepaymentMarshalling2);
        prepaymentMarshalling2.setId(2L);
        assertThat(prepaymentMarshalling1).isNotEqualTo(prepaymentMarshalling2);
        prepaymentMarshalling1.setId(null);
        assertThat(prepaymentMarshalling1).isNotEqualTo(prepaymentMarshalling2);
    }
}
