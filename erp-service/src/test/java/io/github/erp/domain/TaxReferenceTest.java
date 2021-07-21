package io.github.erp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class TaxReferenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxReference.class);
        TaxReference taxReference1 = new TaxReference();
        taxReference1.setId(1L);
        TaxReference taxReference2 = new TaxReference();
        taxReference2.setId(taxReference1.getId());
        assertThat(taxReference1).isEqualTo(taxReference2);
        taxReference2.setId(2L);
        assertThat(taxReference1).isNotEqualTo(taxReference2);
        taxReference1.setId(null);
        assertThat(taxReference1).isNotEqualTo(taxReference2);
    }
}
