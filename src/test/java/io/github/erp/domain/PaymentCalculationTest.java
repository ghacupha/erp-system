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

class PaymentCalculationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCalculation.class);
        PaymentCalculation paymentCalculation1 = new PaymentCalculation();
        paymentCalculation1.setId(1L);
        PaymentCalculation paymentCalculation2 = new PaymentCalculation();
        paymentCalculation2.setId(paymentCalculation1.getId());
        assertThat(paymentCalculation1).isEqualTo(paymentCalculation2);
        paymentCalculation2.setId(2L);
        assertThat(paymentCalculation1).isNotEqualTo(paymentCalculation2);
        paymentCalculation1.setId(null);
        assertThat(paymentCalculation1).isNotEqualTo(paymentCalculation2);
    }
}
