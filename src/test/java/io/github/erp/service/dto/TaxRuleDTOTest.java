package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class TaxRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxRuleDTO.class);
        TaxRuleDTO taxRuleDTO1 = new TaxRuleDTO();
        taxRuleDTO1.setId(1L);
        TaxRuleDTO taxRuleDTO2 = new TaxRuleDTO();
        assertThat(taxRuleDTO1).isNotEqualTo(taxRuleDTO2);
        taxRuleDTO2.setId(taxRuleDTO1.getId());
        assertThat(taxRuleDTO1).isEqualTo(taxRuleDTO2);
        taxRuleDTO2.setId(2L);
        assertThat(taxRuleDTO1).isNotEqualTo(taxRuleDTO2);
        taxRuleDTO1.setId(null);
        assertThat(taxRuleDTO1).isNotEqualTo(taxRuleDTO2);
    }
}
