package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class PaymentCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCategoryDTO.class);
        PaymentCategoryDTO paymentCategoryDTO1 = new PaymentCategoryDTO();
        paymentCategoryDTO1.setId(1L);
        PaymentCategoryDTO paymentCategoryDTO2 = new PaymentCategoryDTO();
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO2.setId(paymentCategoryDTO1.getId());
        assertThat(paymentCategoryDTO1).isEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO2.setId(2L);
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
        paymentCategoryDTO1.setId(null);
        assertThat(paymentCategoryDTO1).isNotEqualTo(paymentCategoryDTO2);
    }
}
