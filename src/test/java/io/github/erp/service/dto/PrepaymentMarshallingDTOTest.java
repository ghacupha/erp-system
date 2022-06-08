package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentMarshallingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMarshallingDTO.class);
        PrepaymentMarshallingDTO prepaymentMarshallingDTO1 = new PrepaymentMarshallingDTO();
        prepaymentMarshallingDTO1.setId(1L);
        PrepaymentMarshallingDTO prepaymentMarshallingDTO2 = new PrepaymentMarshallingDTO();
        assertThat(prepaymentMarshallingDTO1).isNotEqualTo(prepaymentMarshallingDTO2);
        prepaymentMarshallingDTO2.setId(prepaymentMarshallingDTO1.getId());
        assertThat(prepaymentMarshallingDTO1).isEqualTo(prepaymentMarshallingDTO2);
        prepaymentMarshallingDTO2.setId(2L);
        assertThat(prepaymentMarshallingDTO1).isNotEqualTo(prepaymentMarshallingDTO2);
        prepaymentMarshallingDTO1.setId(null);
        assertThat(prepaymentMarshallingDTO1).isNotEqualTo(prepaymentMarshallingDTO2);
    }
}
