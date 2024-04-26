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

class CounterPartyCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterPartyCategory.class);
        CounterPartyCategory counterPartyCategory1 = new CounterPartyCategory();
        counterPartyCategory1.setId(1L);
        CounterPartyCategory counterPartyCategory2 = new CounterPartyCategory();
        counterPartyCategory2.setId(counterPartyCategory1.getId());
        assertThat(counterPartyCategory1).isEqualTo(counterPartyCategory2);
        counterPartyCategory2.setId(2L);
        assertThat(counterPartyCategory1).isNotEqualTo(counterPartyCategory2);
        counterPartyCategory1.setId(null);
        assertThat(counterPartyCategory1).isNotEqualTo(counterPartyCategory2);
    }
}
