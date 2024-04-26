package io.github.erp.service.dto;

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

class CrbCreditFacilityTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbCreditFacilityTypeDTO.class);
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO1 = new CrbCreditFacilityTypeDTO();
        crbCreditFacilityTypeDTO1.setId(1L);
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO2 = new CrbCreditFacilityTypeDTO();
        assertThat(crbCreditFacilityTypeDTO1).isNotEqualTo(crbCreditFacilityTypeDTO2);
        crbCreditFacilityTypeDTO2.setId(crbCreditFacilityTypeDTO1.getId());
        assertThat(crbCreditFacilityTypeDTO1).isEqualTo(crbCreditFacilityTypeDTO2);
        crbCreditFacilityTypeDTO2.setId(2L);
        assertThat(crbCreditFacilityTypeDTO1).isNotEqualTo(crbCreditFacilityTypeDTO2);
        crbCreditFacilityTypeDTO1.setId(null);
        assertThat(crbCreditFacilityTypeDTO1).isNotEqualTo(crbCreditFacilityTypeDTO2);
    }
}
