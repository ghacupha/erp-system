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

class ExchangeRateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExchangeRate.class);
        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setId(1L);
        ExchangeRate exchangeRate2 = new ExchangeRate();
        exchangeRate2.setId(exchangeRate1.getId());
        assertThat(exchangeRate1).isEqualTo(exchangeRate2);
        exchangeRate2.setId(2L);
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);
        exchangeRate1.setId(null);
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);
    }
}
