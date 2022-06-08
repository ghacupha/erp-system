package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class CreditNoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditNoteDTO.class);
        CreditNoteDTO creditNoteDTO1 = new CreditNoteDTO();
        creditNoteDTO1.setId(1L);
        CreditNoteDTO creditNoteDTO2 = new CreditNoteDTO();
        assertThat(creditNoteDTO1).isNotEqualTo(creditNoteDTO2);
        creditNoteDTO2.setId(creditNoteDTO1.getId());
        assertThat(creditNoteDTO1).isEqualTo(creditNoteDTO2);
        creditNoteDTO2.setId(2L);
        assertThat(creditNoteDTO1).isNotEqualTo(creditNoteDTO2);
        creditNoteDTO1.setId(null);
        assertThat(creditNoteDTO1).isNotEqualTo(creditNoteDTO2);
    }
}
