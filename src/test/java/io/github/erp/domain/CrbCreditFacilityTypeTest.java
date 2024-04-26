package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrbCreditFacilityTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbCreditFacilityType.class);
        CrbCreditFacilityType crbCreditFacilityType1 = new CrbCreditFacilityType();
        crbCreditFacilityType1.setId(1L);
        CrbCreditFacilityType crbCreditFacilityType2 = new CrbCreditFacilityType();
        crbCreditFacilityType2.setId(crbCreditFacilityType1.getId());
        assertThat(crbCreditFacilityType1).isEqualTo(crbCreditFacilityType2);
        crbCreditFacilityType2.setId(2L);
        assertThat(crbCreditFacilityType1).isNotEqualTo(crbCreditFacilityType2);
        crbCreditFacilityType1.setId(null);
        assertThat(crbCreditFacilityType1).isNotEqualTo(crbCreditFacilityType2);
    }
}
