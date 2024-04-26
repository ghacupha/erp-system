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

class PrepaymentAmortizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAmortization.class);
        PrepaymentAmortization prepaymentAmortization1 = new PrepaymentAmortization();
        prepaymentAmortization1.setId(1L);
        PrepaymentAmortization prepaymentAmortization2 = new PrepaymentAmortization();
        prepaymentAmortization2.setId(prepaymentAmortization1.getId());
        assertThat(prepaymentAmortization1).isEqualTo(prepaymentAmortization2);
        prepaymentAmortization2.setId(2L);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
        prepaymentAmortization1.setId(null);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
    }
}
