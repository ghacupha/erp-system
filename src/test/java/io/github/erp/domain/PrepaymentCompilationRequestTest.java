package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentCompilationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentCompilationRequest.class);
        PrepaymentCompilationRequest prepaymentCompilationRequest1 = new PrepaymentCompilationRequest();
        prepaymentCompilationRequest1.setId(1L);
        PrepaymentCompilationRequest prepaymentCompilationRequest2 = new PrepaymentCompilationRequest();
        prepaymentCompilationRequest2.setId(prepaymentCompilationRequest1.getId());
        assertThat(prepaymentCompilationRequest1).isEqualTo(prepaymentCompilationRequest2);
        prepaymentCompilationRequest2.setId(2L);
        assertThat(prepaymentCompilationRequest1).isNotEqualTo(prepaymentCompilationRequest2);
        prepaymentCompilationRequest1.setId(null);
        assertThat(prepaymentCompilationRequest1).isNotEqualTo(prepaymentCompilationRequest2);
    }
}
