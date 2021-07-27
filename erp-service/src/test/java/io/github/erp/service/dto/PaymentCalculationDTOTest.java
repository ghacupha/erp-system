package io.github.erp.service.dto;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class PaymentCalculationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCalculationDTO.class);
        PaymentCalculationDTO paymentCalculationDTO1 = new PaymentCalculationDTO();
        paymentCalculationDTO1.setId(1L);
        PaymentCalculationDTO paymentCalculationDTO2 = new PaymentCalculationDTO();
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO2.setId(paymentCalculationDTO1.getId());
        assertThat(paymentCalculationDTO1).isEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO2.setId(2L);
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
        paymentCalculationDTO1.setId(null);
        assertThat(paymentCalculationDTO1).isNotEqualTo(paymentCalculationDTO2);
    }
}
