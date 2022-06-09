package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
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

class PaymentRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentRequisitionDTO.class);
        PaymentRequisitionDTO paymentRequisitionDTO1 = new PaymentRequisitionDTO();
        paymentRequisitionDTO1.setId(1L);
        PaymentRequisitionDTO paymentRequisitionDTO2 = new PaymentRequisitionDTO();
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO2.setId(paymentRequisitionDTO1.getId());
        assertThat(paymentRequisitionDTO1).isEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO2.setId(2L);
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
        paymentRequisitionDTO1.setId(null);
        assertThat(paymentRequisitionDTO1).isNotEqualTo(paymentRequisitionDTO2);
    }
}
