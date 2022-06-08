package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerIDDocumentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerIDDocumentTypeDTO.class);
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO1 = new CustomerIDDocumentTypeDTO();
        customerIDDocumentTypeDTO1.setId(1L);
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO2 = new CustomerIDDocumentTypeDTO();
        assertThat(customerIDDocumentTypeDTO1).isNotEqualTo(customerIDDocumentTypeDTO2);
        customerIDDocumentTypeDTO2.setId(customerIDDocumentTypeDTO1.getId());
        assertThat(customerIDDocumentTypeDTO1).isEqualTo(customerIDDocumentTypeDTO2);
        customerIDDocumentTypeDTO2.setId(2L);
        assertThat(customerIDDocumentTypeDTO1).isNotEqualTo(customerIDDocumentTypeDTO2);
        customerIDDocumentTypeDTO1.setId(null);
        assertThat(customerIDDocumentTypeDTO1).isNotEqualTo(customerIDDocumentTypeDTO2);
    }
}
