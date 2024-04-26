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

class CardBrandTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardBrandType.class);
        CardBrandType cardBrandType1 = new CardBrandType();
        cardBrandType1.setId(1L);
        CardBrandType cardBrandType2 = new CardBrandType();
        cardBrandType2.setId(cardBrandType1.getId());
        assertThat(cardBrandType1).isEqualTo(cardBrandType2);
        cardBrandType2.setId(2L);
        assertThat(cardBrandType1).isNotEqualTo(cardBrandType2);
        cardBrandType1.setId(null);
        assertThat(cardBrandType1).isNotEqualTo(cardBrandType2);
    }
}
