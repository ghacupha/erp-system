package io.github.erp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class DealerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealerDTO.class);
        DealerDTO dealerDTO1 = new DealerDTO();
        dealerDTO1.setId(1L);
        DealerDTO dealerDTO2 = new DealerDTO();
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
        dealerDTO2.setId(dealerDTO1.getId());
        assertThat(dealerDTO1).isEqualTo(dealerDTO2);
        dealerDTO2.setId(2L);
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
        dealerDTO1.setId(null);
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
    }
}
