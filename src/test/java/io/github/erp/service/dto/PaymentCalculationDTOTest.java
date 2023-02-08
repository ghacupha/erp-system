package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.erp.resources.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCalculationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
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
