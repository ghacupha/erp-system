package io.github.erp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class TaxRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxRule.class);
        TaxRule taxRule1 = new TaxRule();
        taxRule1.setId(1L);
        TaxRule taxRule2 = new TaxRule();
        taxRule2.setId(taxRule1.getId());
        assertThat(taxRule1).isEqualTo(taxRule2);
        taxRule2.setId(2L);
        assertThat(taxRule1).isNotEqualTo(taxRule2);
        taxRule1.setId(null);
        assertThat(taxRule1).isNotEqualTo(taxRule2);
    }
}
