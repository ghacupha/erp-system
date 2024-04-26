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

class PaymentLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentLabel.class);
        PaymentLabel paymentLabel1 = new PaymentLabel();
        paymentLabel1.setId(1L);
        PaymentLabel paymentLabel2 = new PaymentLabel();
        paymentLabel2.setId(paymentLabel1.getId());
        assertThat(paymentLabel1).isEqualTo(paymentLabel2);
        paymentLabel2.setId(2L);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
        paymentLabel1.setId(null);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
    }
}
