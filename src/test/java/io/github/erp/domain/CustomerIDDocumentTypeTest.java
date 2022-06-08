package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerIDDocumentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerIDDocumentType.class);
        CustomerIDDocumentType customerIDDocumentType1 = new CustomerIDDocumentType();
        customerIDDocumentType1.setId(1L);
        CustomerIDDocumentType customerIDDocumentType2 = new CustomerIDDocumentType();
        customerIDDocumentType2.setId(customerIDDocumentType1.getId());
        assertThat(customerIDDocumentType1).isEqualTo(customerIDDocumentType2);
        customerIDDocumentType2.setId(2L);
        assertThat(customerIDDocumentType1).isNotEqualTo(customerIDDocumentType2);
        customerIDDocumentType1.setId(null);
        assertThat(customerIDDocumentType1).isNotEqualTo(customerIDDocumentType2);
    }
}
