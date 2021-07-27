package io.github.erp.domain;

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
