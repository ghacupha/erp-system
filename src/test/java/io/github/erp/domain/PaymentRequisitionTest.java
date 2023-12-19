package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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

class PaymentRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentRequisition.class);
        PaymentRequisition paymentRequisition1 = new PaymentRequisition();
        paymentRequisition1.setId(1L);
        PaymentRequisition paymentRequisition2 = new PaymentRequisition();
        paymentRequisition2.setId(paymentRequisition1.getId());
        assertThat(paymentRequisition1).isEqualTo(paymentRequisition2);
        paymentRequisition2.setId(2L);
        assertThat(paymentRequisition1).isNotEqualTo(paymentRequisition2);
        paymentRequisition1.setId(null);
        assertThat(paymentRequisition1).isNotEqualTo(paymentRequisition2);
    }
}
