package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentCompilationRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentCompilationRequestDTO.class);
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO1 = new PrepaymentCompilationRequestDTO();
        prepaymentCompilationRequestDTO1.setId(1L);
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO2 = new PrepaymentCompilationRequestDTO();
        assertThat(prepaymentCompilationRequestDTO1).isNotEqualTo(prepaymentCompilationRequestDTO2);
        prepaymentCompilationRequestDTO2.setId(prepaymentCompilationRequestDTO1.getId());
        assertThat(prepaymentCompilationRequestDTO1).isEqualTo(prepaymentCompilationRequestDTO2);
        prepaymentCompilationRequestDTO2.setId(2L);
        assertThat(prepaymentCompilationRequestDTO1).isNotEqualTo(prepaymentCompilationRequestDTO2);
        prepaymentCompilationRequestDTO1.setId(null);
        assertThat(prepaymentCompilationRequestDTO1).isNotEqualTo(prepaymentCompilationRequestDTO2);
    }
}
