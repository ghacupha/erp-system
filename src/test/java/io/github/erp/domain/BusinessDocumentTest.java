package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessDocument.class);
        BusinessDocument businessDocument1 = new BusinessDocument();
        businessDocument1.setId(1L);
        BusinessDocument businessDocument2 = new BusinessDocument();
        businessDocument2.setId(businessDocument1.getId());
        assertThat(businessDocument1).isEqualTo(businessDocument2);
        businessDocument2.setId(2L);
        assertThat(businessDocument1).isNotEqualTo(businessDocument2);
        businessDocument1.setId(null);
        assertThat(businessDocument1).isNotEqualTo(businessDocument2);
    }
}
