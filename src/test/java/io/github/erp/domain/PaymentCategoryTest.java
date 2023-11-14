package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

class PaymentCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCategory.class);
        PaymentCategory paymentCategory1 = new PaymentCategory();
        paymentCategory1.setId(1L);
        PaymentCategory paymentCategory2 = new PaymentCategory();
        paymentCategory2.setId(paymentCategory1.getId());
        assertThat(paymentCategory1).isEqualTo(paymentCategory2);
        paymentCategory2.setId(2L);
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);
        paymentCategory1.setId(null);
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);
    }
}
