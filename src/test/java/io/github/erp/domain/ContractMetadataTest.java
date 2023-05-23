package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractMetadata.class);
        ContractMetadata contractMetadata1 = new ContractMetadata();
        contractMetadata1.setId(1L);
        ContractMetadata contractMetadata2 = new ContractMetadata();
        contractMetadata2.setId(contractMetadata1.getId());
        assertThat(contractMetadata1).isEqualTo(contractMetadata2);
        contractMetadata2.setId(2L);
        assertThat(contractMetadata1).isNotEqualTo(contractMetadata2);
        contractMetadata1.setId(null);
        assertThat(contractMetadata1).isNotEqualTo(contractMetadata2);
    }
}
