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

class FxTransactionRateTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxTransactionRateType.class);
        FxTransactionRateType fxTransactionRateType1 = new FxTransactionRateType();
        fxTransactionRateType1.setId(1L);
        FxTransactionRateType fxTransactionRateType2 = new FxTransactionRateType();
        fxTransactionRateType2.setId(fxTransactionRateType1.getId());
        assertThat(fxTransactionRateType1).isEqualTo(fxTransactionRateType2);
        fxTransactionRateType2.setId(2L);
        assertThat(fxTransactionRateType1).isNotEqualTo(fxTransactionRateType2);
        fxTransactionRateType1.setId(null);
        assertThat(fxTransactionRateType1).isNotEqualTo(fxTransactionRateType2);
    }
}
