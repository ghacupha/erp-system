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

class LegalStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalStatusDTO.class);
        LegalStatusDTO legalStatusDTO1 = new LegalStatusDTO();
        legalStatusDTO1.setId(1L);
        LegalStatusDTO legalStatusDTO2 = new LegalStatusDTO();
        assertThat(legalStatusDTO1).isNotEqualTo(legalStatusDTO2);
        legalStatusDTO2.setId(legalStatusDTO1.getId());
        assertThat(legalStatusDTO1).isEqualTo(legalStatusDTO2);
        legalStatusDTO2.setId(2L);
        assertThat(legalStatusDTO1).isNotEqualTo(legalStatusDTO2);
        legalStatusDTO1.setId(null);
        assertThat(legalStatusDTO1).isNotEqualTo(legalStatusDTO2);
    }
}
