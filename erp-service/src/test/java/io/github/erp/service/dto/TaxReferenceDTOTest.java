package io.github.erp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class TaxReferenceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxReferenceDTO.class);
        TaxReferenceDTO taxReferenceDTO1 = new TaxReferenceDTO();
        taxReferenceDTO1.setId(1L);
        TaxReferenceDTO taxReferenceDTO2 = new TaxReferenceDTO();
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(taxReferenceDTO1.getId());
        assertThat(taxReferenceDTO1).isEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(2L);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO1.setId(null);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
    }
}
