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

class CurrencyAuthenticityFlagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyAuthenticityFlagDTO.class);
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO1 = new CurrencyAuthenticityFlagDTO();
        currencyAuthenticityFlagDTO1.setId(1L);
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO2 = new CurrencyAuthenticityFlagDTO();
        assertThat(currencyAuthenticityFlagDTO1).isNotEqualTo(currencyAuthenticityFlagDTO2);
        currencyAuthenticityFlagDTO2.setId(currencyAuthenticityFlagDTO1.getId());
        assertThat(currencyAuthenticityFlagDTO1).isEqualTo(currencyAuthenticityFlagDTO2);
        currencyAuthenticityFlagDTO2.setId(2L);
        assertThat(currencyAuthenticityFlagDTO1).isNotEqualTo(currencyAuthenticityFlagDTO2);
        currencyAuthenticityFlagDTO1.setId(null);
        assertThat(currencyAuthenticityFlagDTO1).isNotEqualTo(currencyAuthenticityFlagDTO2);
    }
}
