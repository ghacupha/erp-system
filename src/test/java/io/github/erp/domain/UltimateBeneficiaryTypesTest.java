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

class UltimateBeneficiaryTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UltimateBeneficiaryTypes.class);
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes1 = new UltimateBeneficiaryTypes();
        ultimateBeneficiaryTypes1.setId(1L);
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes2 = new UltimateBeneficiaryTypes();
        ultimateBeneficiaryTypes2.setId(ultimateBeneficiaryTypes1.getId());
        assertThat(ultimateBeneficiaryTypes1).isEqualTo(ultimateBeneficiaryTypes2);
        ultimateBeneficiaryTypes2.setId(2L);
        assertThat(ultimateBeneficiaryTypes1).isNotEqualTo(ultimateBeneficiaryTypes2);
        ultimateBeneficiaryTypes1.setId(null);
        assertThat(ultimateBeneficiaryTypes1).isNotEqualTo(ultimateBeneficiaryTypes2);
    }
}
