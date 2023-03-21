package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractMetadataDTO.class);
        ContractMetadataDTO contractMetadataDTO1 = new ContractMetadataDTO();
        contractMetadataDTO1.setId(1L);
        ContractMetadataDTO contractMetadataDTO2 = new ContractMetadataDTO();
        assertThat(contractMetadataDTO1).isNotEqualTo(contractMetadataDTO2);
        contractMetadataDTO2.setId(contractMetadataDTO1.getId());
        assertThat(contractMetadataDTO1).isEqualTo(contractMetadataDTO2);
        contractMetadataDTO2.setId(2L);
        assertThat(contractMetadataDTO1).isNotEqualTo(contractMetadataDTO2);
        contractMetadataDTO1.setId(null);
        assertThat(contractMetadataDTO1).isNotEqualTo(contractMetadataDTO2);
    }
}
