package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class CreditNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditNote.class);
        CreditNote creditNote1 = new CreditNote();
        creditNote1.setId(1L);
        CreditNote creditNote2 = new CreditNote();
        creditNote2.setId(creditNote1.getId());
        assertThat(creditNote1).isEqualTo(creditNote2);
        creditNote2.setId(2L);
        assertThat(creditNote1).isNotEqualTo(creditNote2);
        creditNote1.setId(null);
        assertThat(creditNote1).isNotEqualTo(creditNote2);
    }
}
