package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 9 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentLabelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentLabelDTO.class);
        PaymentLabelDTO paymentLabelDTO1 = new PaymentLabelDTO();
        paymentLabelDTO1.setId(1L);
        PaymentLabelDTO paymentLabelDTO2 = new PaymentLabelDTO();
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
        paymentLabelDTO2.setId(paymentLabelDTO1.getId());
        assertThat(paymentLabelDTO1).isEqualTo(paymentLabelDTO2);
        paymentLabelDTO2.setId(2L);
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
        paymentLabelDTO1.setId(null);
        assertThat(paymentLabelDTO1).isNotEqualTo(paymentLabelDTO2);
    }
}
