package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessDocumentDTO.class);
        BusinessDocumentDTO businessDocumentDTO1 = new BusinessDocumentDTO();
        businessDocumentDTO1.setId(1L);
        BusinessDocumentDTO businessDocumentDTO2 = new BusinessDocumentDTO();
        assertThat(businessDocumentDTO1).isNotEqualTo(businessDocumentDTO2);
        businessDocumentDTO2.setId(businessDocumentDTO1.getId());
        assertThat(businessDocumentDTO1).isEqualTo(businessDocumentDTO2);
        businessDocumentDTO2.setId(2L);
        assertThat(businessDocumentDTO1).isNotEqualTo(businessDocumentDTO2);
        businessDocumentDTO1.setId(null);
        assertThat(businessDocumentDTO1).isNotEqualTo(businessDocumentDTO2);
    }
}
