package io.github.erp.service.dto;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class TaxRuleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
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
