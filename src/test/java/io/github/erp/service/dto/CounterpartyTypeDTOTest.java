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

class CounterpartyTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterpartyTypeDTO.class);
        CounterpartyTypeDTO counterpartyTypeDTO1 = new CounterpartyTypeDTO();
        counterpartyTypeDTO1.setId(1L);
        CounterpartyTypeDTO counterpartyTypeDTO2 = new CounterpartyTypeDTO();
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO2.setId(counterpartyTypeDTO1.getId());
        assertThat(counterpartyTypeDTO1).isEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO2.setId(2L);
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO1.setId(null);
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
    }
}
