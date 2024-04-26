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

class CardTypesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardTypesDTO.class);
        CardTypesDTO cardTypesDTO1 = new CardTypesDTO();
        cardTypesDTO1.setId(1L);
        CardTypesDTO cardTypesDTO2 = new CardTypesDTO();
        assertThat(cardTypesDTO1).isNotEqualTo(cardTypesDTO2);
        cardTypesDTO2.setId(cardTypesDTO1.getId());
        assertThat(cardTypesDTO1).isEqualTo(cardTypesDTO2);
        cardTypesDTO2.setId(2L);
        assertThat(cardTypesDTO1).isNotEqualTo(cardTypesDTO2);
        cardTypesDTO1.setId(null);
        assertThat(cardTypesDTO1).isNotEqualTo(cardTypesDTO2);
    }
}
