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

class CardChargesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardChargesDTO.class);
        CardChargesDTO cardChargesDTO1 = new CardChargesDTO();
        cardChargesDTO1.setId(1L);
        CardChargesDTO cardChargesDTO2 = new CardChargesDTO();
        assertThat(cardChargesDTO1).isNotEqualTo(cardChargesDTO2);
        cardChargesDTO2.setId(cardChargesDTO1.getId());
        assertThat(cardChargesDTO1).isEqualTo(cardChargesDTO2);
        cardChargesDTO2.setId(2L);
        assertThat(cardChargesDTO1).isNotEqualTo(cardChargesDTO2);
        cardChargesDTO1.setId(null);
        assertThat(cardChargesDTO1).isNotEqualTo(cardChargesDTO2);
    }
}
