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

class SignedPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignedPayment.class);
        SignedPayment signedPayment1 = new SignedPayment();
        signedPayment1.setId(1L);
        SignedPayment signedPayment2 = new SignedPayment();
        signedPayment2.setId(signedPayment1.getId());
        assertThat(signedPayment1).isEqualTo(signedPayment2);
        signedPayment2.setId(2L);
        assertThat(signedPayment1).isNotEqualTo(signedPayment2);
        signedPayment1.setId(null);
        assertThat(signedPayment1).isNotEqualTo(signedPayment2);
    }
}
