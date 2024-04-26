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

class MerchantTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MerchantType.class);
        MerchantType merchantType1 = new MerchantType();
        merchantType1.setId(1L);
        MerchantType merchantType2 = new MerchantType();
        merchantType2.setId(merchantType1.getId());
        assertThat(merchantType1).isEqualTo(merchantType2);
        merchantType2.setId(2L);
        assertThat(merchantType1).isNotEqualTo(merchantType2);
        merchantType1.setId(null);
        assertThat(merchantType1).isNotEqualTo(merchantType2);
    }
}
