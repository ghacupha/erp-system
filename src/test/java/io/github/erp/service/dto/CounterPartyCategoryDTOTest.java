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

class CounterPartyCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterPartyCategoryDTO.class);
        CounterPartyCategoryDTO counterPartyCategoryDTO1 = new CounterPartyCategoryDTO();
        counterPartyCategoryDTO1.setId(1L);
        CounterPartyCategoryDTO counterPartyCategoryDTO2 = new CounterPartyCategoryDTO();
        assertThat(counterPartyCategoryDTO1).isNotEqualTo(counterPartyCategoryDTO2);
        counterPartyCategoryDTO2.setId(counterPartyCategoryDTO1.getId());
        assertThat(counterPartyCategoryDTO1).isEqualTo(counterPartyCategoryDTO2);
        counterPartyCategoryDTO2.setId(2L);
        assertThat(counterPartyCategoryDTO1).isNotEqualTo(counterPartyCategoryDTO2);
        counterPartyCategoryDTO1.setId(null);
        assertThat(counterPartyCategoryDTO1).isNotEqualTo(counterPartyCategoryDTO2);
    }
}
